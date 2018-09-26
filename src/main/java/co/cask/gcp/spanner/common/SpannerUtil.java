/*
 * Copyright © 2018 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package co.cask.gcp.spanner.common;

import co.cask.cdap.api.data.schema.Schema;
import co.cask.gcp.common.GCPUtils;
import com.google.cloud.spanner.Spanner;
import com.google.cloud.spanner.SpannerOptions;
import com.google.common.collect.ImmutableSet;

import java.io.IOException;
import java.util.Set;

/**
 * Spanner utility classs to get spanner service
 */
public class SpannerUtil {
  // todo CDAP-14233 - add support for array
  private static final Set<Schema.Type> SUPPORTED_TYPES =
    ImmutableSet.of(Schema.Type.BOOLEAN, Schema.Type.STRING, Schema.Type.LONG, Schema.Type.DOUBLE,
                    Schema.Type.BYTES);

  private static final Set<Schema.LogicalType> SUPPORTED_LOGICAL_TYPES =
    ImmutableSet.of(Schema.LogicalType.DATE, Schema.LogicalType.TIMESTAMP_MICROS);

  /**
   * Construct and return the {@link Spanner} service for the provided credentials and projectId
   */
  public static Spanner getSpannerService(String serviceAccountFilePath, String projectId) throws IOException {
    SpannerOptions.Builder optionsBuilder = SpannerOptions.newBuilder();
    if (serviceAccountFilePath != null) {
      optionsBuilder.setCredentials(GCPUtils.loadServiceAccountCredentials(serviceAccountFilePath));
    }
    optionsBuilder.setProjectId(projectId);
    return optionsBuilder.build().getService();
  }

  /**
   * Validate that the schema is a supported one, compatible with Spanner.
   */
  public static void validateSchema(Schema schema) {
    for (Schema.Field field : schema.getFields()) {
      Schema fieldSchema = field.getSchema();
      fieldSchema = fieldSchema.isNullable() ? fieldSchema.getNonNullable() : fieldSchema;

      Schema.LogicalType logicalType = fieldSchema.getLogicalType();
      if (logicalType != null && !SUPPORTED_LOGICAL_TYPES.contains(logicalType)) {
        throw new IllegalArgumentException(String.format("Schema logical type %s not supported by Spanner source",
                                                         logicalType));
      }

      if (logicalType == null) {
        Schema.Type type = fieldSchema.getType();
        if (!SUPPORTED_TYPES.contains(type)) {
          throw new IllegalArgumentException(String.format("Schema type %s not supported by Spanner source", type));
        }
      }
    }
  }
}

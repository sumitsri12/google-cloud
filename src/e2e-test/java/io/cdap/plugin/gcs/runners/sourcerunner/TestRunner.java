/*
 * Copyright © 2021 Cask Data, Inc.
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
package io.cdap.plugin.gcs.runners.sourcerunner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;


/**
 * Test Runner to execute GCS source cases.
 */
@RunWith(Cucumber.class)
@CucumberOptions(
  features = {"src/e2e-test/features"},
  glue = {"io.cdap.plugin.gcs.stepsdesign", "io.cdap.plugin.bigquery.stepsdesign",
    "stepsdesign", "io.cdap.plugin.common.stepsdesign"},
  tags = {"@GCS_Source"},
  monochrome = true,
  plugin = {"pretty", "html:target/cucumber-html-report/gcs-source",
    "json:target/cucumber-reports/cucumber-gcs-source.json",
    "junit:target/cucumber-reports/cucumber-gcs-source.xml"}
)
public class TestRunner {
}

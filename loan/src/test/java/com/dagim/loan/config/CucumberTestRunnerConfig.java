package com.dagim.loan.config;


import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("afts.feature")
//@ConfigurationParameter(key = Constants.FEATURES_PROPERTY_NAME, value = "")
public class CucumberTestRunnerConfig {

}

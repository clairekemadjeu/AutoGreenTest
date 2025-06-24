package com.skysoft.app.bdd;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources",
    glue = {"com.skysoft.app.bdd"},
    plugin = {"pretty", "json:target/cucumber-reports/cucumber.json"})
public class CuccumberTestRunner {}

package com.skysoft.app.bdd;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import lombok.extern.slf4j.Slf4j;
import org.mockito.MockitoAnnotations;

@Slf4j
public class ContextResetHook extends AbstractStepDefinition {
  @Before
  public void beforeScenario() {
    log.info("Spring context initialization for next scenario");
    MockitoAnnotations.openMocks(this);
  }

  @After
  public void afterScenario() {
    log.info("Spring context clean up for next scenario");
    context.clear();
  }
}

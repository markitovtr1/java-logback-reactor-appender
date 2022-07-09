package br.com.crazycrowd.logback.reactor;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;


public class FluxAppenderIntegratedTest {

  private static final Logger testLog = LoggerFactory.getLogger("test-logger");

  @Test
  void works() {
    testLog.debug("hello");
    testLog.info("world");

    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    assertThat(SetAppender.set).containsExactlyInAnyOrder("hello", "world");
  }

}

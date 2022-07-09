package br.com.crazycrowd.logback.reactor;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FluxAppenderUnitTest {
  public interface AppenderLoggingEvent extends Appender<ILoggingEvent> {
  }

  @Test
  void works() {
    final FluxAppender appender = new FluxAppender();
    final AppenderLoggingEvent mockAppender1 = buildMock("appender1");
    final AppenderLoggingEvent mockAppender2 = buildMock("appender2");
    final ILoggingEvent loggingEvent = mock(ILoggingEvent.class);

    appender.addAppender(mockAppender1);
    appender.addAppender(mockAppender2);
    appender.start();

    appender.append(loggingEvent);

    try {
      Thread.sleep(200);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    verify(mockAppender1, times(1)).doAppend(eq(loggingEvent));
    verify(mockAppender2, times(1)).doAppend(eq(loggingEvent));
  }

  private AppenderLoggingEvent buildMock(final String appenderName) {
    final AppenderLoggingEvent mockAppender = mock(AppenderLoggingEvent.class);

    when(mockAppender.getName()).thenReturn(appenderName);
    doNothing().when(mockAppender).doAppend(any(ILoggingEvent.class));

    return mockAppender;
  }

}

package br.com.crazycrowd.logback.reactor;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import java.util.HashSet;
import java.util.Set;

public class SetAppender extends AppenderBase<ILoggingEvent> {

  public static final Set<String> set = new HashSet<>();

  @Override
  public void append(ILoggingEvent eventObject) {
    set.add(eventObject.getFormattedMessage());
  }

}

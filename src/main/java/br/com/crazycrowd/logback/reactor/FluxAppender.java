package br.com.crazycrowd.logback.reactor;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.spi.AppenderAttachable;
import lombok.Getter;
import lombok.Setter;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FluxAppender extends UnsynchronizedAppenderBase<ILoggingEvent> implements AppenderAttachable<ILoggingEvent> {

  @Getter
  @Setter
  private Integer backpressureBufferSize = 50_000;

  private final Map<String, Appender<ILoggingEvent>> appenders = new HashMap<>();

  @Getter
  private Disposable subscription;

  private final Sinks.Many<ILoggingEvent> sink =
      Sinks.unsafe()
          .many()
          .multicast()
          .onBackpressureBuffer(backpressureBufferSize);

  @Override
  public void append(ILoggingEvent eventObject) {
    sink.tryEmitNext(eventObject);
  }

  @Override
  public void start() {
    subscription = sink.asFlux()
        .flatMap(eventObject ->
            Flux.fromIterable(appenders.values())
                .flatMap(appender -> Mono.<Void>fromRunnable(() -> appender.doAppend(eventObject)))
                .subscribeOn(Schedulers.boundedElastic())
                .then()
                .thenReturn(eventObject)
        )
        .onErrorContinue((t, o) -> {
        })
        .subscribe();

    super.start();
  }

  @Override
  public void addAppender(Appender<ILoggingEvent> newAppender) {
    appenders.put(newAppender.getName(), newAppender);
  }

  @Override
  public Iterator<Appender<ILoggingEvent>> iteratorForAppenders() {
    return appenders.values().iterator();
  }

  @Override
  public Appender<ILoggingEvent> getAppender(String name) {
    return appenders.get(name);
  }

  @Override
  public boolean isAttached(Appender<ILoggingEvent> appender) {
    return appenders.containsValue(appender);
  }

  @Override
  public void detachAndStopAllAppenders() {
    appenders.clear();
  }

  @Override
  public boolean detachAppender(Appender<ILoggingEvent> appender) {
    return detachAppender(appender.getName());
  }

  @Override
  public boolean detachAppender(String name) {
    return appenders.remove(name) != null;
  }

}

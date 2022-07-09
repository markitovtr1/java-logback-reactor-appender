# Reactor Logback Appender

FluxAppender to handle other synchronous appenders in Schedulers.boundedElastic().

This implementation does not intend to support logging FIFO behavior. It just emits values as they arrive and handles a
buffer queue on backpressure for logging events. In case queue is full, messages are dropped.

I do not recommend using FluxAppender with multiple subAppenders. If one of those appenders present a performance issue,
I did not create any test to assure correct behavior.

## Why use this library?

You might use logback-classic `AsyncAppender`, but it creates a single thread to run appenders. In this lib, we reuse
Schedulers.boundedElastic, giving more performance theoretically.

## Using this library

### Careful: dependencies are not included in default JAR!

You can see that all dependencies are not included with jar. This is because if you are using this, you probably are
already including logging and reactor libs and I did not want to have any version conflicts or vulnerabilities.

If you have in your class path reactor-core, SLF4j and Logback-classic, you should be good to go.

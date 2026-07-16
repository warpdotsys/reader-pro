package com.htmake.reader

import com.htmake.reader.synth.SyntheticContinuation
import com.htmake.reader.synth.SyntheticFunction0
import com.htmake.reader.synth.SyntheticType

import java.util.Arrays
import mu.KLogger
import mu.KotlinLogging
import org.springframework.boot.SpringApplication

private final val logger: KLogger = KotlinLogging.INSTANCE.logger(SyntheticFunction0.INSTANCE)

public fun main(args: Array<String>) {
   logger.info("Starting application with args: {}", args);
   SpringApplication.run(ReaderApplication::class.java, Arrays.copyOf(args, args.length));
}

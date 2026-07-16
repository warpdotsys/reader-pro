package com.htmake.reader.api

import com.htmake.reader.synth.SyntheticContinuation
import com.htmake.reader.synth.SyntheticFunction0
import com.htmake.reader.synth.SyntheticType

import mu.KLogger
import mu.KotlinLogging

private final val logger: KLogger = KotlinLogging.INSTANCE.logger(SyntheticFunction0.INSTANCE)

@JvmSynthetic
fun `access$getLogger$p`(): KLogger {
   return logger;
}

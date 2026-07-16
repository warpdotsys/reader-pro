package com.htmake.reader.synth

import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.jvm.functions.Function0

/**
 * Placeholders for types Vineflower could not represent from Kotlin metadata / synthetics.
 * Used only for reverse-engineering readability — not for production compilation fidelity.
 *
 * Injected by mass-fix pass against reader-pro-3.2.14.jar decompilation.
 */
open class SyntheticType

/**
 * Stand-in for synthetic ContinuationImpl subclasses generated per suspend function.
 * Real classes are BookController$foo$1 etc. in the JAR.
 */
open class SyntheticContinuation(
    completion: Continuation<Any?>? = null
) : Continuation<Any?> {
    @JvmField var result: Any? = null
    @JvmField var label: Int = 0
    override val context: CoroutineContext
        get() = EmptyCoroutineContext
    override fun resumeWith(result: Result<Any?>) {}
    open fun invokeSuspend(result: Any?): Any? = result
}

/**
 * Stand-in for compiler-generated Function0 singletons (lazy/logger SAM adapters).
 */
object SyntheticFunction0 : Function0<Any?> {
    @JvmField
    val INSTANCE: Function0<Any?> = this
    override fun invoke(): Any? = null
}

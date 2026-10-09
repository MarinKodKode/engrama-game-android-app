package com.manu.kode.engrama.core.logging

import android.util.Log

/** Destino de los logs. La app usa [AndroidLogSink]; los tests instalan uno propio. */
interface LogSink {
    fun w(tag: String, message: String)
    fun e(tag: String, message: String)
}

object AndroidLogSink : LogSink {
    override fun w(tag: String, message: String) {
        Log.w(tag, message)
    }

    override fun e(tag: String, message: String) {
        Log.e(tag, message)
    }
}

object NoOpLogSink : LogSink {
    override fun w(tag: String, message: String) = Unit
    override fun e(tag: String, message: String) = Unit
}

/**
 * Punto único de logging para dominio y datos, sin depender de android.util.Log
 * en los tests JVM.
 */
object AppLog {
    @Volatile
    var sink: LogSink = AndroidLogSink

    fun w(tag: String, message: String) = sink.w(tag, message)
    fun e(tag: String, message: String) = sink.e(tag, message)
}

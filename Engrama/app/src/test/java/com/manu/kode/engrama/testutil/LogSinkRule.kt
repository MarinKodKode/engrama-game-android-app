package com.manu.kode.engrama.testutil

import com.manu.kode.engrama.core.logging.AppLog
import com.manu.kode.engrama.core.logging.LogSink
import com.manu.kode.engrama.core.logging.NoOpLogSink
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/** Instala un sink que registra los logs durante el test (android.util.Log no existe en JVM). */
class LogSinkRule : TestWatcher() {

    data class Entry(val level: Char, val tag: String, val message: String)

    val entries = mutableListOf<Entry>()

    val warnings: List<Entry> get() = entries.filter { it.level == 'W' }
    val errors: List<Entry> get() = entries.filter { it.level == 'E' }

    private val sink = object : LogSink {
        override fun w(tag: String, message: String) {
            entries += Entry('W', tag, message)
        }

        override fun e(tag: String, message: String) {
            entries += Entry('E', tag, message)
        }
    }

    override fun starting(description: Description) {
        entries.clear()
        AppLog.sink = sink
    }

    override fun finished(description: Description) {
        AppLog.sink = NoOpLogSink
    }
}

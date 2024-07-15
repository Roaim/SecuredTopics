package app.securedtopics.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

interface Logger {

    val logTag: String get() = javaClass.simpleName
    val logDisable: Boolean

    fun log(msg: Any?) {
        if (logDisable) return
        val time: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(Date())
        println("$time  |  $logTag")
        if (msg is Throwable?) msg?.printStackTrace() else println(msg)
    }

    fun <T : Any?> T.logIt(): T = apply(::log)

}
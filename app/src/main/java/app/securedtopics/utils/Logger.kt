package app.securedtopics.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

interface Logger {

    val logTag: String get() = javaClass.simpleName
    val logDisable: Boolean

    fun log(msg: Any?, tag: String = logTag) {
        if (logDisable) return
        val time: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(Date())
        println("$time  |  $tag")
        if (msg is Throwable?) msg?.printStackTrace() else println(msg)
    }

    fun <T : Any?> T.logIt(): T = apply(::log)

}
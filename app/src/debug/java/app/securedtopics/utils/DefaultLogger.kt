package app.securedtopics.utils

object DefaultLogger : Logger {
    override val logDisable: Boolean get() = false
}
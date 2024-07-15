package app.securedtopics.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ClipboardService @Inject constructor(
    private val clipboardManager: ClipboardManager
) {

    val clipboardText: String?
        get() = clipboardManager.primaryClip?.takeIf { it.itemCount > 0 }
            ?.getItemAt(0)?.text?.toString()

    fun copyToClipboard(text: String, label: String = "UUID") = clipboardManager.apply {
        setPrimaryClip(ClipData.newPlainText(label, text))
    }

}
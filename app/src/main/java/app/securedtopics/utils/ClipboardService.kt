package app.securedtopics.utils

import android.content.ClipData
import android.content.ClipboardManager
import javax.inject.Inject

interface ClipboardService {
    val clipboardText: String?
    fun copyToClipboard(text: String, label: String = "SecuredTopic")
}

class AndroidClipboardService @Inject constructor(
    private val clipboardManager: ClipboardManager
) : ClipboardService {

    override val clipboardText: String?
        get() = clipboardManager.primaryClip?.takeIf { it.itemCount > 0 }
            ?.getItemAt(0)?.text?.toString()

    override fun copyToClipboard(text: String, label: String) =
        clipboardManager.setPrimaryClip(ClipData.newPlainText(label, text))

}
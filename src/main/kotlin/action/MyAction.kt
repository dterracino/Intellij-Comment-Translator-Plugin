package action

import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.command.WriteCommandAction
import utils.TranslatorWrapper


class MyAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent?) {


        if (TranslatorWrapper.filePath.isNullOrEmpty()) {

            val noti = NotificationGroup("Translator Plugin", NotificationDisplayType.BALLOON, true)
            noti.createNotification(
                "Set Config Path",
                "Set configuration json path in preferrence",
                NotificationType.INFORMATION,
                null
            )
                .notify(e?.project)

        } else {
            val editor = e?.getRequiredData(CommonDataKeys.EDITOR)
            val caretModel = editor?.caretModel
            val selectedText = caretModel?.currentCaret?.selectedText

            val project = e?.project
            val document = editor?.document
            val selectionModel = editor?.selectionModel

            val start = selectionModel?.selectionStart
            val end = selectionModel?.selectionEnd

            WriteCommandAction.runWriteCommandAction(
                project
            ) { document?.replaceString(start!!, end!!, TranslatorWrapper.translate(selectedText)) }

            selectionModel?.removeSelection()
        }


    }

    override fun update(e: AnActionEvent?) {
        val editor = e!!.getRequiredData<Editor>(CommonDataKeys.EDITOR)
        val caretModel = editor.caretModel
        e.presentation.isEnabledAndVisible = caretModel.currentCaret.hasSelection()
    }
}
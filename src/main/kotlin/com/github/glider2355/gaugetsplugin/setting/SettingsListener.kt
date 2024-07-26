package com.github.glider2355.gaugetsplugin.setting

import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.JFileChooser

class SettingsListener(private val settingsComponent: SettingsComponent) {
    init {
        settingsComponent.addButton.addActionListener(AddButtonListener())
        settingsComponent.removeButton.addActionListener(RemoveButtonListener())
    }

    // フォルダ追加
    inner class AddButtonListener : ActionListener {
        override fun actionPerformed(e: ActionEvent?) {
            val fileChooser = JFileChooser()
            fileChooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
            val result = fileChooser.showOpenDialog(settingsComponent.mainPanel)
            if (result == JFileChooser.APPROVE_OPTION) {
                val selectedDir = fileChooser.selectedFile.absolutePath
                settingsComponent.addDirectory(selectedDir)
            }
        }
    }

    // フォルダ削除
    inner class RemoveButtonListener : ActionListener {
        override fun actionPerformed(e: ActionEvent?) {
            settingsComponent.removeSelectedDirectories()
        }
    }
}

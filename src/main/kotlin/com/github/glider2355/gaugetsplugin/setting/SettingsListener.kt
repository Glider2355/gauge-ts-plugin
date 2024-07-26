package com.github.glider2355.gaugetsplugin.setting

import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JFileChooser

class SettingsListener(private val settingsComponent: SettingsComponent) {
    init {
        settingsComponent.addButton.addActionListener(AddButtonListener())
        settingsComponent.removeButton.addActionListener(RemoveButtonListener())
        settingsComponent.directoryList.addMouseListener(CheckboxListener())
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

    // チェックボックスのクリックイベントを処理
    inner class CheckboxListener : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent?) {
            val index = settingsComponent.directoryList.locationToIndex(e?.point)
            if (index >= 0) {
                val item = settingsComponent.directoryListModel.getElementAt(index)
                item.isChecked = !item.isChecked
                settingsComponent.directoryList.repaint(settingsComponent.directoryList.getCellBounds(index, index))
            }
        }
    }
}

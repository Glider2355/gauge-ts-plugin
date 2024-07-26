package com.github.glider2355.gaugetsplugin.setting

import com.intellij.ui.components.JBList
import com.intellij.util.ui.JBUI
import javax.swing.*
import java.awt.BorderLayout
import java.awt.Dimension

/**
 * https://plugins.jetbrains.com/docs/intellij/settings-tutorial.html#creating-the-appsettingscomponent-implementation
 */

class SettingsComponent {
    val mainPanel: JPanel = JPanel(BorderLayout())
    val directoryListModel = DefaultListModel<DirectoryItem>()
    val directoryList = JBList(directoryListModel)
    val addButton = JButton("Add Directory")
    val removeButton = JButton("Remove Selected Directory")

    init {
        // メインパネルのボーダー設定
        mainPanel.border = JBUI.Borders.empty(10)

        // ディレクトリ一覧の作成
        val listPanel = JPanel(BorderLayout())
        listPanel.border = JBUI.Borders.empty(10, 0)
        listPanel.add(JLabel("Gauge Step Directories:"), BorderLayout.NORTH)
        directoryList.cellRenderer = DirectoryItemRenderer()
        directoryList.visibleRowCount = 10
        listPanel.add(JScrollPane(directoryList), BorderLayout.CENTER)

        // ディレクトリの追加・削除ボタンを作成
        val buttonPanel = JPanel()
        buttonPanel.layout = BoxLayout(buttonPanel, BoxLayout.Y_AXIS)
        buttonPanel.border = JBUI.Borders.empty(10, 0)
        addButton.maximumSize = Dimension(Int.MAX_VALUE, addButton.preferredSize.height)
        removeButton.maximumSize = Dimension(Int.MAX_VALUE, removeButton.preferredSize.height)
        buttonPanel.add(addButton)
        buttonPanel.add(Box.createRigidArea(Dimension(0, 5)))
        buttonPanel.add(removeButton)

        // メインパネルにディレクトリ一覧とボタンを追加
        mainPanel.add(listPanel, BorderLayout.CENTER)
        mainPanel.add(buttonPanel, BorderLayout.SOUTH)
    }

    // ディレクトリ一覧を取得
    fun getDirectories(): List<String> {
        return directoryListModel.elements().toList().filter { it.isChecked }.map { it.path }
    }

    // ディレクトリ一覧を設定
    fun setDirectories(directories: List<String>) {
        directoryListModel.clear()
        directories.forEach { directoryListModel.addElement(DirectoryItem(it, true)) }
    }

    // ディレクトリを追加
    fun addDirectory(directory: String) {
        if (!directoryListModel.elements().toList().any { it.path == directory }) {
            directoryListModel.addElement(DirectoryItem(directory, true))
        }
    }

    // 選択されたディレクトリを削除
    fun removeSelectedDirectories() {
        val selectedIndices = directoryList.selectedIndices
        for (i in selectedIndices.reversed()) {
            directoryListModel.remove(i)
        }
    }
}

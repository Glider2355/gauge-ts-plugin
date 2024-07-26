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
    private val directoryListModel = DefaultListModel<String>()
    private val directoryList = JBList(directoryListModel)
    val addButton = JButton("Add Directory")
    val removeButton = JButton("Remove Selected Directory")

    init {
        mainPanel.border = JBUI.Borders.empty(10)

        val listPanel = JPanel(BorderLayout())
        listPanel.border = JBUI.Borders.empty(10, 0)
        listPanel.add(JLabel("Gauge Step Directories:"), BorderLayout.NORTH)
        directoryList.visibleRowCount = 10
        listPanel.add(JScrollPane(directoryList), BorderLayout.CENTER)

        val buttonPanel = JPanel()
        buttonPanel.layout = BoxLayout(buttonPanel, BoxLayout.Y_AXIS)
        buttonPanel.border = JBUI.Borders.empty(10, 0)
        addButton.maximumSize = Dimension(Int.MAX_VALUE, addButton.preferredSize.height)
        removeButton.maximumSize = Dimension(Int.MAX_VALUE, removeButton.preferredSize.height)
        buttonPanel.add(addButton)
        buttonPanel.add(Box.createRigidArea(Dimension(0, 5)))
        buttonPanel.add(removeButton)

        mainPanel.add(listPanel, BorderLayout.CENTER)
        mainPanel.add(buttonPanel, BorderLayout.SOUTH)
    }

    fun getDirectories(): List<String> {
        return directoryListModel.elements().toList()
    }

    fun setDirectories(directories: List<String>) {
        directoryListModel.clear()
        directories.forEach { directoryListModel.addElement(it) }
    }

    fun addDirectory(directory: String) {
        if (!directoryListModel.contains(directory)) {
            directoryListModel.addElement(directory)
        }
    }

    fun removeSelectedDirectories() {
        val selectedIndices = directoryList.selectedIndices
        for (i in selectedIndices.reversed()) {
            directoryListModel.remove(i)
        }
    }
}

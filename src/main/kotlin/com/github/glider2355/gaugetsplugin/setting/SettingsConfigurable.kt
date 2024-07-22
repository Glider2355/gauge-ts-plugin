package com.github.glider2355.gaugetsplugin.setting

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.components.service
import com.intellij.ui.components.JBList
import com.intellij.util.ui.JBUI
import javax.swing.*
import java.awt.BorderLayout
import java.awt.Dimension

class SettingsConfigurable(private val project: Project) : Configurable {
    private var mySettingsComponent: JPanel? = null
    private val directoryListModel = DefaultListModel<String>()
    private val directoryList = JBList(directoryListModel)
    private val addButton = JButton("Add Directory")
    private val removeButton = JButton("Remove Selected Directory")

    override fun createComponent(): JComponent? {
        if (mySettingsComponent == null) {
            mySettingsComponent = JPanel(BorderLayout())
            mySettingsComponent!!.border = JBUI.Borders.empty(10)

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

            mySettingsComponent!!.add(listPanel, BorderLayout.CENTER)
            mySettingsComponent!!.add(buttonPanel, BorderLayout.SOUTH)

            // ボタンのアクションリスナーを追加
            addButton.addActionListener {
                val fileChooser = JFileChooser()
                fileChooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
                val result = fileChooser.showOpenDialog(mySettingsComponent)
                if (result == JFileChooser.APPROVE_OPTION) {
                    val selectedDir = fileChooser.selectedFile.absolutePath
                    if (!directoryListModel.contains(selectedDir)) {
                        directoryListModel.addElement(selectedDir)
                    }
                }
            }

            removeButton.addActionListener {
                val selectedIndices = directoryList.selectedIndices
                for (i in selectedIndices.reversed()) {
                    directoryListModel.remove(i)
                }
            }
        }
        return mySettingsComponent
    }

    override fun isModified(): Boolean {
        val settings = project.service<PluginSettings>()
        return directoryListModel.elements().toList() != settings.searchDirectories
    }

    override fun apply() {
        val settings = project.service<PluginSettings>()
        settings.searchDirectories = directoryListModel.elements().toList().toMutableList()
    }

    override fun getDisplayName(): String {
        return "Gauge Ts Plugin Settings"
    }

    override fun reset() {
        val settings = project.service<PluginSettings>()
        directoryListModel.clear()
        settings.searchDirectories.forEach { directoryListModel.addElement(it) }
    }
}

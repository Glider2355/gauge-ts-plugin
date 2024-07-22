package com.github.glider2355.gaugetsplugin.setting

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.components.service
import javax.swing.*

class SettingsConfigurable(private val project: Project) : Configurable {
    private var mySettingsComponent: JPanel? = null
    private val directoryListModel = DefaultListModel<String>()
    private val directoryList = JList(directoryListModel)
    private val addButton = JButton("Add Directory")
    private val removeButton = JButton("Remove Selected Directory")

    override fun createComponent(): JComponent? {
        if (mySettingsComponent == null) {
            mySettingsComponent = JPanel()
            mySettingsComponent!!.layout = BoxLayout(mySettingsComponent, BoxLayout.Y_AXIS)
            mySettingsComponent!!.add(JLabel("Search Directories:"))
            mySettingsComponent!!.add(JScrollPane(directoryList))
            mySettingsComponent!!.add(addButton)
            mySettingsComponent!!.add(removeButton)

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
        return "My Plugin Settings"
    }

    override fun reset() {
        val settings = project.service<PluginSettings>()
        directoryListModel.clear()
        settings.searchDirectories.forEach { directoryListModel.addElement(it) }
    }
}
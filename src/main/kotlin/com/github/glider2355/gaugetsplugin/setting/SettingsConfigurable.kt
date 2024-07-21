package com.github.glider2355.gaugetsplugin.setting

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.components.service
import javax.swing.*

class SettingsConfigurable(private val project: Project) : Configurable {
    private var mySettingsComponent: JPanel? = null
    private val directoryTextField = JTextField(20)
    private val selectDirectoryButton = JButton("Select Directory")

    override fun createComponent(): JComponent? {
        if (mySettingsComponent == null) {
            mySettingsComponent = JPanel()
            mySettingsComponent!!.layout = BoxLayout(mySettingsComponent, BoxLayout.Y_AXIS)
            mySettingsComponent!!.add(JLabel("Search Directory:"))
            mySettingsComponent!!.add(directoryTextField)
            mySettingsComponent!!.add(selectDirectoryButton)

            // ボタンのアクションリスナーを追加
            selectDirectoryButton.addActionListener {
                val fileChooser = JFileChooser()
                fileChooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
                val result = fileChooser.showOpenDialog(mySettingsComponent)
                if (result == JFileChooser.APPROVE_OPTION) {
                    directoryTextField.text = fileChooser.selectedFile.absolutePath
                }
            }
        }
        return mySettingsComponent
    }

    override fun isModified(): Boolean {
        val settings = project.service<PluginSettings>()
        return directoryTextField.text != settings.searchDirectory
    }

    override fun apply() {
        val settings = project.service<PluginSettings>()
        settings.searchDirectory = directoryTextField.text
    }

    override fun getDisplayName(): String {
        return "My Plugin Settings"
    }

    override fun reset() {
        val settings = project.service<PluginSettings>()
        directoryTextField.text = settings.searchDirectory
    }
}

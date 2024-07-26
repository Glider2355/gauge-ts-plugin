package com.github.glider2355.gaugetsplugin.setting

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.components.service
import javax.swing.*

class SettingsConfigurable(private val project: Project) : Configurable {
    private var mySettingsComponent: SettingsComponent? = null

    override fun createComponent(): JComponent? {
        if (mySettingsComponent == null) {
            mySettingsComponent = SettingsComponent()

            // ボタンのアクションリスナーを追加
            mySettingsComponent!!.addButton.addActionListener {
                val fileChooser = JFileChooser()
                fileChooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
                val result = fileChooser.showOpenDialog(mySettingsComponent!!.mainPanel)
                if (result == JFileChooser.APPROVE_OPTION) {
                    val selectedDir = fileChooser.selectedFile.absolutePath
                    mySettingsComponent!!.addDirectory(selectedDir)
                }
            }

            mySettingsComponent!!.removeButton.addActionListener {
                mySettingsComponent!!.removeSelectedDirectories()
            }
        }
        return mySettingsComponent?.mainPanel
    }

    override fun isModified(): Boolean {
        val settings = project.service<PluginSettings>()
        return mySettingsComponent?.getDirectories() != settings.searchDirectories
    }

    // 設定を保存したときに選択されたディレクトリを設定に保存
    override fun apply() {
        val settings = project.service<PluginSettings>()
        settings.searchDirectories = mySettingsComponent?.getDirectories()?.toMutableList() ?: mutableListOf()
    }

    override fun getDisplayName(): String {
        return "Gauge Ts Plugin Settings"
    }

    // リセットの際にディレクトリ一覧をリセット
    override fun reset() {
        val settings = project.service<PluginSettings>()
        mySettingsComponent?.setDirectories(settings.searchDirectories)
    }
}
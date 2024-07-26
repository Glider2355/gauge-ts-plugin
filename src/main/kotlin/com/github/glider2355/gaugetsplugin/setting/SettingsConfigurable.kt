package com.github.glider2355.gaugetsplugin.setting

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.components.service
import javax.swing.*

class SettingsConfigurable(private val project: Project) : Configurable {
    private var mySettingsComponent: SettingsComponent? = null

    // 設定画面のコンポーネントを返す
    override fun createComponent(): JComponent? {
        if (mySettingsComponent == null) {
            mySettingsComponent = SettingsComponent()
            SettingsListener(mySettingsComponent!!)
        }
        return mySettingsComponent?.mainPanel
    }

    // 設定が変更されたかどうかを返す
    override fun isModified(): Boolean {
        val settings = project.service<PluginSettings>()
        val currentDirectories = mySettingsComponent?.getDirectories()?.map { it.path } ?: emptyList()
        return currentDirectories != settings.searchDirectories ||
                mySettingsComponent?.getDirectories()?.any { it.path in settings.searchDirectories && it.isChecked != (it.path in settings.validDirectories) } == true
    }

    // 設定を保存したときに選択されたディレクトリを設定に保存
    override fun apply() {
        val settings = project.service<PluginSettings>()
        settings.searchDirectories = mySettingsComponent?.getDirectories()?.map { it.path }?.toMutableList() ?: mutableListOf()
        settings.validDirectories = mySettingsComponent?.getDirectories()?.filter { it.isChecked }?.map { it.path }?.toMutableList() ?: mutableListOf()
    }

    // 設定画面のタイトル
    override fun getDisplayName(): String {
        return "Gauge Ts Plugin Settings"
    }

    // リセットの際にディレクトリ一覧をリセット
    override fun reset() {
        val settings = project.service<PluginSettings>()
        val directoryItems = settings.searchDirectories.map { DirectoryItem(it, it in settings.validDirectories) }
        mySettingsComponent?.setDirectories(directoryItems)
    }
}
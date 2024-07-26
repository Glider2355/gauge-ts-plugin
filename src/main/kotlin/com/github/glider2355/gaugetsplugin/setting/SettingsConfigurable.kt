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
        return mySettingsComponent?.getDirectories() != settings.searchDirectories
    }

    // 設定を保存したときに選択されたディレクトリを設定に保存
    override fun apply() {
        val settings = project.service<PluginSettings>()
        settings.searchDirectories = mySettingsComponent?.getDirectories()?.toMutableList() ?: mutableListOf()
    }

    // 設定画面のタイトル
    override fun getDisplayName(): String {
        return "Gauge Ts Plugin Settings"
    }

    // リセットの際にディレクトリ一覧をリセット
    override fun reset() {
        val settings = project.service<PluginSettings>()
        mySettingsComponent?.setDirectories(settings.searchDirectories)
    }
}
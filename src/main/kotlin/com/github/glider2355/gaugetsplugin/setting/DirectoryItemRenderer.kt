package com.github.glider2355.gaugetsplugin.setting

import java.awt.Component
import javax.swing.JCheckBox
import javax.swing.JList
import javax.swing.ListCellRenderer

class DirectoryItemRenderer : JCheckBox(), ListCellRenderer<DirectoryItem> {
    override fun getListCellRendererComponent(
        list: JList<out DirectoryItem>?,
        value: DirectoryItem?,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        this.text = value?.path
        this.isSelected = isSelected
        this.isFocusPainted = cellHasFocus
        this.isOpaque = true
        this.background = if (isSelected) list?.selectionBackground else list?.background
        this.isChecked = value?.isChecked == true
        return this
    }

    private var isChecked: Boolean
        get() = isSelected
        set(value) {
            isSelected = value
        }
}

package com.github.glider2355.gaugetsplugin.contributor

import com.intellij.psi.tree.IElementType
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.NotNull

class SpecTokenType(@NotNull @NonNls debugName: String) :
    IElementType(debugName, SpecLanguage.INSTANCE) {
    override fun toString(): String {
        return "SpecTokenType." + super.toString()
    }
}

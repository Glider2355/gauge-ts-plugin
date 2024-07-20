package com.github.glider2355.gaugetsplugin.language.psi

import com.github.glider2355.gaugetsplugin.contributor.SpecTokenType
import com.github.glider2355.gaugetsplugin.language.psi.impl.SpecPropertyImpl
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType


interface SpecTypes {
    companion object {
        val PROPERTY: IElementType = SpecElementType("PROPERTY")
        val COMMENT: IElementType = SpecTokenType("COMMENT")
        val CRLF: IElementType = SpecTokenType("CRLF")
        val KEY: IElementType = SpecTokenType("KEY")
        val SEPARATOR: IElementType = SpecTokenType("SEPARATOR")
        val VALUE: IElementType = SpecTokenType("VALUE")
    }

    object Factory {
        fun createElement(node: ASTNode): PsiElement {
            val type = node.elementType
            return when (type) {
                PROPERTY -> SpecPropertyImpl(node)
                else -> throw AssertionError("Unknown element type: $type")
            }
        }
    }
}
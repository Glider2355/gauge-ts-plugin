package com.github.glider2355.gaugetsplugin.language.psi.impl

import com.github.glider2355.gaugetsplugin.language.psi.SpecNamedElement
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode

abstract class SpecNamedElementImpl(node: ASTNode) : ASTWrapperPsiElement(node), SpecNamedElement {
    init {
        requireNotNull(node) { "ASTNode cannot be null" }
    }
}
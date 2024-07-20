package com.github.glider2355.gaugetsplugin.language.psi.impl

import com.github.glider2355.gaugetsplugin.language.psi.SpecProperty
import com.github.glider2355.gaugetsplugin.language.psi.SpecVisitor
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.annotations.NotNull

class SpecPropertyImpl(node: ASTNode) : SpecNamedElementImpl(node), SpecProperty {

    private fun accept(visitor: SpecVisitor<*, *>) {
        visitor.visitProperty(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is SpecVisitor<*, *>) {
            accept(visitor)
        } else {
            super.accept(visitor)
        }
    }

    override fun getKey(): String? {
        return SpecPsiImplUtil.getKey(this)
    }

    override fun getValue(): String? {
        return SpecPsiImplUtil.getValue(this)
    }

    override fun getName(): String? {
        return SpecPsiImplUtil.getName(this)
    }

    override fun setName(newName: String): PsiElement {
        return SpecPsiImplUtil.setName(this, newName)
    }

    override fun getNameIdentifier(): PsiElement? {
        return SpecPsiImplUtil.getNameIdentifier(this)
    }

    override fun getPresentation(): ItemPresentation? {
        return SpecPsiImplUtil.getPresentation(this)
    }
}
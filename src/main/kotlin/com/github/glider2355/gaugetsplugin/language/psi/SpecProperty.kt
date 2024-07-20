package com.github.glider2355.gaugetsplugin.language.psi

import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement

interface SpecProperty : SpecNamedElement {

    fun getKey(): String?

    fun getValue(): String?

    override fun getName(): String?

    override fun setName(newName: String): PsiElement

    override fun getNameIdentifier(): PsiElement?

    fun getPresentation(): ItemPresentation?
}
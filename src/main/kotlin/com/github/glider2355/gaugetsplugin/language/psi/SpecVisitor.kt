package com.github.glider2355.gaugetsplugin.language.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor

class SpecVisitor<T, U> : PsiElementVisitor() {

    fun visitProperty(o: SpecProperty) {
        visitNamedElement(o)
    }

    fun visitNamedElement(o: SpecNamedElement) {
        visitPsiElement(o)
    }

    fun visitPsiElement(o: PsiElement) {
        visitElement(o)
    }
}
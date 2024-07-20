package com.github.glider2355.gaugetsplugin.contributor

import com.github.glider2355.gaugetsplugin.language.psi.SpecTypes
import com.intellij.codeInsight.completion.*
import com.intellij.patterns.PlatformPatterns

class SpecCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(SpecTypes.VALUE).withLanguage(SpecLanguage.INSTANCE),
            SpecCompletionProvider()
        )
    }
}
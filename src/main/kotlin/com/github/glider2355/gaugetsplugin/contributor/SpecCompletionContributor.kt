package com.github.glider2355.gaugetsplugin.contributor

import com.github.glider2355.gaugetsplugin.language.SpecLanguage
import com.intellij.codeInsight.completion.*
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.StandardPatterns

class SpecCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement().withLanguage(SpecLanguage.INSTANCE),
            SpecCompletionProvider()
        )
    }
}
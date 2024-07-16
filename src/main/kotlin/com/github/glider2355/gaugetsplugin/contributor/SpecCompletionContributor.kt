package com.github.glider2355.gaugetsplugin.contributor

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PlainTextTokenTypes
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext

internal class SpecCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(PlainTextTokenTypes.PLAIN_TEXT),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    resultSet: CompletionResultSet
                ) {
                    resultSet.addElement(LookupElementBuilder.create("Hello"))
                }
            }
        )
    }
}
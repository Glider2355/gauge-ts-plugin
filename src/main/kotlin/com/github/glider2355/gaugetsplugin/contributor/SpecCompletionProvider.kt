package com.github.glider2355.gaugetsplugin.contributor

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext

class SpecCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        result.addElement(LookupElementBuilder.create("specOption1"))
        result.addElement(LookupElementBuilder.create("specOption2"))
        result.addElement(LookupElementBuilder.create("specOption3"))
    }
}
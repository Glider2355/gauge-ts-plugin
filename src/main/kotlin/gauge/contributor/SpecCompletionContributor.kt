package gauge.contributor

import com.intellij.codeInsight.completion.*
import com.intellij.patterns.PlatformPatterns
import gauge.language.Specification

class SpecCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement().withLanguage(Specification.INSTANCE),
            SpecCompletionProvider()
        )
    }
}
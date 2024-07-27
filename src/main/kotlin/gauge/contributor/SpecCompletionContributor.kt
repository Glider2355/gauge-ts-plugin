package gauge.contributor

import gauge.language.SpecLanguage
import com.intellij.codeInsight.completion.*
import com.intellij.patterns.PlatformPatterns

class SpecCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement().withLanguage(SpecLanguage.INSTANCE),
            SpecCompletionProvider()
        )
    }
}
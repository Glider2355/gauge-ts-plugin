package gauge.contributor

import com.intellij.codeInsight.completion.*
import com.intellij.patterns.PlatformPatterns
import gauge.language.Specification
import gauge.language.token.SpecTokenTypes

class SpecCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(SpecTokenTypes.STEP).withLanguage(Specification.INSTANCE),
            SpecCompletionProvider()
        )
    }
}
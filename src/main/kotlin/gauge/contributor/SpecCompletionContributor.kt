package gauge.contributor

import com.intellij.codeInsight.completion.*
import com.intellij.patterns.PlatformPatterns
import gauge.language.Specification
import gauge.language.token.SpecTokenTypes
import gauge.setting.PluginSettings
import com.intellij.openapi.project.Project
import com.intellij.openapi.components.service

class SpecCompletionContributor : CompletionContributor() {
    private var searchDirectories: List<String> = emptyList()

    init {
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(SpecTokenTypes.STEP).withLanguage(Specification.INSTANCE),
            SpecCompletionProvider{ searchDirectories }
        )
    }

    override fun beforeCompletion(context: CompletionInitializationContext) {
        super.beforeCompletion(context)
        val project: Project = context.project
        val settings = project.service<PluginSettings>()
        searchDirectories = settings.validDirectories
    }
}
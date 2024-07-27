package gauge.language

import com.intellij.lang.Language

class SpecLanguage private constructor() : Language("Spec") {
    companion object {
        val INSTANCE = SpecLanguage()
    }
}
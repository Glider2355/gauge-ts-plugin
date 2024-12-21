package gauge.language

import com.intellij.lang.Language

class Specification private constructor() : Language("Specification") {
    companion object {
        val INSTANCE: Specification = Specification()
    }
}

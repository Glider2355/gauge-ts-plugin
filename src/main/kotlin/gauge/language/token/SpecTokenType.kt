package gauge.language.token

import com.intellij.psi.tree.IElementType
import gauge.language.Specification

class SpecTokenType(debugName: String) : IElementType(debugName, Specification.INSTANCE) {

    override fun toString(): String {
        return "Specification.${super.toString()}"
    }
}

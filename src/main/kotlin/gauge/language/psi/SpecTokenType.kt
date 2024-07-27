package gauge.language.psi

import gauge.language.SpecLanguage
import com.intellij.psi.tree.IElementType
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.NotNull

class SpecTokenType(@NotNull @NonNls debugName: String) :
    IElementType(debugName, SpecLanguage.INSTANCE) {
    override fun toString(): String {
        return "SpecTokenType." + super.toString()
    }
}

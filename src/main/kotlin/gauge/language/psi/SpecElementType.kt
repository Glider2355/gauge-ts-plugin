package gauge.language.psi

import gauge.language.SpecLanguage
import com.intellij.psi.tree.IElementType
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.NotNull

class SpecElementType(@NotNull @NonNls debugName: String) :
    IElementType(debugName, SpecLanguage.INSTANCE)
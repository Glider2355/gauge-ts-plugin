package gauge.language.token

import com.intellij.psi.tree.IElementType
import gauge.language.Specification

class SpecElementType(debugName: String) : IElementType(debugName, Specification.INSTANCE)

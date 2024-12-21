package gauge.language.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import gauge.language.psi.SpecNamedElement

abstract class SpecNamedElementImpl(node: ASTNode) : ASTWrapperPsiElement(node), SpecNamedElement
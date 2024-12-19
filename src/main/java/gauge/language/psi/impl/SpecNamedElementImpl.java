package gauge.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import gauge.language.psi.SpecNamedElement;
import org.jetbrains.annotations.NotNull;

public abstract class SpecNamedElementImpl extends ASTWrapperPsiElement implements SpecNamedElement {
  public SpecNamedElementImpl(@NotNull ASTNode node) {
    super(node);
  }
}

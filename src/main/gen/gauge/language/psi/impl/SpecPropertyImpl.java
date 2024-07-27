// This is a generated file. Not intended for manual editing.
package gauge.language.psi.impl;

import java.util.List;

import gauge.language.psi.SpecProperty;
import gauge.language.psi.SpecVisitor;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static gauge.language.psi.SpecTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import gauge.language.psi.*;

public class SpecPropertyImpl extends ASTWrapperPsiElement implements SpecProperty {

  public SpecPropertyImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SpecVisitor visitor) {
    visitor.visitProperty(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SpecVisitor) accept((SpecVisitor)visitor);
    else super.accept(visitor);
  }

}

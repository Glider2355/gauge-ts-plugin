// This is a generated file. Not intended for manual editing.
package gauge.language.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static gauge.language.token.SpecTokenTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import gauge.language.psi.*;

public class SpecStepImpl extends ASTWrapperPsiElement implements SpecStep {

  public SpecStepImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SpecVisitor visitor) {
    visitor.visitStep(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SpecVisitor) accept((SpecVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public SpecTable getTable() {
    return findChildByClass(SpecTable.class);
  }

}

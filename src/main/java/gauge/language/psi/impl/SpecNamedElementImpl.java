package gauge.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import gauge.language.psi.SpecNamedElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SpecNamedElementImpl extends ASTWrapperPsiElement implements SpecNamedElement {
  public SpecNamedElementImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Nullable
  @Override
  public PsiElement getNameIdentifier() {
    // 名前を示す要素を取得するロジックを実装してください
    // 例: return findChildByType(SPEC_IDENTIFIER);
    return null;
  }

  @Override
  public PsiElement setName(@NotNull String name) {
    PsiElement nameIdentifier = getNameIdentifier();
    if (nameIdentifier != null) {
      // PSIツリーの名前部分を新しい名前で置き換え
      PsiElement newNameIdentifier = PsiElementFactory.getInstance(getProject())
              .createIdentifier(name);
      nameIdentifier.replace(newNameIdentifier);
    }
    return this;
  }
}

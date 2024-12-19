// This is a generated file. Not intended for manual editing.
package gauge.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import gauge.language.psi.SpecKeyword;
import gauge.language.psi.SpecVisitor;
import org.jetbrains.annotations.NotNull;

public class SpecKeywordImpl extends ASTWrapperPsiElement implements SpecKeyword {

    public SpecKeywordImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof SpecVisitor) ((SpecVisitor) visitor).visitKeyword(this);
        else super.accept(visitor);
    }

}

// This is a generated file. Not intended for manual editing.
package gauge.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import gauge.language.psi.SpecTags;
import gauge.language.psi.SpecVisitor;
import org.jetbrains.annotations.NotNull;

public class SpecTagsImpl extends ASTWrapperPsiElement implements SpecTags {

    public SpecTagsImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof SpecVisitor) ((SpecVisitor) visitor).visitTags(this);
        else super.accept(visitor);
    }

}

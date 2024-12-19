// This is a generated file. Not intended for manual editing.
package gauge.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import gauge.language.psi.ConceptArg;
import gauge.language.psi.ConceptDynamicArg;
import gauge.language.psi.ConceptStaticArg;
import gauge.language.psi.ConceptVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConceptArgImpl extends ASTWrapperPsiElement implements ConceptArg {

    public ConceptArgImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof ConceptVisitor) ((ConceptVisitor) visitor).visitArg(this);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public ConceptDynamicArg getDynamicArg() {
        return findChildByClass(ConceptDynamicArg.class);
    }

    @Override
    @Nullable
    public ConceptStaticArg getStaticArg() {
        return findChildByClass(ConceptStaticArg.class);
    }

}

// This is a generated file. Not intended for manual editing.
package gauge.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import gauge.language.psi.ConceptDynamicArg;
import gauge.language.psi.ConceptVisitor;
import org.jetbrains.annotations.NotNull;

public class ConceptDynamicArgImpl extends ASTWrapperPsiElement implements ConceptDynamicArg {

    public ConceptDynamicArgImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof ConceptVisitor) ((ConceptVisitor) visitor).visitDynamicArg(this);
        else super.accept(visitor);
    }

}

// This is a generated file. Not intended for manual editing.
package gauge.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import gauge.language.psi.ConceptStaticArg;
import gauge.language.psi.ConceptVisitor;
import org.jetbrains.annotations.NotNull;

public class ConceptStaticArgImpl extends ASTWrapperPsiElement implements ConceptStaticArg {

    public ConceptStaticArgImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof ConceptVisitor) ((ConceptVisitor) visitor).visitStaticArg(this);
        else super.accept(visitor);
    }

}

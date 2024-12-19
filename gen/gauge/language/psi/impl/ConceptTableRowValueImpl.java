// This is a generated file. Not intended for manual editing.
package gauge.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import gauge.language.psi.ConceptTableRowValue;
import gauge.language.psi.ConceptVisitor;
import org.jetbrains.annotations.NotNull;

public class ConceptTableRowValueImpl extends ASTWrapperPsiElement implements ConceptTableRowValue {

    public ConceptTableRowValueImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof ConceptVisitor) ((ConceptVisitor) visitor).visitTableRowValue(this);
        else super.accept(visitor);
    }

}

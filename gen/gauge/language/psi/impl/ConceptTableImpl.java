// This is a generated file. Not intended for manual editing.
package gauge.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import gauge.language.psi.ConceptTable;
import gauge.language.psi.ConceptTableBody;
import gauge.language.psi.ConceptTableHeader;
import gauge.language.psi.ConceptVisitor;
import org.jetbrains.annotations.NotNull;

public class ConceptTableImpl extends ASTWrapperPsiElement implements ConceptTable {

    public ConceptTableImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof ConceptVisitor) ((ConceptVisitor) visitor).visitTable(this);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public ConceptTableBody getTableBody() {
        return findNotNullChildByClass(ConceptTableBody.class);
    }

    @Override
    @NotNull
    public ConceptTableHeader getTableHeader() {
        return findNotNullChildByClass(ConceptTableHeader.class);
    }

}

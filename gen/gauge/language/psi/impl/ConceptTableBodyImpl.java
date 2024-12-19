// This is a generated file. Not intended for manual editing.
package gauge.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import gauge.language.psi.ConceptTableBody;
import gauge.language.psi.ConceptTableRowValue;
import gauge.language.psi.ConceptVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ConceptTableBodyImpl extends ASTWrapperPsiElement implements ConceptTableBody {

    public ConceptTableBodyImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof ConceptVisitor) ((ConceptVisitor) visitor).visitTableBody(this);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public List<ConceptTableRowValue> getTableRowValueList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, ConceptTableRowValue.class);
    }

}

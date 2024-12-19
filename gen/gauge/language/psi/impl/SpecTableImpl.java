// This is a generated file. Not intended for manual editing.
package gauge.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import gauge.language.psi.SpecTable;
import gauge.language.psi.SpecTableBody;
import gauge.language.psi.SpecTableHeader;
import gauge.language.psi.SpecVisitor;
import org.jetbrains.annotations.NotNull;

public class SpecTableImpl extends ASTWrapperPsiElement implements SpecTable {

    public SpecTableImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof SpecVisitor) ((SpecVisitor) visitor).visitTable(this);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public SpecTableBody getTableBody() {
        return findNotNullChildByClass(SpecTableBody.class);
    }

    @Override
    @NotNull
    public SpecTableHeader getTableHeader() {
        return findNotNullChildByClass(SpecTableHeader.class);
    }

}

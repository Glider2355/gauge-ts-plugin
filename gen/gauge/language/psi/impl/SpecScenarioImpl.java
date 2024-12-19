// This is a generated file. Not intended for manual editing.
package gauge.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import gauge.language.psi.SpecScenario;
import gauge.language.psi.SpecStep;
import gauge.language.psi.SpecTags;
import gauge.language.psi.SpecVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpecScenarioImpl extends ASTWrapperPsiElement implements SpecScenario {

    public SpecScenarioImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof SpecVisitor) ((SpecVisitor) visitor).visitScenario(this);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public List<SpecStep> getStepList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, SpecStep.class);
    }

    @Override
    @Nullable
    public SpecTags getTags() {
        return findChildByClass(SpecTags.class);
    }

}

// This is a generated file. Not intended for manual editing.
package gauge.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import gauge.language.psi.SpecStaticArg;
import gauge.language.token.SpecTokenTypes;

public class SpecStaticArgImpl extends ASTWrapperPsiElement implements SpecStaticArg {

    public SpecStaticArgImpl(ASTNode node) {
        super(node);
    }

    @Override
    public String getText() {
        PsiElement arg = findChildByType(SpecTokenTypes.ARG);
        return arg != null ? arg.getText() : null;
    }
}

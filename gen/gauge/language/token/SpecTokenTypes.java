// This is a generated file. Not intended for manual editing.
package gauge.language.token;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import gauge.language.psi.impl.*;

public interface SpecTokenTypes {

  IElementType SCENARIO = new SpecElementType("SCENARIO");
  IElementType STEP = new SpecElementType("STEP");
  IElementType TABLE = new SpecElementType("TABLE");

  IElementType COMMENT = new SpecTokenType("COMMENT");
  IElementType SCENARIO_HEADING = new SpecTokenType("SCENARIO_HEADING");
  IElementType SPEC_HEADING = new SpecTokenType("SPEC_HEADING");
  IElementType STEP = new SpecTokenType("STEP");
  IElementType TABLE_HEADER = new SpecTokenType("TABLE_HEADER");
  IElementType TABLE_ROW = new SpecTokenType("TABLE_ROW");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == SCENARIO) {
        return new SpecScenarioImpl(node);
      }
      else if (type == STEP) {
        return new SpecStepImpl(node);
      }
      else if (type == TABLE) {
        return new SpecTableImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}

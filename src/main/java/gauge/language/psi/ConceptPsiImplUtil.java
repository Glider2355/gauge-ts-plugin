package gauge.language.psi;

import com.intellij.lang.ASTNode;
import com.thoughtworks.gauge.StepValue;
import gauge.language.psi.impl.ConceptConceptImpl;

import static com.thoughtworks.gauge.language.psi.SpecPsiImplUtil.getStepValueFor;

public final class ConceptPsiImplUtil {

  private ConceptPsiImplUtil() {
  }

  public static StepValue getStepValue(ConceptStep element) {
    ASTNode step = element.getNode();
    String stepText = step.getText().trim();
    int newLineIndex = stepText.indexOf("\n");
    int endIndex = newLineIndex == -1 ? stepText.length() : newLineIndex;
    ConceptTable inlineTable = element.getTable();
    int index = 0;
    if (stepText.trim().charAt(0) == '#') {
      index = 1;
    }
    stepText = stepText.substring(index, endIndex).trim();
    return getStepValueFor(element, stepText, inlineTable != null);
  }

  public static StepValue getStepValue(ConceptConceptImpl conceptConcept) {
    String conceptHeadingText = conceptConcept.getConceptHeading().getText();
    conceptHeadingText = conceptHeadingText.trim().split("\n")[0];
    String text = conceptHeadingText.trim().replaceFirst("#", "");
    return getStepValueFor(conceptConcept, text, false);
  }
}

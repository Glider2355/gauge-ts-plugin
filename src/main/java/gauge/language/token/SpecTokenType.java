package gauge.language.token;

import com.intellij.psi.tree.IElementType;
import gauge.language.Specification;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public final class SpecTokenType extends IElementType {
  public SpecTokenType(@NotNull @NonNls String debugName) {
    super(debugName, Specification.INSTANCE);
  }

  @Override
  public String toString() {
    return "Specification." + super.toString();
  }
}

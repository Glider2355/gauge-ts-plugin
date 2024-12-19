package gauge.language;

import com.intellij.lang.Language;

public final class Specification extends Language {
  public static final Specification INSTANCE = new Specification();

  private Specification() {
    super("Specification");
  }
}

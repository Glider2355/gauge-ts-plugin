// This is a generated file. Not intended for manual editing.
package gauge.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static gauge.language.token.SpecTokenTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class SpecParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return specFile(b, l + 1);
  }

  /* ********************************************************** */
  // COMMENT
  static boolean comment(PsiBuilder b, int l) {
    return consumeToken(b, COMMENT);
  }

  /* ********************************************************** */
  // scenarioHeading (step | comment)*
  public static boolean scenario(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scenario")) return false;
    if (!nextTokenIs(b, SCENARIO_HEADING)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = scenarioHeading(b, l + 1);
    r = r && scenario_1(b, l + 1);
    exit_section_(b, m, SCENARIO, r);
    return r;
  }

  // (step | comment)*
  private static boolean scenario_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scenario_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!scenario_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "scenario_1", c)) break;
    }
    return true;
  }

  // step | comment
  private static boolean scenario_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scenario_1_0")) return false;
    boolean r;
    r = step(b, l + 1);
    if (!r) r = comment(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // SCENARIO_HEADING
  static boolean scenarioHeading(PsiBuilder b, int l) {
    return consumeToken(b, SCENARIO_HEADING);
  }

  /* ********************************************************** */
  // (comment)*  specHeading table? (step|comment)*
  static boolean specDetail(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "specDetail")) return false;
    if (!nextTokenIs(b, "", COMMENT, SPEC_HEADING)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = specDetail_0(b, l + 1);
    r = r && specHeading(b, l + 1);
    r = r && specDetail_2(b, l + 1);
    r = r && specDetail_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (comment)*
  private static boolean specDetail_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "specDetail_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!specDetail_0_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "specDetail_0", c)) break;
    }
    return true;
  }

  // (comment)
  private static boolean specDetail_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "specDetail_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = comment(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // table?
  private static boolean specDetail_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "specDetail_2")) return false;
    table(b, l + 1);
    return true;
  }

  // (step|comment)*
  private static boolean specDetail_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "specDetail_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!specDetail_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "specDetail_3", c)) break;
    }
    return true;
  }

  // step|comment
  private static boolean specDetail_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "specDetail_3_0")) return false;
    boolean r;
    r = step(b, l + 1);
    if (!r) r = comment(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // specDetail scenario+
  static boolean specFile(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "specFile")) return false;
    if (!nextTokenIs(b, "", COMMENT, SPEC_HEADING)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = specDetail(b, l + 1);
    r = r && specFile_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // scenario+
  private static boolean specFile_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "specFile_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = scenario(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!scenario(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "specFile_1", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // SPEC_HEADING
  static boolean specHeading(PsiBuilder b, int l) {
    return consumeToken(b, SPEC_HEADING);
  }

  /* ********************************************************** */
  // STEP table?
  public static boolean step(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "step")) return false;
    if (!nextTokenIs(b, STEP)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, STEP);
    r = r && step_1(b, l + 1);
    exit_section_(b, m, STEP, r);
    return r;
  }

  // table?
  private static boolean step_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "step_1")) return false;
    table(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // TABLE_HEADER TABLE_ROW*
  public static boolean table(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table")) return false;
    if (!nextTokenIs(b, TABLE_HEADER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TABLE_HEADER);
    r = r && table_1(b, l + 1);
    exit_section_(b, m, TABLE, r);
    return r;
  }

  // TABLE_ROW*
  private static boolean table_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, TABLE_ROW)) break;
      if (!empty_element_parsed_guard_(b, "table_1", c)) break;
    }
    return true;
  }

}

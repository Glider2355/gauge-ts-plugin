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

  public ASTNode parse(IElementType root_, PsiBuilder builder_) {
    parseLight(root_, builder_);
    return builder_.getTreeBuilt();
  }

  public void parseLight(IElementType root_, PsiBuilder builder_) {
    boolean result_;
    builder_ = adapt_builder_(root_, builder_, this, null);
    Marker marker_ = enter_section_(builder_, 0, _COLLAPSE_, null);
    result_ = parse_root_(root_, builder_);
    exit_section_(builder_, 0, marker_, root_, result_, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType root_, PsiBuilder builder_) {
    return parse_root_(root_, builder_, 0);
  }

  static boolean parse_root_(IElementType root_, PsiBuilder builder_, int level_) {
    return specFile(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // COMMENT
  static boolean comment(PsiBuilder builder_, int level_) {
    return consumeToken(builder_, COMMENT);
  }

  /* ********************************************************** */
  // scenarioHeading (stepItem | comment | tag)*
  public static boolean scenario(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "scenario")) return false;
    if (!nextTokenIs(builder_, SCENARIO_HEADING)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = scenarioHeading(builder_, level_ + 1);
    result_ = result_ && scenario_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, SCENARIO, result_);
    return result_;
  }

  // (stepItem | comment | tag)*
  private static boolean scenario_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "scenario_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!scenario_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "scenario_1", pos_)) break;
    }
    return true;
  }

  // stepItem | comment | tag
  private static boolean scenario_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "scenario_1_0")) return false;
    boolean result_;
    result_ = stepItem(builder_, level_ + 1);
    if (!result_) result_ = comment(builder_, level_ + 1);
    if (!result_) result_ = tag(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // SCENARIO_HEADING
  static boolean scenarioHeading(PsiBuilder builder_, int level_) {
    return consumeToken(builder_, SCENARIO_HEADING);
  }

  /* ********************************************************** */
  // (comment | tag)*  specHeading table? (stepItem | comment | tag)*
  static boolean specDetail(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "specDetail")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = specDetail_0(builder_, level_ + 1);
    result_ = result_ && specHeading(builder_, level_ + 1);
    result_ = result_ && specDetail_2(builder_, level_ + 1);
    result_ = result_ && specDetail_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (comment | tag)*
  private static boolean specDetail_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "specDetail_0")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!specDetail_0_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "specDetail_0", pos_)) break;
    }
    return true;
  }

  // comment | tag
  private static boolean specDetail_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "specDetail_0_0")) return false;
    boolean result_;
    result_ = comment(builder_, level_ + 1);
    if (!result_) result_ = tag(builder_, level_ + 1);
    return result_;
  }

  // table?
  private static boolean specDetail_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "specDetail_2")) return false;
    table(builder_, level_ + 1);
    return true;
  }

  // (stepItem | comment | tag)*
  private static boolean specDetail_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "specDetail_3")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!specDetail_3_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "specDetail_3", pos_)) break;
    }
    return true;
  }

  // stepItem | comment | tag
  private static boolean specDetail_3_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "specDetail_3_0")) return false;
    boolean result_;
    result_ = stepItem(builder_, level_ + 1);
    if (!result_) result_ = comment(builder_, level_ + 1);
    if (!result_) result_ = tag(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // specDetail+ scenario*
  static boolean specFile(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "specFile")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = specFile_0(builder_, level_ + 1);
    result_ = result_ && specFile_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // specDetail+
  private static boolean specFile_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "specFile_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = specDetail(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!specDetail(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "specFile_0", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // scenario*
  private static boolean specFile_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "specFile_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!scenario(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "specFile_1", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // SPEC_HEADING
  static boolean specHeading(PsiBuilder builder_, int level_) {
    return consumeToken(builder_, SPEC_HEADING);
  }

  /* ********************************************************** */
  // STEP table?
  public static boolean stepItem(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "stepItem")) return false;
    if (!nextTokenIs(builder_, STEP)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, STEP);
    result_ = result_ && stepItem_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, STEP_ITEM, result_);
    return result_;
  }

  // table?
  private static boolean stepItem_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "stepItem_1")) return false;
    table(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // TABLE_HEADER TABLE_ROW*
  public static boolean table(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "table")) return false;
    if (!nextTokenIs(builder_, TABLE_HEADER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, TABLE_HEADER);
    result_ = result_ && table_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, TABLE, result_);
    return result_;
  }

  // TABLE_ROW*
  private static boolean table_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "table_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!consumeToken(builder_, TABLE_ROW)) break;
      if (!empty_element_parsed_guard_(builder_, "table_1", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // TAG
  static boolean tag(PsiBuilder builder_, int level_) {
    return consumeToken(builder_, TAG);
  }

}

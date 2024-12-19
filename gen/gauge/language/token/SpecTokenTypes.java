// This is a generated file. Not intended for manual editing.
package gauge.language.token;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import gauge.language.psi.impl.*;

public interface SpecTokenTypes {

    IElementType SCENARIO = new gauge.language.token.SpecElementType("SCENARIO");
    IElementType SPEC_DETAIL = new gauge.language.token.SpecElementType("SPEC_DETAIL");
    IElementType STATIC_ARG = new gauge.language.token.SpecElementType("STATIC_ARG");
    IElementType TABLE = new com.thoughtworks.gauge.language.token.SpecElementType("TABLE");
    IElementType TABLE_BODY = new com.thoughtworks.gauge.language.token.SpecElementType("TABLE_BODY");
    IElementType TEARDOWN = new SpecElementType("TEARDOWN");

    IElementType ARG = new com.thoughtworks.gauge.language.token.SpecTokenType("ARG");
    IElementType ARG_END = new com.thoughtworks.gauge.language.token.SpecTokenType("ARG_END");
    IElementType ARG_START = new com.thoughtworks.gauge.language.token.SpecTokenType("ARG_START");
    IElementType COMMENT = new com.thoughtworks.gauge.language.token.SpecTokenType("COMMENT");
    IElementType DYNAMIC_ARG = new com.thoughtworks.gauge.language.token.SpecTokenType("DYNAMIC_ARG");
    IElementType DYNAMIC_ARG_END = new com.thoughtworks.gauge.language.token.SpecTokenType("DYNAMIC_ARG_END");
    IElementType DYNAMIC_ARG_START = new com.thoughtworks.gauge.language.token.SpecTokenType("DYNAMIC_ARG_START");
    IElementType KEYWORD = new com.thoughtworks.gauge.language.token.SpecTokenType("KEYWORD");
    IElementType NEW_LINE = new com.thoughtworks.gauge.language.token.SpecTokenType("NEW_LINE");
    IElementType SCENARIO_HEADING = new com.thoughtworks.gauge.language.token.SpecTokenType("SCENARIO_HEADING");
    IElementType SPEC_COMMENT = new com.thoughtworks.gauge.language.token.SpecTokenType("SPEC_COMMENT");
    IElementType SPEC_HEADING = new com.thoughtworks.gauge.language.token.SpecTokenType("SPEC_HEADING");
    IElementType STEP = new com.thoughtworks.gauge.language.token.SpecTokenType("STEP");
    IElementType STEP_IDENTIFIER = new com.thoughtworks.gauge.language.token.SpecTokenType("STEP_IDENTIFIER");
    IElementType TABLE_BORDER = new com.thoughtworks.gauge.language.token.SpecTokenType("TABLE_BORDER");
    IElementType TABLE_HEADER = new com.thoughtworks.gauge.language.token.SpecTokenType("TABLE_HEADER");
    IElementType TABLE_ROW_VALUE = new com.thoughtworks.gauge.language.token.SpecTokenType("TABLE_ROW_VALUE");
    IElementType TAGS = new com.thoughtworks.gauge.language.token.SpecTokenType("TAGS");
    IElementType TEARDOWN_IDENTIFIER = new com.thoughtworks.gauge.language.token.SpecTokenType("TEARDOWN_IDENTIFIER");
    IElementType WHITESPACE = new SpecTokenType("WHITESPACE");

    class Factory {
        public static PsiElement createElement(ASTNode node) {
            IElementType type = node.getElementType();
            if (type == ARG) {
                return new SpecArgImpl(node);
            } else if (type == DYNAMIC_ARG) {
                return new SpecDynamicArgImpl(node);
            } else if (type == KEYWORD) {
                return new SpecKeywordImpl(node);
            } else if (type == SCENARIO) {
                return new SpecScenarioImpl(node);
            } else if (type == SPEC_DETAIL) {
                return new SpecDetailImpl(node);
            } else if (type == STATIC_ARG) {
                return new SpecStaticArgImpl(node);
            } else if (type == STEP) {
                return new SpecStepImpl(node);
            } else if (type == TABLE) {
                return new SpecTableImpl(node);
            } else if (type == TABLE_BODY) {
                return new SpecTableBodyImpl(node);
            } else if (type == TABLE_HEADER) {
                return new SpecTableHeaderImpl(node);
            } else if (type == TABLE_ROW_VALUE) {
                return new SpecTableRowValueImpl(node);
            } else if (type == TAGS) {
                return new SpecTagsImpl(node);
            } else if (type == TEARDOWN) {
                return new SpecTeardownImpl(node);
            }
            throw new AssertionError("Unknown element type: " + type);
        }
    }
}

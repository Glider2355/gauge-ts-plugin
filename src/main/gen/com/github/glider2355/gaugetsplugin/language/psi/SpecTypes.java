// This is a generated file. Not intended for manual editing.
package com.github.glider2355.gaugetsplugin.language.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.github.glider2355.gaugetsplugin.language.psi.impl.*;

public interface SpecTypes {

  IElementType PROPERTY = new SpecElementType("PROPERTY");

  IElementType COMMENT = new SpecTokenType("COMMENT");
  IElementType CRLF = new SpecTokenType("CRLF");
  IElementType KEY = new SpecTokenType("KEY");
  IElementType SEPARATOR = new SpecTokenType("SEPARATOR");
  IElementType VALUE = new SpecTokenType("VALUE");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == PROPERTY) {
        return new SpecPropertyImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}

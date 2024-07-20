package com.github.glider2355.gaugetsplugin.language.psi

import com.intellij.psi.tree.TokenSet

interface SpecTokenSets {
    companion object {
        val IDENTIFIERS: TokenSet = TokenSet.create(SpecTypes.KEY)
        val COMMENTS: TokenSet = TokenSet.create(SpecTypes.COMMENT)
    }
}
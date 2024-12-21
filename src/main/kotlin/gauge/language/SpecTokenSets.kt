package gauge.language

import com.intellij.psi.tree.TokenSet
import gauge.language.token.SpecTokenTypes

object SpecTokenSets {
    val WHITE_SPACES: TokenSet = TokenSet.WHITE_SPACE
    val COMMENTS: TokenSet = TokenSet.create(SpecTokenTypes.COMMENT)
}
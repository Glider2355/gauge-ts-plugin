package gauge.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import gauge.language.SpecFile
import gauge.language.SpecTokenSets
import gauge.language.Specification
import gauge.language.token.SpecTokenTypes
import gauge.lexer.SpecLexer

class SpecParserDefinition : ParserDefinition {
    companion object {
        val FILE = IFileElementType(Specification.INSTANCE)
    }

    override fun createLexer(project: Project): Lexer {
        return SpecLexer()
    }

    override fun getWhitespaceTokens(): TokenSet {
        return SpecTokenSets.WHITE_SPACES
    }

    override fun getCommentTokens(): TokenSet {
        return SpecTokenSets.COMMENTS
    }

    override fun getStringLiteralElements(): TokenSet {
        return TokenSet.EMPTY
    }

    override fun createParser(project: Project): PsiParser {
        return SpecParser()
    }

    override fun getFileNodeType(): IFileElementType {
        return FILE
    }

    override fun createFile(viewProvider: FileViewProvider): PsiFile {
        return SpecFile(viewProvider)
    }

    override fun spaceExistenceTypeBetweenTokens(left: ASTNode, right: ASTNode): ParserDefinition.SpaceRequirements {
        return ParserDefinition.SpaceRequirements.MAY
    }

    override fun createElement(node: ASTNode): PsiElement {
        return SpecTokenTypes.Factory.createElement(node)
    }
}
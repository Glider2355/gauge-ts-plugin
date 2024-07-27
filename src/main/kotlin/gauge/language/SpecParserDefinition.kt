package gauge.language

import gauge.language.parser.SpecParser
import gauge.language.psi.SpecFile
import gauge.language.psi.SpecTokenSets
import gauge.language.psi.SpecTypes
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

class SpecParserDefinition : ParserDefinition {

    companion object {
        val FILE: IFileElementType = IFileElementType(SpecLanguage.INSTANCE)
    }

    override fun createLexer(project: Project): Lexer {
        return SpecLexerAdapter()
    }

    override fun getCommentTokens(): TokenSet {
        return SpecTokenSets.COMMENTS
    }

    override fun getStringLiteralElements(): TokenSet {
        return TokenSet.EMPTY
    }

    override fun createParser(project: Project): PsiParser {
        return gauge.language.parser.SpecParser()
    }

    override fun getFileNodeType(): IFileElementType {
        return FILE
    }

    override fun createFile(viewProvider: FileViewProvider): PsiFile {
        return SpecFile(viewProvider)
    }

    override fun createElement(node: ASTNode): PsiElement {
        return SpecTypes.Factory.createElement(node)
    }
}
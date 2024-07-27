package gauge.language

import com.intellij.lexer.FlexAdapter

class SpecLexerAdapter : FlexAdapter(SpecLexer(null))
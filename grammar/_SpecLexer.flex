package gauge.lexer;

import com.intellij.lexer.*;
import com.intellij.psi.tree.IElementType;
import static gauge.language.token.SpecTokenTypes.*;

%%
%{
  public _SpecLexer() {
    this((java.io.Reader)null);
  }
%}


// Flexディレクティブ
// 生成されるLexerクラスはpublic
%public
// 生成されるLexerクラス名
%class _SpecLexer
// IntelliJ IDEAのFlexLexerインターフェースを実装
%implements FlexLexer
// メイン処理関数をadvanceに指定
%function advance
// トークンの型をIElementTypeに指定
%type IElementType
// Unicodeをサポート
%unicode
// テーブル内の状態をINTABLEに指定
%state INTABLE

// トークン定義
// 改行文字
LineTerminator = \r|\n|\r\n
// 改行文字以外の文字
InputCharacter = [^\r\n]
// テーブルのセル内の文字
TableInputCharacter = [^|\r\n]
// 空白文字
WhiteSpace = [ \t\f]

// シナリオの見出し(##で始まる行)
ScenarioHeading = {WhiteSpace}* "##" {InputCharacter}* {LineTerminator}+ | {WhiteSpace}* {InputCharacter}* {LineTerminator} [-]+ {LineTerminator}+
// 仕様の見出し(#で始まる行)
SpecHeading = {WhiteSpace}* "#" {InputCharacter}* {LineTerminator}+ | {WhiteSpace}* {InputCharacter}* {LineTerminator} [=]+ {LineTerminator}+
// ステップ(*で始まる行)
Step = {WhiteSpace}* "*" [^*] {InputCharacter}* {LineTerminator}*
// テーブルのヘッダー（|区切りの行）
TableHeader = {WhiteSpace}* ("|" {TableInputCharacter}*)+ "|" {LineTerminator} | {WhiteSpace}* ("|" {TableInputCharacter}*)+ "|" {LineTerminator} {WhiteSpace}* {TableSeparator}+ {LineTerminator}+
// テーブルの行
TableRow={WhiteSpace}* ("|" {TableInputCharacter}*)+ "|" {LineTerminator}
// コメント
Comment = {InputCharacter}*? {LineTerminator}*?
// テーブルのセパレータ
TableSeparator = [-|]

// ルール
%%
// 初期状態でのトークン認識ルール
<YYINITIAL> {
  {ScenarioHeading} {return SCENARIO_HEADING;}
  {SpecHeading}     {return SPEC_HEADING;}
  {Step}            {return STEP;}
  {TableHeader}     {yybegin(INTABLE);return TABLE_HEADER;}
  {Comment}         {return COMMENT;}
}

// テーブル内の状態でのトークン認識ルール
<INTABLE> {
  {TableRow}         {yybegin(INTABLE);return TABLE_ROW;}
  [^]                {yypushback(1); yybegin(YYINITIAL);}
}
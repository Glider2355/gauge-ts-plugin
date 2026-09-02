package gauge.finder

import com.intellij.lang.javascript.psi.JSArrayLiteralExpression
import com.intellij.lang.javascript.psi.JSExpression
import com.intellij.lang.javascript.psi.JSLiteralExpression

internal object StepPatternExtractor {

    // @Step(...) の引数式から step パターン文字列を抽出する。
    //  - 文字列リテラル      @Step('foo')         → ["foo"]
    //  - 配列リテラル        @Step(['foo', 'bar']) → ["foo", "bar"]
    //  - 動的式（変数参照・結合等）は解析不能なので空を返す（gauge-ts ランタイム自体も動的 step 名は未対応）。
    fun extract(argument: JSExpression): List<String> {
        return when (argument) {
            is JSArrayLiteralExpression -> argument.expressions.mapNotNull { toStringLiteral(it) }
            else -> listOfNotNull(toStringLiteral(argument))
        }
    }

    private fun toStringLiteral(expr: JSExpression?): String? {
        val literal = expr as? JSLiteralExpression ?: return null
        if (!literal.isStringLiteral) return null
        return literal.stringValue
    }
}

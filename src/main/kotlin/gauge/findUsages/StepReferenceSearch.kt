package gauge.findUsages

import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.psi.PsiReference
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.util.Processor

/**
 * TODO: 下記を追加する
 * <referencesSearch implementation="gauge.findUsages.StepReferenceSearch"/>
 *
 * StepReferenceSearch は、特定の言語要素（この場合、ステップ）がどこで参照されているかを検索するためのクラスです。
 * このクラスは、ReferencesSearch の検索処理をカスタマイズするために使用され、特定のステップがどのSpecファイルや他の場所で使用されているかを検索します。
 *
 * 役割の具体例:
 * 例えば、TypeScriptで実装されたステップがSpecファイル内でどこで使用されているかを検索したい場合、このクラスを使ってSpecファイルを解析し、ステップの参照を探します。
 */

class StepReferenceSearch: QueryExecutorBase<PsiReference, ReferencesSearch.SearchParameters>(true) {

    // これは、指定された PsiElement に対する参照を検索し、見つかった参照を Processor に渡すメソッドです。
    // このメソッドの実装により、特定のステップがプロジェクト内のどこで使用されているかを検索します。
    override fun processQuery(
        queryParameters: ReferencesSearch.SearchParameters,
        consumer: Processor<in PsiReference>
    ) {
        TODO("Not yet implemented")
    }
}
package gauge.findUsages

import com.intellij.find.findUsages.FindUsagesHandler
import com.intellij.find.findUsages.FindUsagesOptions
import com.intellij.psi.PsiElement
import com.intellij.usageView.UsageInfo
import com.intellij.util.Processor

/**
 * TODO: StepFindUsagesHandlerFactoryに登録する
 * FindUsagesHandler は、特定の PsiElement に対して使用箇所の検索を実際に行うクラスです。
 * これは、プロジェクト全体を検索し、指定された要素がどこで使われているか（メソッドの呼び出し、フィールドの参照など）を見つけるためのロジックを提供します。
 * また、検索結果を整形し、適切にユーザーに表示するための処理も行います。
 */

class StepFindUsagesHandler(element: PsiElement): FindUsagesHandler(element) {

    /**
     * 指定された PsiElement に対して使用箇所検索を行うことができるかどうかを判定します。
     * 例: プロジェクト全体を検索し、指定されたメソッドがどこで呼び出されているかを見つけ、その情報を Processor 経由で収集します。
     */
    override fun processElementUsages(
        element: PsiElement,
        processor: Processor<in UsageInfo>,
        options: FindUsagesOptions
    ): Boolean {
        return super.processElementUsages(element, processor, options)
    }
}
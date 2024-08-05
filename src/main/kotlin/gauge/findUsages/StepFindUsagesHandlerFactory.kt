package gauge.findUsages

import com.intellij.find.findUsages.FindUsagesHandler
import com.intellij.find.findUsages.FindUsagesHandlerFactory
import com.intellij.psi.PsiElement

/**
 * TODO: 下記を追加する
 * <findUsagesHandlerFactory implementation="gauge.findUsages.TypeScriptStepFindUsagesHandlerFactory"/>
 *
 * FindUsagesHandlerFactory は、Find Usages 機能のための FindUsagesHandler を作成するためのファクトリクラスです。
 * FindUsagesHandler は実際に使用箇所を検索する際に使用されるクラスで、このファクトリは特定の PsiElement に対して適切なハンドラを提供します。
 *
 * 役割の具体例:
 * 例えば、特定のJavaメソッドに対して Find Usages を実行する際、そのメソッドに対する使用箇所を探す FindUsagesHandler を提供します。
 * このハンドラは、実際にプロジェクト全体を検索して、使用箇所を特定します。
 */

class StepFindUsagesHandlerFactory: FindUsagesHandlerFactory() {

    // 指定された PsiElement に対して使用箇所検索を行うことができるかどうかを判定します。
    override fun canFindUsages(element: PsiElement): Boolean {
        TODO("Not yet implemented")
    }

    // 指定された PsiElement に対して使用箇所検索を行うための FindUsagesHandler を作成します。
    override fun createFindUsagesHandler(element: PsiElement, forHighlightUsages: Boolean): FindUsagesHandler? {
        TODO("Not yet implemented")
    }

}
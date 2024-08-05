package gauge.findUsages

import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement

/**
 * TODO: 下記を追加する
 * <lang.findUsagesProvider implementation="gauge.findUsages.StepFindUsagesProvider"/>
 *
 * FindUsagesProvider は、IntelliJ IDEAのプラットフォームで、特定の言語要素に対して「Find Usages」（使用箇所の検索）機能を提供するためのクラスです。
 * このクラスは、検索対象の要素が何であるかを指定し、その要素のどの部分がユーザーインターフェースに表示されるか、またどのように検索されるかを定義します。
 *
 * 役割の具体例:
 * 例えば、Javaのメソッドに対して Find Usages 機能を提供する場合、
 * FindUsagesProvider クラスはそのメソッドの名前やシグネチャを取得し、それがどこで使用されているかを見つけるための基準を提供します。
 */
class StepFindUsagesProvider: FindUsagesProvider {

    // 指定された PsiElement が使用箇所検索の対象になるかどうかを判定します。
    override fun canFindUsagesFor(psiElement: PsiElement): Boolean {
        TODO("Not yet implemented")
    }

    override fun getHelpId(psiElement: PsiElement): String? {
        TODO("Not yet implemented")
    }

    // 検索対象の要素のタイプを返します。例えば、「クラス」、「メソッド」、「フィールド」など、要素の種類を表す文字列を返します。
    override fun getType(element: PsiElement): String {
        TODO("Not yet implemented")
    }

    // 検索結果のリストやUIに表示される要素の名前を返します。
    override fun getDescriptiveName(element: PsiElement): String {
        TODO("Not yet implemented")
    }

    // ユーザーインターフェースでノードとして表示される際のテキストを返します。
    override fun getNodeText(element: PsiElement, useFullName: Boolean): String {
        TODO("Not yet implemented")
    }
}
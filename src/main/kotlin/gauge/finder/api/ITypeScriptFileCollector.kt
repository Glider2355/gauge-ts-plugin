package gauge.finder.api

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

/**
 * TypeScriptファイルを収集するためのインターフェイス。
 */
interface ITypeScriptFileCollector {
    /**
     * 指定されたディレクトリパスからTypeScriptファイルを収集します。
     *
     * @param project IntelliJプロジェクト。
     * @param directoryPath 検索対象のディレクトリパス。
     * @return TypeScriptファイルのリスト。
     */
    fun collectTypeScriptFiles(project: Project, directoryPath: String): List<PsiFile>


    /**
     * 指定されたディレクトリから@Stepアノテーションを検索します。
     *
     * @param project IntelliJプロジェクト。
     * @param searchDirectories 検索対象のディレクトリリスト。
     * @return @Stepアノテーションのリスト。
     */
    fun findStepAnnotations(project: Project, searchDirectories: MutableList<String>): List<String>

    /**
     * 指定されたディレクトリからステップ関数を検索します。
     *
     * @param project IntelliJプロジェクト。
     * @param searchDirectories 検索対象のディレクトリリスト。
     * @param stepText ステップのテキスト。
     * @return ステップ関数のPsiElement。
     */
    fun findStepFunction(project: Project, searchDirectories: MutableList<String>, stepText: String): PsiFile?
}

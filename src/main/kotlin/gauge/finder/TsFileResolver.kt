package gauge.finder

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiFile
import gauge.setting.PluginSettings

/**
 * PluginSettings の設定 (AUTO/MANUAL, useGaugeRootScope) を見て
 * 検索対象の .ts ファイル一覧を組み立てる中央窓口。
 * AUTO で Gauge プロジェクトルートが 1 つも見つからない場合はプロジェクト全体を対象にする。
 * StepFunctionFinder / StepAnnotationsFinder はこれを呼ぶだけでよい。
 */
object TsFileResolver {

    fun resolveTypeScriptFiles(project: Project): List<PsiFile> {
        val settings = project.service<PluginSettings>()
        val collector = TypeScriptFileCollector()
        return when (settings.scanMode) {
            PluginSettings.ScanMode.AUTO -> {
                val roots = if (settings.useGaugeRootScope) {
                    GaugeProjectRootFinder.findGaugeProjectRoots(project)
                } else {
                    emptyList()
                }
                collector.collectAllTypeScriptFilesInProject(project, roots)
            }
            PluginSettings.ScanMode.MANUAL -> {
                settings.validDirectories.flatMap { path ->
                    val vf = LocalFileSystem.getInstance().findFileByPath(path)
                    collector.collectTypeScriptFiles(project, vf)
                }
            }
        }
    }
}

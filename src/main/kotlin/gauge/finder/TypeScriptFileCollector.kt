package gauge.finder

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope

internal class TypeScriptFileCollector {

    fun collectTypeScriptFiles(project: Project, virtualFile: VirtualFile?): List<PsiFile> {
        val files = mutableListOf<PsiFile>()

        if (virtualFile != null && virtualFile.isDirectory) {
            val psiManager = PsiManager.getInstance(project)
            VfsUtil.iterateChildrenRecursively(virtualFile, null) { file ->
                if (isStepImplementationCandidate(file)) {
                    psiManager.findFile(file)?.let { psiFile ->
                        files.add(psiFile)
                    }
                }
                true
            }
        }

        return files
    }

    /**
     * プロジェクトスコープ全体の .ts ファイルを FilenameIndex から取得。
     * IntelliJ の索引を使うのでディレクトリ再帰列挙より速い。
     * projectScope は IDE の Excluded フォルダと ignore パターン (node_modules 等) を除外するが、
     * .gitignore の内容は考慮しない。
     *
     * @param gaugeRoots 空でなければ、その配下の .ts のみに絞る (Gauge プロジェクトルート起点スコープ用途)
     */
    fun collectAllTypeScriptFilesInProject(
        project: Project,
        gaugeRoots: List<VirtualFile> = emptyList()
    ): List<PsiFile> {
        val scope = GlobalSearchScope.projectScope(project)
        val virtualFiles = FilenameIndex.getAllFilesByExt(project, "ts", scope)
            .filter { isStepImplementationCandidate(it) }
        val filtered = if (gaugeRoots.isEmpty()) {
            virtualFiles
        } else {
            virtualFiles.filter { file ->
                gaugeRoots.any { root -> VfsUtil.isAncestor(root, file, true) }
            }
        }
        val psiManager = PsiManager.getInstance(project)
        return filtered.mapNotNull { psiManager.findFile(it) }
    }

    companion object {
        /** @Step 実装を含みうる .ts か。型定義ファイル (.d.ts) は実装を持たないので走査対象から外す */
        fun isStepImplementationCandidate(file: VirtualFile): Boolean =
            !file.isDirectory && file.extension == "ts" && !file.name.endsWith(".d.ts")
    }
}

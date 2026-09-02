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
            VfsUtil.iterateChildrenRecursively(virtualFile, null) { file ->
                if (!file.isDirectory && file.extension == "ts") {
                    PsiManager.getInstance(project).findFile(file)?.let { psiFile ->
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
     * .gitignore / Excluded Folders は projectScope 側で除外される。
     *
     * @param gaugeRoots 空でなければ、その配下の .ts のみに絞る (.gauge/ 起点スコープ用途)
     */
    fun collectAllTypeScriptFilesInProject(
        project: Project,
        gaugeRoots: List<VirtualFile> = emptyList()
    ): List<PsiFile> {
        val scope = GlobalSearchScope.projectScope(project)
        val virtualFiles = FilenameIndex.getAllFilesByExt(project, "ts", scope)
        val filtered = if (gaugeRoots.isEmpty()) {
            virtualFiles
        } else {
            virtualFiles.filter { file ->
                gaugeRoots.any { root -> com.intellij.openapi.vfs.VfsUtil.isAncestor(root, file, true) }
            }
        }
        val psiManager = PsiManager.getInstance(project)
        return filtered.mapNotNull { psiManager.findFile(it) }
    }
}

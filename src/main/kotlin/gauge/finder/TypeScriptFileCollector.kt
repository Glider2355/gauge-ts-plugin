package gauge.finder

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager

class TypeScriptFileCollector {

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
}

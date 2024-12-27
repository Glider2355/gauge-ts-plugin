package gauge.util

import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import gauge.language.SpecFile
import gauge.language.SpecFileType
import java.util.*

object GaugeUtil {
    fun isSpecFile(file: PsiFile?): Boolean {
        return file is SpecFile
    }
    fun isSpecFile(selectedFile: VirtualFile): Boolean {
        return selectedFile.fileType.javaClass == SpecFileType::class.java
    }

    fun moduleForPsiElement(element: PsiElement): Module? {
        val file = element.containingFile
        return ModuleUtilCore.findModuleForPsiElement(Objects.requireNonNullElse(file, element))
    }
}
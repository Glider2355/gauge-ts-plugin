package gauge.util

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import gauge.language.SpecFile
import gauge.language.SpecFileType

object GaugeUtil {
    fun isSpecFile(file: PsiFile?): Boolean {
        return file is SpecFile
    }
    fun isSpecFile(selectedFile: VirtualFile): Boolean {
        return selectedFile.fileType.javaClass == SpecFileType::class.java
    }

}
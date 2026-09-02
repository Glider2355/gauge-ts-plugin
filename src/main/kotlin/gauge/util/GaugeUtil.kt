package gauge.util

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import gauge.language.SpecFile
import gauge.language.SpecFileType

object GaugeUtil {
    fun isSpecFile(file: PsiFile?): Boolean {
        // .cpt も SpecFile (同一言語) なので FileType で .spec に限定する
        return file is SpecFile && file.fileType == SpecFileType.INSTANCE
    }
    fun isSpecFile(selectedFile: VirtualFile): Boolean {
        return selectedFile.fileType.javaClass == SpecFileType::class.java
    }

}
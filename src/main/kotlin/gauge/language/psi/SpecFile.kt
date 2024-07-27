package gauge.language.psi

import gauge.language.SpecLanguage
import gauge.language.SpecFileType
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

class SpecFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, SpecLanguage.INSTANCE) {

    override fun getFileType(): FileType {
        return SpecFileType
    }

    override fun toString(): String {
        return "Spec File"
    }
}
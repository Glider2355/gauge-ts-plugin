package gauge.language

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import javax.swing.Icon

class SpecFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, Specification.INSTANCE) {
    // .spec は SpecFileType、.cpt は ConceptFileType。同じ言語を共有するため ViewProvider から実際の型を返す
    override fun getFileType(): FileType = viewProvider.fileType

    override fun toString(): String {
        return "Specification File"
    }

    override fun getIcon(flags: Int): Icon? {
        return super.getIcon(flags)
    }
}
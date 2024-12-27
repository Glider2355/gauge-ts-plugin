package gauge.language

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import javax.swing.Icon

class SpecFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, Specification.INSTANCE) {
    override fun getFileType(): FileType {
        return SpecFileType.INSTANCE
    }

    override fun toString(): String {
        return "Specification File"
    }

    override fun getIcon(flags: Int): Icon? {
        return super.getIcon(flags)
    }
}
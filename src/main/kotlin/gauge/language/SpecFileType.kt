package gauge.language

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

object SpecFileType : LanguageFileType(SpecLanguage.INSTANCE) {

    override fun getName(): String {
        return "Spec File"
    }

    override fun getDescription(): String {
        return "Spec language file"
    }

    override fun getDefaultExtension(): String {
        return "spec"
    }

    override fun getIcon(): Icon {
        return SpecIcons.FILE
    }
}

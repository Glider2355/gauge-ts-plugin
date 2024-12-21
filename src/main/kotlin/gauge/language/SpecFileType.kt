package gauge.language

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

class SpecFileType private constructor() : LanguageFileType(Specification.INSTANCE) {

    companion object {
        val INSTANCE: SpecFileType = SpecFileType()
    }

    override fun getName(): String {
        return "Specification"
    }

    override fun getDescription(): String {
        return "Gauge specification file"
    }

    override fun getDefaultExtension(): String {
        return "spec"
    }

    override fun getIcon(): Icon {
        return SpecificationIcons.FILE
    }
}

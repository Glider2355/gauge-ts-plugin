package gauge.language

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

/**
 * Gauge の concept ファイル (.cpt)。
 * 言語は .spec と同じ Specification を共有するが、FileType を分けることで
 * 実行対象 (gauge run) から除外し、ハイライト側でも spec / cpt を判別できるようにする。
 * secondary=true にして Specification.associatedFileType は SpecFileType のままにする。
 */
class ConceptFileType private constructor() : LanguageFileType(Specification.INSTANCE, true) {

    companion object {
        val INSTANCE: ConceptFileType = ConceptFileType()
    }

    override fun getName(): String = "Gauge Concept"

    override fun getDescription(): String = "Gauge concept file"

    override fun getDefaultExtension(): String = "cpt"

    override fun getIcon(): Icon = SpecificationIcons.FILE
}

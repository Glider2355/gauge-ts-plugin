package gauge.highlight

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import gauge.finder.ConceptIndex
import gauge.language.SpecFileType
import gauge.language.token.SpecTokenTypes

class GaugeConceptAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element.node?.elementType != SpecTokenTypes.STEP) return
        // concept 呼び出しの強調は .spec のみ (.cpt 自身は対象外)
        if (element.containingFile?.fileType != SpecFileType.INSTANCE) return

        val stepText = element.text.trimStart().removePrefix("*").trim()
        val normalized = ConceptIndex.normalize(stepText)
        if (normalized.isEmpty()) return

        val concepts = ConceptIndex.findConceptTexts(element.project)
        if (normalized in concepts) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(element.textRange)
                .textAttributes(SpecHighlighterColors.CONCEPT_STEP)
                .create()
        }
    }
}

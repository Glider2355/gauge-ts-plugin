package gauge.finder

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import com.intellij.psi.util.PsiTreeUtil
import gauge.language.token.SpecTokenTypes

/** .cpt 内の concept 定義 1 件。headingElement は見出しの SPEC_HEADING トークン (ジャンプ先) */
data class ConceptDefinition(val headingElement: PsiElement, val normalized: String)

object ConceptIndex {

    private val QUOTED_PARAM = Regex("\"[^\"]*\"")
    private val ANGLE_PARAM = Regex("<[^>]+>")

    /** 正規化済み concept 見出しの集合 (アノテータの一致判定用) */
    fun findConceptTexts(project: Project): Set<String> = cachedDefinitions(project).keys

    /** 正規化済みステップ文に一致する concept 定義。同じ見出しが複数の .cpt にあれば最初に見つかったもの */
    fun findConceptDefinition(project: Project, normalizedStepText: String): ConceptDefinition? =
        cachedDefinitions(project)[normalizedStepText]

    // 正規化済み見出し -> 定義 の Map をプロジェクト単位でキャッシュする。
    // アノテータは STEP ごとに問い合わせるので、呼び出し側でコレクションを組み直さずに済む形で持つ。
    // PSI が変わるたびに作り直されるため、見出しトークンの参照をそのまま保持してよい
    private fun cachedDefinitions(project: Project): Map<String, ConceptDefinition> {
        return CachedValuesManager.getManager(project).getCachedValue(project) {
            CachedValueProvider.Result.create(
                collect(project),
                PsiModificationTracker.MODIFICATION_COUNT
            )
        }
    }

    private fun collect(project: Project): Map<String, ConceptDefinition> {
        val result = LinkedHashMap<String, ConceptDefinition>()
        val cptFiles = FilenameIndex.getAllFilesByExt(project, "cpt", GlobalSearchScope.projectScope(project))
        val psiManager = PsiManager.getInstance(project)
        for (vf in cptFiles) {
            val psi = psiManager.findFile(vf) ?: continue
            for (heading in findSpecHeadings(psi)) {
                val normalized = normalize(headingText(heading.text))
                if (normalized.isNotEmpty()) {
                    result.putIfAbsent(normalized, ConceptDefinition(heading, normalized))
                }
            }
        }
        return result
    }

    private fun findSpecHeadings(file: PsiFile): Array<PsiElement> =
        PsiTreeUtil.collectElements(file) { it.node?.elementType == SpecTokenTypes.SPEC_HEADING }

    /**
     * SPEC_HEADING トークンのテキストから見出し文だけを取り出す。
     * トークンは末尾の改行を含み、`# 見出し` 形式のほか次行を `====` で下線する形式もあるので、
     * 先頭行だけを見て `#` を外す
     */
    fun headingText(tokenText: String): String =
        tokenText.trimStart().lineSequence().first().removePrefix("#").trim()

    /** ステップ文・concept 見出しから引数 ("..." と <...>) を取り除き、比較用の形にする */
    fun normalize(text: String): String = text
        .replace(QUOTED_PARAM, "")
        .replace(ANGLE_PARAM, "")
        .trim()
}

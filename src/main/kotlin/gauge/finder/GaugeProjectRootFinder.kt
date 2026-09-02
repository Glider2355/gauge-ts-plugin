package gauge.finder

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileVisitor
import java.io.IOException

/**
 * プロジェクト内の Gauge プロジェクトルートを探す。次のいずれかを直下に持つディレクトリをルートとみなす。
 *  - Gauge の manifest.json ("Language" キーを持つ)。gauge init で生成されコミットされるので常に存在する
 *  - .gauge/ ディレクトリ。実行時に生成され .gitignore 対象なので、クローン直後には無いことが多い
 * モノレポで複数の Gauge プロジェクトが並ぶケースに対応するため全部拾う。
 * ルートと判定したディレクトリの配下は、スコープ判定が祖先ベースなのでそれ以上降下しない。
 * 一般的な重いディレクトリ (node_modules 等) にも降下しない。
 */
object GaugeProjectRootFinder {

    private const val MANIFEST_FILE = "manifest.json"
    private const val GAUGE_DIR = ".gauge"
    private const val MAX_MANIFEST_BYTES = 64 * 1024L
    // Gauge の manifest.json は {"Language": "ts", "Plugins": [...]} の形。
    // Web App Manifest や Chrome 拡張の manifest.json と区別するため Language キーを要求する
    private val MANIFEST_LANGUAGE_KEY = Regex("\"Language\"\\s*:")

    private val SKIP_DIRS = setOf(
        "node_modules", ".git", "dist", "build", "target",
        ".idea", ".gradle", "out", "coverage", ".next", ".nuxt"
    )

    fun findGaugeProjectRoots(project: Project): List<VirtualFile> {
        val results = linkedSetOf<VirtualFile>()
        val contentRoots = ProjectRootManager.getInstance(project).contentRoots
        for (root in contentRoots) {
            VfsUtil.visitChildrenRecursively(root, object : VirtualFileVisitor<Void>() {
                override fun visitFile(file: VirtualFile): Boolean {
                    if (!file.isDirectory) return true
                    if (file.name in SKIP_DIRS) return false
                    if (isGaugeProjectRoot(file)) {
                        results.add(file)
                        return false
                    }
                    return true
                }
            })
        }
        return results.toList()
    }

    private fun isGaugeProjectRoot(dir: VirtualFile): Boolean {
        val manifest = dir.findChild(MANIFEST_FILE)
        if (manifest != null && !manifest.isDirectory && isGaugeManifest(manifest)) return true
        return dir.findChild(GAUGE_DIR)?.isDirectory == true
    }

    private fun isGaugeManifest(file: VirtualFile): Boolean {
        if (file.length > MAX_MANIFEST_BYTES) return false
        return try {
            isGaugeManifest(VfsUtil.loadText(file))
        } catch (e: IOException) {
            false
        }
    }

    /** manifest.json の内容が Gauge のものか (テスト容易性のため文字列で判定する) */
    fun isGaugeManifest(text: String): Boolean = MANIFEST_LANGUAGE_KEY.containsMatchIn(text)
}

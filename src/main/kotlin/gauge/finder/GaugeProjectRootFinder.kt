package gauge.finder

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileVisitor

/**
 * プロジェクト内の Gauge プロジェクトルート (`.gauge/` を含むディレクトリの親) を探す。
 * モノレポで複数の Gauge プロジェクトが並ぶケースに対応するため BFS で全部拾う。
 * 一般的な重いディレクトリ (node_modules 等) は降下しない。
 */
object GaugeProjectRootFinder {

    private val SKIP_DIRS = setOf(
        "node_modules", ".git", "dist", "build", "target",
        ".idea", ".gradle", "out", "coverage", ".next", ".nuxt"
    )

    fun findGaugeProjectRoots(project: Project): List<VirtualFile> {
        val results = mutableListOf<VirtualFile>()
        val contentRoots = ProjectRootManager.getInstance(project).contentRoots
        for (root in contentRoots) {
            VfsUtil.visitChildrenRecursively(root, object : VirtualFileVisitor<Void>() {
                override fun visitFile(file: VirtualFile): Boolean {
                    if (!file.isDirectory) return true
                    if (file.name in SKIP_DIRS) return false
                    if (file.name == ".gauge") {
                        file.parent?.let { results.add(it) }
                        return false
                    }
                    return true
                }
            })
        }
        return results
    }
}

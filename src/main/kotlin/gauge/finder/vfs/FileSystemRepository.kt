package gauge.finder.vfs

import com.intellij.openapi.vfs.VirtualFile

interface FileSystemRepository {
    fun findFileByPath(directoryPath: String): VirtualFile?
    fun findFilesByPaths(directoryPaths: Set<String>): Set<VirtualFile>
}
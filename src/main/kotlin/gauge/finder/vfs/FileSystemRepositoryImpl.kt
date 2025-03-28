package gauge.finder.vfs

import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile

class FileSystemRepositoryImpl {
    private val fileSystem = LocalFileSystem.getInstance()
    fun findFileByPath(directoryPath: String): VirtualFile? {
        return fileSystem.findFileByPath(directoryPath)
    }

    fun findFilesByPaths(directoryPaths: Set<String>): Set<VirtualFile> {
        return directoryPaths.mapNotNull { fileSystem.findFileByPath(it) }.toSet()
    }
}
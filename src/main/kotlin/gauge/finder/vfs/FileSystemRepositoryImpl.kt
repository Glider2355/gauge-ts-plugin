package gauge.finder.vfs

import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile

class FileSystemRepositoryImpl: FileSystemRepository {
    private val fileSystem = LocalFileSystem.getInstance()
    override fun findFileByPath(directoryPath: String): VirtualFile? {
        return fileSystem.findFileByPath(directoryPath)
    }

    override fun findFilesByPaths(directoryPaths: Set<String>): Set<VirtualFile> {
        return directoryPaths.mapNotNull { fileSystem.findFileByPath(it) }.toSet()
    }
}
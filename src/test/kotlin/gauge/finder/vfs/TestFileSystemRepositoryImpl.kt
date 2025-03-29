package gauge.finder.vfs

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.testFramework.fixtures.TempDirTestFixture

class TestFileSystemRepositoryImpl(private val dir: TempDirTestFixture): FileSystemRepository {

    override fun findFileByPath(directoryPath: String): VirtualFile? {
        return dir.getFile(directoryPath)
    }

    override fun findFilesByPaths(directoryPaths: Set<String>): Set<VirtualFile> {
        return directoryPaths.mapNotNull { dir.getFile(it) }.toSet()
    }
}

package gauge.finder.vfs

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.openapi.vfs.VirtualFile

class TestFileSystemRepositoryImplTest : BasePlatformTestCase() {

    private lateinit var fileSystemRepository: TestFileSystemRepositoryImpl

    override fun setUp() {
        super.setUp()
        fileSystemRepository = TestFileSystemRepositoryImpl(myFixture.tempDirFixture)
    }

    fun `test findFileByPath should return correct file`() {
        val filePath = "testRoot/sample.ts"
        myFixture.addFileToProject(filePath, "console.log('Hello, world');")

        // `findFileByPath` でファイルを取得
        val result: VirtualFile? = fileSystemRepository.findFileByPath(filePath)

        // ファイルが正しく取得できたか検証
        assertNotNull("Expected file to be found", result)
        assertEquals("Expected file path to match", "/src/testRoot/sample.ts", result?.path)
    }

    fun `test findFilesByPaths should return correct files`() {
        val file1 = "testRoot/file1.ts"
        val file2 = "testRoot/file2.ts"
        myFixture.addFileToProject(file1, "export const value = 1;")
        myFixture.addFileToProject(file2, "export const value = 2;")

        val result: Set<VirtualFile> = fileSystemRepository.findFilesByPaths(setOf(file1, file2))

        assertEquals("Expected to find 2 files", 2, result.size)
        assertTrue("Expected file1 to be found", result.any { it.path.endsWith("testRoot/file1.ts") })
        assertTrue("Expected file2 to be found", result.any { it.path.endsWith("testRoot/file2.ts") })
    }
}

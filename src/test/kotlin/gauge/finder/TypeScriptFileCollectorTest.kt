package gauge.finder
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class TypeScriptFileCollectorTest : BasePlatformTestCase() {

    fun `test collect typescript files`() {
        // Create a JavaScript file instead
        myFixture.addFileToProject(
            "test/dir/only_js.js",
            "// JavaScript content"
        )
        myFixture.addFileToProject(
            "test/dir/only_ts.ts",
            "// TypeScript content"
        )

        // Use the collector
        val collector = TypeScriptFileCollector()
        val virtualFile = myFixture.tempDirFixture.getFile("test/dir")
        val files = collector.collectTypeScriptFiles(project, virtualFile)

        // Verify no TypeScript files found
        assertEquals("Expected one TypeScript file", 1, files.size)
        assertEquals("Expected TypeScript file", "only_ts.ts", files[0].name)
    }
}
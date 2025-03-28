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
            "test/dir/only_ts1.ts",
            "// TypeScript content"
        )
        myFixture.addFileToProject(
            "test/dir/tmp/only_ts2.ts",
            "// TypeScript content"
        )

        // Use the collector
        val collector = TypeScriptFileCollector()
        val virtualFile = myFixture.tempDirFixture.getFile("test/dir")
        val files = collector.collectTypeScriptFiles(project, virtualFile)

        // Verify no TypeScript files found
        assertEquals("Expected one TypeScript file", 2, files.size)
        assertEquals("Expected TypeScript file", "only_ts1.ts", files[1].name)
        assertEquals("Expected TypeScript file", "only_ts2.ts", files[0].name)
    }
}
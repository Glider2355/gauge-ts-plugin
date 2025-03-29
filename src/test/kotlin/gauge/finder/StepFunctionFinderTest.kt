package gauge.finder

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import gauge.finder.vfs.TestFileSystemRepositoryImpl

class StepFunctionFinderTest: BasePlatformTestCase() {
    fun testFindStepFunction() {
        myFixture.addFileToProject(
            "testRoot/step.ts",
            "@Step(\"step text\")\n"
                    + "function stepFunction() {\n"
                    + "  // Step function implementation\n"
                    + "}"
                    + "\n"
                    + "function anotherFunction() {\n"
                    + "  // Another function implementation\n"
                    + "}"
        )

        val fileSystemRepository = TestFileSystemRepositoryImpl(myFixture.tempDirFixture)
        val typeScriptFileCollector = TypeScriptFileCollector()
        val finder = StepFunctionFinder(fileSystemRepository, typeScriptFileCollector)

        val stepText = "step text"
        val searchDirectories = setOf("testRoot")
        val result = finder.findStepFunction(project, searchDirectories, stepText)

        val expected = "@Step(\"step text\")\n" +
                "function stepFunction() {\n" +
                "  // Step function implementation\n" +
                "}"
        assertEquals("Step function should match", expected, result?.text)
    }
}
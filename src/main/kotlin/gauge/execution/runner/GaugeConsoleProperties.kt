package gauge.execution.runner

import com.intellij.execution.Executor
import com.intellij.execution.PsiLocation
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.testframework.TestConsoleProperties
import com.intellij.execution.testframework.actions.AbstractRerunFailedTestsAction
import com.intellij.execution.testframework.sm.SMCustomMessagesParsing
import com.intellij.execution.testframework.sm.runner.OutputToGeneralTestEventsConverter
import com.intellij.execution.testframework.sm.runner.SMTRunnerConsoleProperties
import com.intellij.execution.testframework.sm.runner.SMTestLocator
import com.intellij.execution.ui.ConsoleView
import com.intellij.openapi.editor.Document
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import gauge.GaugeConstants
import gauge.execution.GaugeRunConfiguration

class GaugeConsoleProperties(
    config: GaugeRunConfiguration,
    gauge: String,
    executor: Executor,
    private val handler: ProcessHandler
) : SMTRunnerConsoleProperties(config, gauge, executor), SMCustomMessagesParsing {

    init {
        isIdBasedTestTree = true
        setIfUndefined(TestConsoleProperties.HIDE_PASSED_TESTS, false)
        setIfUndefined(TestConsoleProperties.HIDE_IGNORED_TEST, false)
        setIfUndefined(TestConsoleProperties.SCROLL_TO_SOURCE, true)
        setIfUndefined(TestConsoleProperties.SHOW_INLINE_STATISTICS, false)
        setIfUndefined(TestConsoleProperties.SHOW_STATISTICS, false)
    }

    override fun createTestEventsConverter(
        testFrameworkName: String,
        consoleProperties: TestConsoleProperties
    ): OutputToGeneralTestEventsConverter {
        return GaugeOutputToGeneralTestEventsProcessor(testFrameworkName, consoleProperties, handler)
    }

    override fun createRerunFailedTestsAction(consoleView: ConsoleView): AbstractRerunFailedTestsAction {
        return GaugeRerunFailedAction(consoleView).apply { init(this@GaugeConsoleProperties) }
    }

    override fun isIdBasedTestTree(): Boolean = true

    override fun getTestLocator(): SMTestLocator {
        return SMTestLocator { _, path, project, _ ->
            try {
                val fileInfo = path.split(GaugeConstants.SPEC_SCENARIO_DELIMITER)
                val file: VirtualFile = LocalFileSystem.getInstance().findFileByPath(fileInfo[0]) ?: return@SMTestLocator emptyList()
                val psiFile: PsiFile = PsiManager.getInstance(project).findFile(file) ?: return@SMTestLocator emptyList()
                val document: Document = PsiDocumentManager.getInstance(project).getDocument(psiFile) ?: return@SMTestLocator emptyList()
                val line = fileInfo[1].toInt()
                val element: PsiElement = psiFile.findElementAt(document.getLineStartOffset(line)) ?: return@SMTestLocator emptyList()
                listOf(PsiLocation(element))
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}

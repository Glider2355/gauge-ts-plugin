package gauge.execution

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import gauge.setting.PluginSettings
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

class CommandLineBuilderTest {
     private lateinit var mockProject: Project
     private lateinit var mockPluginSettings: PluginSettings

     @BeforeEach
     fun setUp() {
         mockProject = mockk(relaxed = true)
         mockPluginSettings = mockk(relaxed = true)

         // project.service<PluginSettings>() が mockPluginSettings を返すように設定
         every { mockProject.service<PluginSettings>() } returns mockPluginSettings
     }

     @AfterEach
     fun tearDown() {
         unmockkAll()
     }
@Test
 fun buildCommandLine() {
    // PluginSettings の設定をモック化
    every { mockPluginSettings.gaugeBinaryPath } returns "/path/to/gauge"
    every { mockPluginSettings.gaugeHomePath } returns "/path/to/home/.gauge"

    val commandLine = GaugeCommandLine.getInstance(mockProject)
    val commandLineBuilder = CommandLineBuilder()
    val buildCommandLine = commandLineBuilder.buildCommandLine(
        commandLine,
        "specs",
        "environment",
        "tags",
        3,
        "rowsRange"
    )

    assertEquals("/path/to/gauge", buildCommandLine.exePath)
    assertEquals("/path/to/home/.gauge", buildCommandLine.environment["GAUGE_HOME"])

    val expected = listOf(
        "run",
        "--machine-readable",
        "--hide-suggestion",
        "--tags",
        "tags",
        "--env",
        "environment",
        "--table-rows",
        "rowsRange",
        "--parallel",
        "-n=3",
        "specs"
    )
    assertEquals(expected, commandLine.parametersList.parameters)
 }
}
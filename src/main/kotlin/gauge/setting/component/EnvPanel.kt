package gauge.setting.component

import com.intellij.util.ui.JBUI
import java.awt.GridLayout
import javax.swing.BoxLayout
import javax.swing.JCheckBox
import javax.swing.JPanel
import javax.swing.JTextField

class EnvPanel : JPanel() {
    private val enableEnvCheckbox = JCheckBox("--env: ")
    private val envTextField = JTextField()
    private val enableEnvVarCheckbox = JCheckBox("Environment Variables: ")
    private val envVarTextField = JTextField()

    init {
        layout = GridLayout(2, 1)
        val envPanel = JPanel()
        envPanel.layout = BoxLayout(envPanel, BoxLayout.X_AXIS)
        envPanel.border = JBUI.Borders.empty(10, 0)
        envPanel.add(enableEnvCheckbox)
        envPanel.add(envTextField)
        envTextField.isEnabled = false
        enableEnvCheckbox.addActionListener {
            envTextField.isEnabled = enableEnvCheckbox.isSelected
        }

        val envVarPanel = JPanel()
        envVarPanel.layout = BoxLayout(envVarPanel, BoxLayout.X_AXIS)
        envVarPanel.border = JBUI.Borders.empty(10, 0)
        envVarPanel.add(enableEnvVarCheckbox)
        envVarPanel.add(envVarTextField)
        envVarTextField.isEnabled = false
        enableEnvVarCheckbox.addActionListener {
            envVarTextField.isEnabled = enableEnvVarCheckbox.isSelected
        }

        add(envPanel)
        add(envVarPanel)
    }

    fun getEnv(): String = envTextField.text
    fun setEnv(value: String) {
        envTextField.text = value
    }

    fun isEnvEnabled(): Boolean = enableEnvCheckbox.isSelected
    fun setEnvEnabled(enabled: Boolean) {
        enableEnvCheckbox.isSelected = enabled
        envTextField.isEnabled = enabled
    }

    fun getEnvVar(): String = envVarTextField.text
    fun setEnvVar(value: String) {
        envVarTextField.text = value
    }

    fun isEnvVarEnabled(): Boolean = enableEnvVarCheckbox.isSelected
    fun setEnvVarEnabled(enabled: Boolean) {
        enableEnvVarCheckbox.isSelected = enabled
        envVarTextField.isEnabled = enabled
    }
}

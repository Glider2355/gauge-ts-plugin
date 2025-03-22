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

    fun getEnableEnv(): Boolean = enableEnvCheckbox.isSelected
    fun getEnvValue(): String = envTextField.text
    fun getEnableEnvVar(): Boolean = enableEnvVarCheckbox.isSelected
    fun getEnvVarValue(): String = envVarTextField.text
    fun setEnableEnv(value: Boolean) {
        enableEnvCheckbox.isSelected = value
        envTextField.isEnabled = value
    }
    fun setEnvValue(value: String) {
        envTextField.text = value
    }
    fun setEnableEnvVar(value: Boolean) {
        enableEnvVarCheckbox.isSelected = value
        envVarTextField.isEnabled = value
    }
    fun setEnvVarValue(value: String) {
        envVarTextField.text = value
    }
}

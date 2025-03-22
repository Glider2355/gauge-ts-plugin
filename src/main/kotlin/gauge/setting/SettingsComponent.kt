package gauge.setting

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.ui.TextBrowseFolderListener
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBList
import com.intellij.util.ui.JBUI
import gauge.setting.component.EnvPanel
import javax.swing.*
import java.awt.BorderLayout
import java.awt.GridLayout

/**
 * https://plugins.jetbrains.com/docs/intellij/settings-tutorial.html#creating-the-appsettingscomponent-implementation
 */

class SettingsComponent {
    val mainPanel: JPanel = JPanel()
    val directoryListModel = DefaultListModel<DirectoryItem>()
    val directoryList = JBList(directoryListModel)
    val addButton = JButton("Add Directory")
    val removeButton = JButton("Remove Selected Directory")
    private val gaugeBinaryPathField = TextFieldWithBrowseButton()
    private val gaugeHomePathField = TextFieldWithBrowseButton()

    private val enableEnvCheckbox = JCheckBox("--env")
    private val envTextField = JTextField()
    private val enableEnvVarCheckbox = JCheckBox("Environment Variables")
    private val envVarTextField = JTextField()

    private val parallelNodesSpinner = JSpinner(
        SpinnerNumberModel(
            1,   // 初期値
            1,   // 最小値
            10,  // 最大値 (必要に応じて変更)
            1    // ステップ
        )
    )

    init {
        // Gauge Binary Pathの設定フィールド
        val binaryPathPanel = JPanel(BorderLayout())
        binaryPathPanel.border = JBUI.Borders.empty(10, 0)
        binaryPathPanel.add(JLabel("Gauge Binary Path:"), BorderLayout.WEST)
        binaryPathPanel.add(gaugeBinaryPathField, BorderLayout.CENTER)

        val parallelExecPanel = JPanel(BorderLayout())
        parallelExecPanel.border = JBUI.Borders.empty(10, 0)
        parallelExecPanel.add(JLabel("Max Parallel Nodes:"), BorderLayout.WEST)
        parallelExecPanel.add(parallelNodesSpinner, BorderLayout.CENTER)

        // フォルダ選択の設定
        gaugeBinaryPathField.addBrowseFolderListener(
            TextBrowseFolderListener(
                FileChooserDescriptorFactory.createSingleFileDescriptor()
                    .withTitle("Select Gauge Binary")
                    .withDescription("Choose the Gauge binary file")
            )
        )

        // GAUGE_HOME Pathの設定フィールド
        val homePathPanel = JPanel(BorderLayout())
        homePathPanel.border = JBUI.Borders.empty(10, 0)
        homePathPanel.add(JLabel("GAUGE_HOME Path:"), BorderLayout.WEST)
        homePathPanel.add(gaugeHomePathField, BorderLayout.CENTER)

        // フォルダ選択の設定
        gaugeHomePathField.addBrowseFolderListener(
            TextBrowseFolderListener(
                FileChooserDescriptorFactory.createSingleFolderDescriptor()
                    .withTitle("Select GAUGE_HOME")
                    .withDescription("Choose the Gauge home directory")
            )
        )

        // ディレクトリ一覧の作成
        val listPanel = JPanel(BorderLayout())
        listPanel.border = JBUI.Borders.empty(10, 0)
        listPanel.add(JLabel("Gauge Step Directories:"), BorderLayout.NORTH)
        directoryList.cellRenderer = DirectoryItemRenderer()
        directoryList.visibleRowCount = 10
        listPanel.add(JScrollPane(directoryList), BorderLayout.CENTER)

        // ディレクトリの追加・削除ボタンを作成
        val buttonPanel = JPanel(GridLayout(1, 2, 10, 0))
        buttonPanel.border = JBUI.Borders.empty(10, 0)
        buttonPanel.add(removeButton)
        buttonPanel.add(addButton)

        // メインパネルに各コンポーネントを追加
        val inputPanel = JPanel()
        inputPanel.layout = BoxLayout(inputPanel, BoxLayout.Y_AXIS)
        inputPanel.add(binaryPathPanel)
        inputPanel.add(homePathPanel)
        inputPanel.add(parallelExecPanel)

        val settingsPanel = JPanel()
        settingsPanel.layout = BoxLayout(settingsPanel, BoxLayout.Y_AXIS)
        settingsPanel.add(inputPanel)
        val envSettingsPanel = EnvPanel()
        settingsPanel.add(envSettingsPanel)

        mainPanel.layout = BoxLayout(mainPanel, BoxLayout.Y_AXIS)
        mainPanel.add(settingsPanel)
        mainPanel.add(listPanel)
        mainPanel.add(buttonPanel)
    }

    // Gauge Binary Pathを取得
    fun getGaugeBinaryPath(): String {
        return gaugeBinaryPathField.text
    }

    // Gauge Binary Pathを設定
    fun setGaugeBinaryPath(path: String) {
        gaugeBinaryPathField.text = path
    }

    // GAUGE_HOME Pathを取得
    fun getGaugeHomePath(): String {
        return gaugeHomePathField.text
    }

    // GAUGE_HOME Pathを設定
    fun setGaugeHomePath(path: String) {
        gaugeHomePathField.text = path
    }

    fun getParallelNode(): Int {
        return (parallelNodesSpinner.value as? Int) ?: 1
    }

    fun setParallelNode(value: Int) {
        parallelNodesSpinner.value = value
    }

    fun getEnableEnv(): Boolean {
        return enableEnvCheckbox.isSelected
    }

    fun setEnableEnv(value: Boolean) {
        enableEnvCheckbox.isSelected = value
    }

    fun getEnv(): String {
        return envTextField.text
    }

    fun setEnv(value: String) {
        envTextField.text = value
    }

    // ディレクトリ一覧を取得
    fun getDirectories(): List<DirectoryItem> {
        return directoryListModel.elements().toList()
    }

    // ディレクトリ一覧を設定
    fun setDirectories(directories: List<DirectoryItem>) {
        directoryListModel.clear()
        directories.forEach { directoryListModel.addElement(it) }
    }

    // ディレクトリを追加
    fun addDirectory(directory: String) {
        if (!directoryListModel.elements().toList().any { it.path == directory }) {
            directoryListModel.addElement(DirectoryItem(directory, true))
        }
    }

    // 選択されたディレクトリを削除
    fun removeSelectedDirectories() {
        val selectedIndices = directoryList.selectedIndices
        for (i in selectedIndices.reversed()) {
            directoryListModel.remove(i)
        }
    }
}

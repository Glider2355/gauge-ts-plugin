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

    private val parallelNodesSpinner = JSpinner(
        SpinnerNumberModel(
            1,   // 初期値
            1,   // 最小値
            10,  // 最大値
            1    // ステップ
        )
    )
    private val envSettingsPanel = EnvPanel()

    // Step 検索モード切替 (AUTO / MANUAL)
    private val autoScanRadio = JRadioButton("Auto scan (search all .ts in project)")
    private val manualRadio = JRadioButton("Manual (use directories listed below)")
    private val useGaugeRootCheckBox = JCheckBox("Restrict scope to `.gauge/` project roots")

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

        // Step 検索モードのラジオ + サブオプション
        val scanGroup = ButtonGroup()
        scanGroup.add(autoScanRadio)
        scanGroup.add(manualRadio)
        val gaugeRootIndent = JPanel(BorderLayout())
        gaugeRootIndent.border = JBUI.Borders.emptyLeft(24)
        gaugeRootIndent.add(useGaugeRootCheckBox, BorderLayout.WEST)

        val scanModePanel = JPanel()
        scanModePanel.layout = BoxLayout(scanModePanel, BoxLayout.Y_AXIS)
        scanModePanel.border = JBUI.Borders.compound(
            JBUI.Borders.customLine(java.awt.Color.GRAY, 1, 0, 0, 0),
            JBUI.Borders.empty(10, 0)
        )
        scanModePanel.add(JLabel("Step Scan Mode:"))
        scanModePanel.add(autoScanRadio)
        scanModePanel.add(gaugeRootIndent)
        scanModePanel.add(manualRadio)

        val onModeChanged = {
            val isManual = manualRadio.isSelected
            useGaugeRootCheckBox.isEnabled = !isManual
            directoryList.isEnabled = isManual
            addButton.isEnabled = isManual
            removeButton.isEnabled = isManual
        }
        autoScanRadio.addActionListener { onModeChanged() }
        manualRadio.addActionListener { onModeChanged() }

        // メインパネルに各コンポーネントを追加
        val inputPanel = JPanel()
        inputPanel.layout = BoxLayout(inputPanel, BoxLayout.Y_AXIS)
        inputPanel.add(binaryPathPanel)
        inputPanel.add(homePathPanel)
        inputPanel.add(parallelExecPanel)

        val settingsPanel = JPanel()
        settingsPanel.layout = BoxLayout(settingsPanel, BoxLayout.Y_AXIS)
        settingsPanel.add(inputPanel)
        settingsPanel.add(envSettingsPanel)

        mainPanel.layout = BoxLayout(mainPanel, BoxLayout.Y_AXIS)
        mainPanel.add(settingsPanel)
        mainPanel.add(scanModePanel)
        mainPanel.add(listPanel)
        mainPanel.add(buttonPanel)
    }

    fun getScanMode(): String =
        if (autoScanRadio.isSelected) PluginSettings.ScanMode.AUTO else PluginSettings.ScanMode.MANUAL

    fun setScanMode(mode: String) {
        val isAuto = mode == PluginSettings.ScanMode.AUTO
        autoScanRadio.isSelected = isAuto
        manualRadio.isSelected = !isAuto
        useGaugeRootCheckBox.isEnabled = isAuto
        directoryList.isEnabled = !isAuto
        addButton.isEnabled = !isAuto
        removeButton.isEnabled = !isAuto
    }

    fun getUseGaugeRootScope(): Boolean = useGaugeRootCheckBox.isSelected
    fun setUseGaugeRootScope(value: Boolean) {
        useGaugeRootCheckBox.isSelected = value
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
        return envSettingsPanel.getEnableEnv()
    }
    fun setEnableEnv(value: Boolean) {
        envSettingsPanel.setEnableEnv(value)
    }

    fun getEnvValue(): String {
        return envSettingsPanel.getEnvValue()
    }
    fun setEnvValue(value: String) {
        envSettingsPanel.setEnvValue(value)
    }

    fun getEnableEnvVar(): Boolean {
        return envSettingsPanel.getEnableEnvVar()
    }
    fun setEnableEnvVar(value: Boolean) {
        envSettingsPanel.setEnableEnvVar(value)
    }

    fun getEnvVarValue(): String {
        return envSettingsPanel.getEnvVarValue()
    }
    fun setEnvVarValue(value: String) {
        envSettingsPanel.setEnvVarValue(value)
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

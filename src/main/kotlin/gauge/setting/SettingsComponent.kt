package gauge.setting

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.ui.TextBrowseFolderListener
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBList
import com.intellij.util.ui.JBUI
import javax.swing.*
import java.awt.BorderLayout
import java.awt.Dimension

/**
 * https://plugins.jetbrains.com/docs/intellij/settings-tutorial.html#creating-the-appsettingscomponent-implementation
 */

class SettingsComponent {
    val mainPanel: JPanel = JPanel(BorderLayout())
    val directoryListModel = DefaultListModel<DirectoryItem>()
    val directoryList = JBList(directoryListModel)
    val addButton = JButton("Add Directory")
    val removeButton = JButton("Remove Selected Directory")
    private val gaugeBinaryPathField = TextFieldWithBrowseButton()
    private val gaugeHomePathField = TextFieldWithBrowseButton()

    init {
        // メインパネルのボーダー設定
        mainPanel.border = JBUI.Borders.empty(10)

        // Gauge Binary Pathの設定フィールド
        val binaryPathPanel = JPanel(BorderLayout())
        binaryPathPanel.border = JBUI.Borders.empty(10, 0)
        binaryPathPanel.add(JLabel("Gauge Binary Path:"), BorderLayout.WEST)
        binaryPathPanel.add(gaugeBinaryPathField, BorderLayout.CENTER)

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
        val buttonPanel = JPanel()
        buttonPanel.layout = BoxLayout(buttonPanel, BoxLayout.Y_AXIS)
        buttonPanel.border = JBUI.Borders.empty(10, 0)
        addButton.maximumSize = Dimension(Int.MAX_VALUE, addButton.preferredSize.height)
        removeButton.maximumSize = Dimension(Int.MAX_VALUE, removeButton.preferredSize.height)
        buttonPanel.add(addButton)
        buttonPanel.add(Box.createRigidArea(Dimension(0, 5)))
        buttonPanel.add(removeButton)

        // メインパネルに各コンポーネントを追加
        val inputPanel = JPanel(BorderLayout())
        inputPanel.add(binaryPathPanel, BorderLayout.NORTH)
        inputPanel.add(homePathPanel, BorderLayout.SOUTH)
        mainPanel.add(inputPanel, BorderLayout.NORTH)
        mainPanel.add(listPanel, BorderLayout.CENTER)
        mainPanel.add(buttonPanel, BorderLayout.SOUTH)
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

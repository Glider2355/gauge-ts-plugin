package gauge.setting

import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

class SettingsListener(
    private val settingsComponent: SettingsComponent,
    private val project: Project
) {
    init {
        settingsComponent.addButton.addActionListener(AddButtonListener())
        settingsComponent.removeButton.addActionListener(RemoveButtonListener())
        settingsComponent.directoryList.addMouseListener(CheckboxListener())
    }

    // フォルダ追加 (IntelliJ 標準の FileChooser + プロジェクトルート起点)
    inner class AddButtonListener : ActionListener {
        override fun actionPerformed(e: ActionEvent?) {
            val descriptor = FileChooserDescriptorFactory.createMultipleFoldersDescriptor()
                .withTitle("Select Gauge Step Directory")
                .withDescription("Choose one or more directories containing Gauge step implementations")
            val toSelect = project.guessProjectDir()
            val chosen = FileChooser.chooseFiles(descriptor, project, toSelect)
            for (dir in chosen) {
                settingsComponent.addDirectory(dir.path)
            }
        }
    }

    // フォルダ削除
    inner class RemoveButtonListener : ActionListener {
        override fun actionPerformed(e: ActionEvent?) {
            settingsComponent.removeSelectedDirectories()
        }
    }

    // チェックボックスのクリックイベントを処理
    inner class CheckboxListener : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent?) {
            // 無効化された Swing コンポーネントにもマウスイベントは届くので、AUTO モード中はチェックを変更させない
            if (!settingsComponent.directoryList.isEnabled) return
            val index = settingsComponent.directoryList.locationToIndex(e?.point)
            if (index >= 0) {
                val item = settingsComponent.directoryListModel.getElementAt(index)
                item.isChecked = !item.isChecked
                settingsComponent.directoryList.repaint(settingsComponent.directoryList.getCellBounds(index, index))
            }
        }
    }
}

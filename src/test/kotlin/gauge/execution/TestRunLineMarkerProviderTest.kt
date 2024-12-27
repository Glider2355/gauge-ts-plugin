package gauge.execution

import com.intellij.execution.lineMarker.RunLineMarkerContributor.Info
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.tree.IElementType
import gauge.language.token.SpecTokenTypes
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TestRunLineMarkerProviderTest {

    private lateinit var provider: TestRunLineMarkerProvider

    @BeforeEach
    fun setUp() {
        // 実際の ExecutorAction 呼び出しをしないようにサブクラス化
        provider = object : TestRunLineMarkerProvider() {
            // これでテスト時は IDE のサービスを呼ばずに済む
            override fun getExecutorActions(): Array<AnAction> = emptyArray()
        }
    }

    @Test
    fun `拡張子がspecでelementがLeafPsiElementかつSPEC_HEADINGのときInfoを返す`() {
        // Mock の準備
        val mockElement = mockk<LeafPsiElement>(relaxed = true)
        val mockFile = mockk<PsiFile>(relaxed = true)
        val mockVirtualFile = mockk<VirtualFile>(relaxed = true)

        // 拡張子を "spec" に設定
        every { mockVirtualFile.extension } returns "spec"
        every { mockFile.virtualFile } returns mockVirtualFile
        every { mockElement.containingFile } returns mockFile

        // elementType を SPEC_HEADING に設定
        every { mockElement.elementType } returns SpecTokenTypes.SPEC_HEADING

        // テスト実行
        val info: Info? = provider.getInfo(mockElement)

        // 検証
        assertNotNull(info, "Info オブジェクトが返るはず")
        assertEquals(AllIcons.RunConfigurations.TestState.Run, info?.icon, "Icon が想定通りか")
    }

    @Test
    fun `拡張子がspecでない場合nullを返す`() {
        val mockElement = mockk<LeafPsiElement>(relaxed = true)
        val mockFile = mockk<PsiFile>(relaxed = true)
        val mockVirtualFile = mockk<VirtualFile>(relaxed = true)

        // 拡張子を "txt" に設定
        every { mockVirtualFile.extension } returns "txt"
        every { mockFile.virtualFile } returns mockVirtualFile
        every { mockElement.containingFile } returns mockFile
        // elementType は SPEC_HEADING にしておく
        every { mockElement.elementType } returns SpecTokenTypes.SPEC_HEADING

        val info: Info? = provider.getInfo(mockElement)
        assertNull(info, "拡張子が spec でない場合は null")
    }

    @Test
    fun `LeafPsiElementでもelementTypeが対象外ならnullを返す`() {
        val mockElement = mockk<LeafPsiElement>(relaxed = true)
        val mockFile = mockk<PsiFile>(relaxed = true)
        val mockVirtualFile = mockk<VirtualFile>(relaxed = true)
        val mockType = mockk<IElementType>(relaxed = true)

        every { mockVirtualFile.extension } returns "spec"
        every { mockFile.virtualFile } returns mockVirtualFile
        every { mockElement.containingFile } returns mockFile

        // SPEC_HEADING でも SCENARIO_HEADING でもないトークンを設定
        every { mockElement.elementType } returns mockType

        val info: Info? = provider.getInfo(mockElement)
        assertNull(info, "elementType が対象外なら null")
    }

    @Test
    fun `elementがLeafPsiElementでない場合nullを返す`() {
        val mockElement = mockk<PsiElement>(relaxed = true)
        val mockFile = mockk<PsiFile>(relaxed = true)
        val mockVirtualFile = mockk<VirtualFile>(relaxed = true)

        every { mockVirtualFile.extension } returns "spec"
        every { mockFile.virtualFile } returns mockVirtualFile
        every { mockElement.containingFile } returns mockFile

        val info: Info? = provider.getInfo(mockElement)
        assertNull(info, "LeafPsiElement でなければ null")
    }
}

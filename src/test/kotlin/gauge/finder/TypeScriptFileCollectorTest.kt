package gauge.finder

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class TypeScriptFileCollectorTest : BasePlatformTestCase() {

    fun `test collect typescript files`() {
        // テスト用のTypeScriptファイルを作成
        val tsFile = myFixture.addFileToProject(
            "test/dir/sample.ts",
            "// TypeScript content"
        )

        // 比較のためのJavaScriptファイルも作成（これは収集されないはず）
        myFixture.addFileToProject(
            "test/dir/sample.js",
            "// JavaScript content"
        )

        // インデックス更新を確実にする
        myFixture.allowTreeAccessForAllFiles()

        // 親ディレクトリのパスを取得
        val dirPath = tsFile.virtualFile.parent.path
        println("テストディレクトリパス: $dirPath")

        // TypeScriptFileCollectorをテスト
        val collector = TypeScriptFileCollector()
        val files = collector.collectTypeScriptFiles(project, dirPath)

        // 結果を出力
        println("見つかったファイル数: ${files.size}")
        files.forEach { println("- ${it.name} (${it.virtualFile.path})") }

        // 検証
        assertEquals("TypeScriptファイルが1つ見つかるべき", 1, files.size)
        assertEquals("sample.ts", files[0].name)
    }
}
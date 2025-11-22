# Testing Strategy for gauge-ts-plugin

## 現状分析

### プロジェクト概要
- **プロジェクト名**: gauge-ts-plugin
- **目的**: IntelliJ IDEA用のGaugeテストプラグイン（TypeScriptステップサポート）
- **主な機能**: オートコンプリート、テスト実行、コードジャンプ

### テストカバレッジの現状

```
メインソースファイル: 59ファイル
テストファイル: 3ファイル
カバレッジ: 極めて低い（約5%）
```

**既存テスト:**
1. `CommandLineBuilderTest.kt` - コマンドライン構築のユニットテスト
2. `TestRunLineMarkerProviderTest.kt` - ラインマーカー提供のテスト
3. `StepTextProcessorTest.kt` - Step定義テキスト処理のテスト

---

## テストを書きにくい主な課題

### 1. IntelliJ Platform APIへの強い依存

**問題点:**
- `Project`, `PsiElement`, `VirtualFile`, `ExecutionEnvironment` などの複雑なインターフェース
- これらのオブジェクトは実際のIDE環境でしか正しく機能しない
- モック化が非常に困難かつ脆弱

**影響を受けるクラス:**
- `StepAnnotationsFinder` (src/main/kotlin/gauge/finder/StepAnnotationsFinder.kt:15)
- `GaugeTestRunner` (src/main/kotlin/gauge/execution/GaugeTestRunner.kt:27)
- `GaugeCommandLineState` (src/main/kotlin/gauge/execution/GaugeCommandLineState.kt:36)
- `TestRunLineMarkerProvider` (src/main/kotlin/gauge/execution/TestRunLineMarkerProvider.kt)

### 2. ファイルシステムとの密結合

**問題点:**
- `LocalFileSystem.getInstance()` などのシングルトンへの直接アクセス
- テスト時にファイルシステムのモックが困難
- テストデータの準備とクリーンアップが複雑

**影響を受けるクラス:**
- `StepAnnotationsFinder` (src/main/kotlin/gauge/finder/StepAnnotationsFinder.kt:25)
- `TypeScriptFileCollector`

### 3. 実行環境への依存

**問題点:**
- プロセス実行、コンソール表示などの実行時環境に強く依存
- `ProcessHandler`, `ExecutionResult` などの実行フレームワーク
- IDE環境なしではテストできない

**影響を受けるクラス:**
- `GaugeTestRunner` (src/main/kotlin/gauge/execution/GaugeTestRunner.kt)
- `GaugeRunProcessHandler`
- `GaugeCommandLineState` (src/main/kotlin/gauge/execution/GaugeCommandLineState.kt)

### 4. UIコンポーネントへの依存

**問題点:**
- Swingコンポーネントの直接操作
- UIロジックとビジネスロジックの混在
- UIテストフレームワークが必要

**影響を受けるクラス:**
- `SettingsComponent` (src/main/kotlin/gauge/setting/component/SettingsComponent.kt)
- `SettingsConfigurable` (src/main/kotlin/gauge/setting/SettingsConfigurable.kt:12)

### 5. イベント処理の複雑性

**問題点:**
- 非同期イベント処理の連鎖
- 状態管理の複雑性
- イベント順序への依存

**影響を受けるクラス:**
- `GaugeEventProcessor` (src/main/kotlin/gauge/execution/runner/processors/GaugeEventProcessor.kt:23)
- `ScenarioEventProcessor`
- `SpecEventProcessor`
- `SuiteEventProcessor`

### 6. サービスとシングルトンへの依存

**問題点:**
- `project.service<PluginSettings>()` などのサービス直接参照
- DI的な設計になっていない
- テスト時にサービスの置き換えが困難

**影響を受けるクラス:**
- `GaugeCommandLine` (src/main/kotlin/gauge/execution/GaugeCommandLine.kt)
- `GaugeRunConfiguration` (src/main/kotlin/gauge/execution/GaugeRunConfiguration.kt:44)
- `SettingsConfigurable` (src/main/kotlin/gauge/setting/SettingsConfigurable.kt:22)

---

## テスト戦略とソリューション

### 全体方針: ハイブリッド・段階的アプローチ

現実的かつ効果的なテスト戦略として、以下の3段階のアプローチを提案します。

```
Phase 1: Quick Wins (即効性)
    ↓
Phase 2: Architecture Improvement (中期改善)
    ↓
Phase 3: Integration Testing (長期的な品質保証)
```

---

## Phase 1: Quick Wins (すぐに実施可能)

### 1-1. Pure Functions の特定とテスト追加

**戦略:**
外部依存のない純粋関数を特定し、標準的なユニットテストを追加する。

**対象クラス:**
- ✅ `StepTextProcessor` (既にテストあり)
- ✅ `CommandLineBuilder` (既にテストあり)
- `TableInfo`
- イベント処理のロジック部分

**実装例:**

```kotlin
// src/test/kotlin/gauge/execution/EventProcessingLogicTest.kt
class EventProcessingLogicTest {
    @Test
    fun `イベントタイプがStartで終わる場合にtrueを返す`() {
        assertTrue(shouldProcessStartEvent("scenarioStart"))
        assertTrue(shouldProcessStartEvent("specStart"))
        assertFalse(shouldProcessStartEvent("scenarioEnd"))
    }
}
```

**メリット:**
- 🟢 実装が簡単
- 🟢 高速なテスト実行
- 🟢 即座にカバレッジ向上

**推奨優先度:** ⭐⭐⭐ 高

---

### 1-2. Mockk ベースのユニットテスト拡充

**戦略:**
既存のテストパターン（`CommandLineBuilderTest.kt`, `TestRunLineMarkerProviderTest.kt`）を踏襲し、
mockkライブラリを活用して他のクラスのテストを追加する。

**対象クラス:**
- `GaugeRunConfiguration`
- `GaugeCommandLine`
- `PluginSettings`

**実装例:**

```kotlin
// src/test/kotlin/gauge/execution/GaugeRunConfigurationTest.kt
class GaugeRunConfigurationTest {
    private lateinit var mockProject: Project
    private lateinit var mockFactory: ConfigurationFactory
    private lateinit var mockPluginSettings: PluginSettings

    @BeforeEach
    fun setUp() {
        mockProject = mockk(relaxed = true)
        mockFactory = mockk(relaxed = true)
        mockPluginSettings = mockk(relaxed = true)

        every { mockProject.service<PluginSettings>() } returns mockPluginSettings
    }

    @Test
    fun `specsArrayToExecuteが正しく結合される`() {
        val config = GaugeRunConfiguration(mockProject, mockFactory, "test")
        val specs = listOf("spec1.spec", "spec2.spec", "spec3.spec")

        config.setSpecsArrayToExecute(specs)

        assertEquals("spec1.spec || spec2.spec || spec3.spec", config.specs)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }
}
```

**メリット:**
- 🟢 既存パターンを活用
- 🟢 IntelliJ Platform環境不要
- 🟡 モックのメンテナンスコスト

**推奨優先度:** ⭐⭐ 中

---

### 1-3. ビジネスロジックの抽出とテスト

**戦略:**
既存のクラスからビジネスロジックを抽出し、テスト可能な形にリファクタリングする。

**実装例:**

```kotlin
// Before: テストしにくい
class GaugeEventProcessor(...) {
    override fun process(event: ExecutionEvent): Boolean {
        return if (event.type.endsWith("Start")) onStart(event) else onEnd(event)
    }
}

// After: ロジックを抽出
object EventTypeChecker {
    fun isStartEvent(eventType: String): Boolean = eventType.endsWith("Start")
    fun isEndEvent(eventType: String): Boolean = eventType.endsWith("End")
}

// テストが簡単に
class EventTypeCheckerTest {
    @Test
    fun isStartEvent() {
        assertTrue(EventTypeChecker.isStartEvent("scenarioStart"))
        assertFalse(EventTypeChecker.isStartEvent("scenarioEnd"))
    }
}
```

**メリット:**
- 🟢 コードの再利用性向上
- 🟢 テストが簡単
- 🟢 ロジックの明確化

**推奨優先度:** ⭐⭐⭐ 高

---

## Phase 2: Architecture Improvement (中期改善)

### 2-1. インターフェースの導入とDI化

**戦略:**
外部依存をインターフェースで抽象化し、依存性注入パターンを導入する。

**実装例:**

```kotlin
// Step 1: インターフェースの定義
interface FileSystemAccess {
    fun findFileByPath(path: String): VirtualFile?
}

interface ProjectServiceAccess {
    fun <T : Any> getService(serviceClass: Class<T>): T
}

// Step 2: 本番実装
class IntelliJFileSystemAccess : FileSystemAccess {
    override fun findFileByPath(path: String): VirtualFile? {
        return LocalFileSystem.getInstance().findFileByPath(path)
    }
}

class IntelliJProjectServiceAccess(private val project: Project) : ProjectServiceAccess {
    override fun <T : Any> getService(serviceClass: Class<T>): T {
        return project.service()
    }
}

// Step 3: リファクタリング
class StepAnnotationsFinder(
    private val fileSystem: FileSystemAccess,
    private val typeScriptFileCollector: TypeScriptFileCollector
) {
    fun findStepAnnotations(project: Project, searchDirectories: List<String>): List<String> {
        val stepAnnotations = searchDirectories.flatMap {
            findStepAnnotationsByDirectoryPath(project, it)
        }.toSet()
        return stepAnnotations.toList()
    }

    private fun findStepAnnotationsByDirectoryPath(
        project: Project,
        directoryPath: String
    ): List<String> {
        val stepAnnotations = mutableListOf<String>()

        // ファイルシステムアクセスを抽象化
        val virtualFile = fileSystem.findFileByPath(directoryPath)
        val files = typeScriptFileCollector.collectTypeScriptFiles(project, virtualFile)

        for (file in files) {
            if (file is JSFile) {
                extractStepAnnotationsFromFile(file, stepAnnotations)
            }
        }
        return stepAnnotations
    }

    // ... 既存のメソッド
}

// Step 4: テスト用Fake実装
class FakeFileSystemAccess : FileSystemAccess {
    private val files = mutableMapOf<String, VirtualFile>()

    fun registerFile(path: String, file: VirtualFile) {
        files[path] = file
    }

    override fun findFileByPath(path: String): VirtualFile? = files[path]
}

// Step 5: テスト
class StepAnnotationsFinderTest {
    @Test
    fun `ディレクトリ内のステップアノテーションを取得できる`() {
        val fakeFileSystem = FakeFileSystemAccess()
        val mockVirtualFile = mockk<VirtualFile>()
        fakeFileSystem.registerFile("/test/steps", mockVirtualFile)

        val finder = StepAnnotationsFinder(
            fakeFileSystem,
            mockk(relaxed = true)
        )

        // テスト実行...
    }
}
```

**メリット:**
- 🟢 テスタビリティの大幅向上
- 🟢 依存関係の明確化
- 🟡 リファクタリングコストが大きい

**推奨優先度:** ⭐⭐ 中（長期的には重要）

---

### 2-2. Facade パターンによるIntelliJ API隔離

**戦略:**
IntelliJ Platform APIへのアクセスをFacadeクラスで隠蔽し、テスト時に置き換え可能にする。

**実装例:**

```kotlin
// Facadeインターフェース
interface IntelliJPlatformFacade {
    fun createRunContentDescriptor(
        console: ExecutionConsole?,
        processHandler: ProcessHandler?,
        component: Component?,
        displayName: String
    ): RunContentDescriptor

    fun showRunContent(
        project: Project,
        executor: Executor,
        descriptor: RunContentDescriptor
    )
}

// 本番実装
class DefaultIntelliJPlatformFacade : IntelliJPlatformFacade {
    override fun createRunContentDescriptor(
        console: ExecutionConsole?,
        processHandler: ProcessHandler?,
        component: Component?,
        displayName: String
    ): RunContentDescriptor {
        return RunContentDescriptor(console, processHandler, component, displayName)
    }

    override fun showRunContent(
        project: Project,
        executor: Executor,
        descriptor: RunContentDescriptor
    ) {
        RunContentManager.getInstance(project).showRunContent(executor, descriptor)
    }
}

// テスト用実装
class FakeIntelliJPlatformFacade : IntelliJPlatformFacade {
    var lastDescriptor: RunContentDescriptor? = null
    var showRunContentCalled = false

    override fun createRunContentDescriptor(
        console: ExecutionConsole?,
        processHandler: ProcessHandler?,
        component: Component?,
        displayName: String
    ): RunContentDescriptor {
        return mockk(relaxed = true).also { lastDescriptor = it }
    }

    override fun showRunContent(
        project: Project,
        executor: Executor,
        descriptor: RunContentDescriptor
    ) {
        showRunContentCalled = true
    }
}
```

**メリット:**
- 🟢 IntelliJ APIからの分離
- 🟢 テスト時の制御が容易
- 🟢 API変更の影響を局所化

**推奨優先度:** ⭐⭐ 中

---

### 2-3. レイヤー分離アーキテクチャ

**戦略:**
以下の3層アーキテクチャに分離する。

```
┌─────────────────────────────────┐
│  Presentation Layer             │  <- IntelliJ Platform API
│  (UI, Actions, Configurables)   │
└─────────────────────────────────┘
              ↓
┌─────────────────────────────────┐
│  Domain Layer                   │  <- ビジネスロジック（テスト容易）
│  (Core Logic, Rules)            │
└─────────────────────────────────┘
              ↓
┌─────────────────────────────────┐
│  Infrastructure Layer           │  <- 外部依存（抽象化）
│  (File I/O, Process Execution)  │
└─────────────────────────────────┘
```

**実装例:**

```kotlin
// Domain Layer - テストしやすい
data class StepDefinition(
    val text: String,
    val filePath: String,
    val lineNumber: Int
)

class StepMatcher {
    fun isMatch(stepAnnotationText: String, stepText: String): Boolean {
        // Pure function - 簡単にテスト可能
        return StepTextProcessor.isStepMatch(stepAnnotationText, stepText)
    }
}

// Infrastructure Layer - 抽象化
interface StepDefinitionRepository {
    fun findAll(directories: List<String>): List<StepDefinition>
}

class TypeScriptStepDefinitionRepository(
    private val fileSystem: FileSystemAccess
) : StepDefinitionRepository {
    override fun findAll(directories: List<String>): List<StepDefinition> {
        // ファイルシステムアクセス
    }
}

// Presentation Layer - 薄いアダプター
class StepCompletionProvider(
    private val repository: StepDefinitionRepository,
    private val matcher: StepMatcher
) : CompletionContributor() {
    // IntelliJ APIとDomain Layerの橋渡しのみ
}
```

**メリット:**
- 🟢 関心の分離
- 🟢 テスト戦略の明確化
- 🟢 保守性の向上
- 🔴 大規模なリファクタリングが必要

**推奨優先度:** ⭐ 低（大規模リファクタが必要）

---

## Phase 3: Integration Testing (長期的な品質保証)

### 3-1. IntelliJ Platform Test Framework の導入

**戦略:**
公式のIntelliJ Platform Test Frameworkを使用し、統合テストを実装する。

**使用するテストベースクラス:**

| テストベースクラス | 用途 | 起動時間 |
|---|---|---|
| `BasePlatformTestCase` | 基本的なテスト（推奨） | 中 |
| `LightJavaCodeInsightFixtureTestCase` | PSI関連の軽量テスト | 速い |
| `HeavyPlatformTestCase` | 完全なプロジェクト環境 | 遅い |

**実装例:**

```kotlin
// build.gradle.kts に追加
dependencies {
    testImplementation(intellijPlatform.testFramework(TestFrameworkType.Platform))
}

// src/test/kotlin/gauge/integration/StepAnnotationsFinderIntegrationTest.kt
class StepAnnotationsFinderIntegrationTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String {
        return "src/test/testData"
    }

    fun `test find step annotations from typescript file`() {
        // テストデータの準備
        val testFile = myFixture.copyFileToProject(
            "stepDefinitions.ts",
            "steps/stepDefinitions.ts"
        )

        // 実際のテスト
        val finder = StepAnnotationsFinder()
        val annotations = finder.findStepAnnotations(
            project,
            mutableListOf(testFile.parent.path)
        )

        // 検証
        assertTrue(annotations.contains("Get request <path>"))
        assertTrue(annotations.contains("Post request <path>"))
    }
}
```

**テストデータ例:**

```typescript
// src/test/testData/stepDefinitions.ts
import { Step } from "gauge-ts";

@Step("Get request <path>")
export function getRequest(path: string) {
    // implementation
}

@Step("Post request <path>")
export function postRequest(path: string) {
    // implementation
}
```

**メリット:**
- 🟢 実際のIntelliJ環境でテスト
- 🟢 PSI、VirtualFileなどが正しく動作
- 🟢 統合的な動作保証
- 🔴 テスト実行が遅い
- 🔴 セットアップが複雑

**推奨優先度:** ⭐⭐ 中（Phase 1, 2の後に実施）

---

### 3-2. E2Eテストの追加

**戦略:**
重要なユーザーシナリオに対して、エンドツーエンドのテストを実装する。

**テストシナリオ例:**

```kotlin
class GaugePluginE2ETest : BasePlatformTestCase() {

    fun `test complete user workflow - write spec and run test`() {
        // 1. プロジェクト設定
        val settings = project.service<PluginSettings>()
        settings.gaugeBinaryPath = "/usr/local/bin/gauge"
        settings.searchDirectories.add("${projectPath}/steps")

        // 2. Specファイルを開く
        val specFile = myFixture.copyFileToProject(
            "sample.spec",
            "specs/sample.spec"
        )
        myFixture.openFileInEditor(specFile)

        // 3. オートコンプリートが動作することを確認
        myFixture.type("* Get")
        val completions = myFixture.completeBasic()
        assertTrue(completions.any { it.lookupString == "Get request <path>" })

        // 4. Stepにジャンプできることを確認
        myFixture.performEditorAction(IdeActions.ACTION_GOTO_DECLARATION)
        val targetFile = myFixture.file
        assertTrue(targetFile.name.endsWith(".ts"))

        // 5. テスト実行マーカーが表示されることを確認
        val markers = getLineMarkers()
        assertTrue(markers.any { it.icon == AllIcons.RunConfigurations.TestState.Run })
    }
}
```

**メリット:**
- 🟢 実際のユーザー体験をテスト
- 🟢 リグレッション防止
- 🟢 機能の統合的な動作保証
- 🔴 テスト実行時間が長い
- 🔴 メンテナンスコストが高い

**推奨優先度:** ⭐ 低（Phase 1, 2の後に実施）

---

## テスト優先度マトリクス

### 優先度: 高 ⭐⭐⭐

| クラス/機能 | テスト方法 | Phase | 理由 |
|---|---|---|---|
| `StepTextProcessor` | ✅ ユニットテスト（既存） | - | 既にテストあり |
| `CommandLineBuilder` | ✅ ユニットテスト（既存） | - | 既にテストあり |
| `TableInfo` | ユニットテスト | 1 | Pure Function |
| イベント処理ロジック | ロジック抽出+ユニット | 1 | ビジネスロジック |
| `StepMatcher` | ユニットテスト | 1 | コア機能 |

### 優先度: 中 ⭐⭐

| クラス/機能 | テスト方法 | Phase | 理由 |
|---|---|---|---|
| `GaugeRunConfiguration` | Mockベーステスト | 1-2 | 設定管理の重要性 |
| `PluginSettings` | Mockベーステスト | 1-2 | 状態管理 |
| `StepAnnotationsFinder` | リファクタ+統合テスト | 2-3 | ファイルシステム依存 |
| `GaugeCommandLine` | Mockベーステスト | 1-2 | コマンド構築 |
| `TestRunLineMarkerProvider` | ✅ Mockテスト（既存） | - | 既にテストあり |

### 優先度: 低 ⭐

| クラス/機能 | テスト方法 | Phase | 理由 |
|---|---|---|---|
| `SettingsComponent` | UIテスト | 3 | UIは手動テストで十分 |
| `SettingsConfigurable` | UIテスト | 3 | UIは手動テストで十分 |
| `GaugeTestRunner` | E2Eテスト | 3 | 実行環境依存が強い |
| `GaugeCommandLineState` | E2Eテスト | 3 | 実行環境依存が強い |
| Event Processors系 | 統合/E2Eテスト | 3 | 複雑な統合テストが必要 |

---

## 具体的な実装計画

### Week 1-2: Phase 1 - Quick Wins

**目標:** テストカバレッジを15%以上に向上

```
✓ タスク1: Pure Functionのテスト追加
  - TableInfo のユニットテスト
  - イベント処理ロジックの抽出とテスト

✓ タスク2: Mockベーステスト追加
  - GaugeRunConfigurationTest
  - PluginSettingsTest
  - GaugeCommandLineTest

✓ タスク3: CI/CD統合
  - GitHub Actions でテスト自動実行
  - PRマージ時のテスト必須化
  - Koverカバレッジレポート生成
```

### Week 3-4: Phase 1 継続 + Phase 2 準備

**目標:** テストカバレッジを30%以上に向上

```
✓ タスク4: ビジネスロジック抽出
  - EventProcessingLogic の分離
  - StepMatcher の分離
  - 各ロジックのユニットテスト

✓ タスク5: リファクタリング計画策定
  - インターフェース設計
  - DI導入箇所の特定
  - 影響範囲の調査
```

### Month 2-3: Phase 2 - Architecture Improvement

**目標:** 主要クラスのテスタビリティ向上

```
✓ タスク6: インターフェース導入
  - FileSystemAccess インターフェース
  - ProjectServiceAccess インターフェース
  - 実装とFake/Mock実装

✓ タスク7: StepAnnotationsFinderリファクタ
  - インターフェース経由のアクセスに変更
  - テスト追加

✓ タスク8: Facade導入
  - IntelliJPlatformFacade 実装
  - 既存コードの移行
```

### Month 4+: Phase 3 - Integration Testing

**目標:** 統合テストとE2Eテストの追加

```
✓ タスク9: Platform Test Framework導入
  - テスト環境のセットアップ
  - テストデータの準備
  - StepAnnotationsFinder統合テスト

✓ タスク10: E2Eテスト実装
  - オートコンプリートのE2Eテスト
  - コードジャンプのE2Eテスト
  - テスト実行のE2Eテスト
```

---

## テストツールとフレームワーク

### 使用中のツール

| ツール | 用途 | バージョン |
|---|---|---|
| JUnit 5 | テストフレームワーク | 5.12.1 |
| JUnit 4 | レガシーサポート | 4.13.2 |
| MockK | Kotlinモックライブラリ | 1.14.6 |
| Kover | カバレッジ測定 | (Gradle Plugin) |

### 推奨追加ツール

| ツール | 用途 | 導入Phase |
|---|---|---|
| IntelliJ Platform Test Framework | 統合テスト | Phase 3 |
| Kotest | BDD/Property-based Testing | Phase 2-3 |
| AssertJ | Fluent Assertions | Phase 1 |

---

## ベストプラクティス

### 1. テストの命名規則

```kotlin
// ❌ 悪い例
@Test
fun test1() { }

// ✅ 良い例 - 日本語OK
@Test
fun `ディレクトリパスが空の場合は空のリストを返す`() { }

// ✅ 良い例 - 英語
@Test
fun `should return empty list when directory path is empty`() { }
```

### 2. Given-When-Then パターン

```kotlin
@Test
fun `複数のspecファイルを正しく結合する`() {
    // Given - テスト準備
    val config = GaugeRunConfiguration(mockProject, mockFactory, "test")
    val specs = listOf("spec1.spec", "spec2.spec")

    // When - テスト実行
    config.setSpecsArrayToExecute(specs)

    // Then - 検証
    assertEquals("spec1.spec || spec2.spec", config.specs)
}
```

### 3. テストデータのBuilder活用

```kotlin
// テストデータビルダー
class ExecutionEventBuilder {
    private var type: String = "scenarioStart"
    private var filename: String? = null
    private var line: Int? = null

    fun withType(type: String) = apply { this.type = type }
    fun withFilename(filename: String) = apply { this.filename = filename }
    fun withLine(line: Int) = apply { this.line = line }

    fun build() = ExecutionEvent(type, filename, line)
}

// 使用例
@Test
fun `イベント処理のテスト`() {
    val event = ExecutionEventBuilder()
        .withType("scenarioStart")
        .withFilename("test.spec")
        .withLine(10)
        .build()

    // テスト...
}
```

### 4. Mockのスコープ管理

```kotlin
class MyTest {
    private lateinit var mockProject: Project

    @BeforeEach
    fun setUp() {
        mockProject = mockk(relaxed = true)
        // セットアップ
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()  // 必ずクリーンアップ
    }

    @Test
    fun myTest() {
        // テスト
    }
}
```

### 5. ParameterizedTest の活用

```kotlin
@ParameterizedTest
@CsvSource(
    "'StepName', StepName",
    "\"StepName\", StepName",
    "['StepName1', 'StepName2'], StepName1"
)
fun `様々な入力形式でステップ名を抽出できる`(input: String, expected: String) {
    val result = StepTextProcessor.fixStepText(input)
    assertEquals(expected, result)
}
```

---

## CI/CD 統合

### GitHub Actions ワークフロー例

```yaml
# .github/workflows/test.yml
name: Test

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  test:
    runs-on: ubuntu-latest

    steps:
    - uses: actions/checkout@v3

    - name: Set up JDK 21
      uses: actions/setup-java@v3
      with:
        java-version: '21'
        distribution: 'temurin'

    - name: Grant execute permission for gradlew
      run: chmod +x gradlew

    - name: Run tests
      run: ./gradlew test

    - name: Generate coverage report
      run: ./gradlew koverReport

    - name: Upload coverage to Codecov
      uses: codecov/codecov-action@v3
      with:
        files: ./build/reports/kover/report.xml

    - name: Fail if coverage < 30%
      run: |
        # カバレッジチェックスクリプト
        ./scripts/check-coverage.sh 30
```

### カバレッジ目標

| フェーズ | 目標カバレッジ | 期限 |
|---|---|---|
| Phase 1 開始 | 5% (現状) | - |
| Phase 1 完了 | 30% | 1ヶ月後 |
| Phase 2 完了 | 50% | 3ヶ月後 |
| Phase 3 完了 | 70%+ | 6ヶ月後 |

---

## まとめ

### テストを書きにくい主な理由
1. IntelliJ Platform APIへの密結合
2. ファイルシステム・実行環境への依存
3. DI不足とシングルトン依存
4. ロジックとインフラの混在

### 推奨アプローチ
- ✅ **Phase 1 (即効性)**: Pure Functionとモックベーステストで Quick Wins
- ✅ **Phase 2 (中期)**: アーキテクチャ改善でテスタビリティ向上
- ✅ **Phase 3 (長期)**: 統合テストとE2Eテストで品質保証

### 次のアクション
1. `TableInfo` のユニットテストを作成する
2. `GaugeRunConfiguration` のモックベーステストを作成する
3. イベント処理ロジックを抽出して Pure Function にする
4. CI/CDにテストとカバレッジチェックを統合する

---

## 参考資料

- [IntelliJ Platform SDK - Testing](https://plugins.jetbrains.com/docs/intellij/testing-plugins.html)
- [MockK Documentation](https://mockk.io/)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Kotlin Test Best Practices](https://kotlinlang.org/docs/jvm-test-using-junit.html)
- [IntelliJ Platform Test Framework](https://plugins.jetbrains.com/docs/intellij/testing-plugins.html#tests-prerequisites)

---

最終更新: 2025-11-22
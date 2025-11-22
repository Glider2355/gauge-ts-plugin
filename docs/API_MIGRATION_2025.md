# IntelliJ Platform 2025.2 API Migration Guide

## 概要

IntelliJ Platform 2025.2 へのアップグレードに伴い、いくつかの API 変更と依存関係の変更が必要になりました。

## 発生したエラー

### 1. buildSearchableOptions タスクでの JVM クラッシュ

```
java.lang.ClassNotFoundException: kotlinx.coroutines.debug.AgentPremain
```

**原因**: IntelliJ Platform Gradle Plugin 2.3.0 のバグ

**解決策**:
- IntelliJ Platform Gradle Plugin を 2.10.4 にアップグレード
- Gradle を 8.13 以上にアップグレード（2.10.4 の要件）

### 2. RemoteConfiguration 関連の Unresolved reference エラー

```
e: Unresolved reference 'impl'.
e: Unresolved reference 'remote'.
e: Unresolved reference 'GenericDebuggerRunner'.
e: Unresolved reference 'RemoteConfigurationType'.
```

**原因**: IntelliJ Platform 2025.2 では、多くのモジュールがコアプラグインから分離され、独自のクラスローダーを持つようになりました。

**影響を受けるパッケージ**:
- `com.intellij.debugger.impl.*` - Java デバッガの実装
- `com.intellij.execution.remote.*` - リモート実行設定
- `com.intellij.execution.impl.*` - 実行設定の実装

これらは Java プラグインの一部として提供されます。

## 必要な修正

### 1. Gradle のアップグレード

**gradle.properties**:
```properties
gradleVersion = 8.13  # 8.9 から変更
```

実行コマンド:
```bash
./gradlew wrapper --gradle-version=8.13
```

### 2. IntelliJ Platform Gradle Plugin のアップグレード

**gradle/libs.versions.toml**:
```toml
intelliJPlatform = "2.10.4"  # 2.3.0 から変更
```

### 3. Java プラグインの依存関係を追加

**gradle.properties**:
```properties
platformBundledPlugins = JavaScript, com.intellij.java
```

**src/main/resources/META-INF/plugin.xml**:
```xml
<depends>com.intellij.modules.platform</depends>
<depends>com.intellij.java</depends>
<depends>JavaScript</depends>
```

重要: IntelliJ Platform 2025.2 以降では、`com.intellij.modules.java` ではなく `com.intellij.java` を使用します。

`com.intellij.java` を追加することで、以下のパッケージが利用可能になります:
- `com.intellij.debugger.impl.*`
- `com.intellij.execution.remote.*`
- `com.intellij.execution.impl.*`

### 4. 非推奨 API の削除

**build.gradle.kts**:

削除:
```kotlin
instrumentationTools()  // もはや不要
```

変更:
```kotlin
// 古い API
withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "21"
}

// 新しい API
withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}
```

## RemoteConfiguration API の使用方法

`com.intellij.execution.remote.RemoteConfiguration` クラスは IntelliJ Platform 2025.2 でも引き続き利用可能です。

### 利用可能なフィールド:
- `HOST: String` - デバッグサーバーのホスト（デフォルト: "localhost"）
- `PORT: String` - デバッグサーバーのポート（デフォルト: "5005"）
- `USE_SOCKET_TRANSPORT: boolean` - ソケット転送を使用（デフォルト: true）
- `SERVER_MODE: boolean` - サーバーモード
- `SHMEM_ADDRESS: String` - 共有メモリアドレス（デフォルト: "javadebug"）
- `AUTO_RESTART: boolean` - 自動再起動

### 使用例:

```kotlin
val configFactory: ConfigurationFactory =
    RemoteConfigurationType.getInstance().configurationFactories[0]
val remoteConfig = RemoteConfiguration(project, configFactory).apply {
    PORT = debugInfo.port
    HOST = debugInfo.host
    USE_SOCKET_TRANSPORT = true
    SERVER_MODE = false
}
```

## GenericDebuggerRunner について

`com.intellij.debugger.impl.GenericDebuggerRunner` は Java プラグインの一部として提供されます。

### 必要な import:
```kotlin
import com.intellij.debugger.impl.GenericDebuggerRunner
import com.intellij.execution.impl.RunManagerImpl
import com.intellij.execution.impl.RunnerAndConfigurationSettingsImpl
import com.intellij.execution.remote.RemoteConfiguration
import com.intellij.execution.remote.RemoteConfigurationType
```

これらすべてのクラスは `com.intellij.java` プラグインに依存します。

## 参考リンク

- [IntelliJ Platform 2025.* API Changes](https://plugins.jetbrains.com/docs/intellij/api-changes-list-2025.html)
- [IntelliJ Platform 2025.* Notable Changes](https://plugins.jetbrains.com/docs/intellij/api-notable-list-2025.html)
- [IntelliJ Platform Gradle Plugin 2.10.4](https://github.com/JetBrains/intellij-platform-gradle-plugin)
- [Module Extraction in 2025.2](https://plugins.jetbrains.com/docs/intellij/api-changes-list-2025.html#module-extraction)

## トラブルシューティング

### ビルドが遅い場合

Gradle のキャッシュをクリア:
```bash
./gradlew clean --configuration-cache
```

### 依存関係の問題

依存関係を再解決:
```bash
./gradlew --refresh-dependencies
```

### Gradle Daemon の問題

Daemon を停止して再起動:
```bash
./gradlew --stop
./gradlew build
```

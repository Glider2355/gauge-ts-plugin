# IntelliJ IDEA プラグイン互換性問題とJDKアップグレード

## 問題の概要

最新のIntelliJ IDEA（2024.3以降、現在の最新版は2025.3）では、このプラグインが動作しない可能性があります。
これは、IntelliJ Platform 2024.2以降でJDK 21が必須要件となったことが原因です。

## 現在の設定

- **JDKバージョン**: 17 (`build.gradle.kts:19`)
- **IntelliJ Platformバージョン**: 2024.3
- **サポート範囲**: 243 (2024.3) ～ 251.*

## JetBrainsの公式要件

JetBrainsの公式ドキュメント（[Creating a Plugin Gradle Project](https://plugins.jetbrains.com/docs/intellij/creating-plugin-project.html)）によると：

| IntelliJ Platform バージョン | 必須JDKバージョン |
|---------------------------|----------------|
| 2024.2以降（最新は2025.3） | **Java 21**    |
| 2022.3～2024.1            | Java 17        |

現在のプロジェクトはIntelliJ Platform 2024.3をターゲットにしているため、**Java 21が必須**です。
また、最新の2025.3でも同様にJava 21が要求されます。

## 根本原因の詳細分析

### 1. IntelliJ Platform側の変更

- IntelliJ IDEA 2024.2以降、すべての製品に**JetBrains Runtime 21**がバンドルされている
- プラットフォーム自体がJava 21で動作することを前提としている
- プラグイン開発でもJava 21の使用が必須要件となった

### 2. 後方互換性の制約

- Java 17でビルドされたプラグインは、Java 21を要求するプラットフォームでは動作しない可能性がある
- 特に、プラットフォームAPIの一部がJava 21の機能を使用している場合がある

### 3. Gradleプラグインの要件

- IntelliJ Platform Gradle Plugin 2.x系が推奨される
- 2024.2以降では、Gradle Plugin 2.xが**必須**

## 解決策

### ステップ1: JDK 21のインストール

まず、開発環境にJDK 21をインストールします。

**macOS（Homebrewを使用）:**
```bash
brew install openjdk@21
```

**その他のプラットフォーム:**
- [Oracle JDK 21](https://www.oracle.com/java/technologies/downloads/#java21)
- [Eclipse Temurin 21](https://adoptium.net/)
- [Amazon Corretto 21](https://aws.amazon.com/corretto/)

### ステップ2: build.gradle.ktsの更新

`build.gradle.kts`を以下のように変更します：

#### 変更前（現在）:
```kotlin
kotlin {
    jvmToolchain(17)
}
```

#### 変更後:
```kotlin
kotlin {
    jvmToolchain(21)
}
```

#### 追加の推奨設定

より明示的に設定する場合は、以下も追加することを推奨します：

```kotlin
tasks {
    withType<JavaCompile> {
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "21"
    }
}
```

### ステップ3: Gradle Wrapperの確認

Gradleバージョンが十分に新しいことを確認します：

```bash
./gradlew --version
```

現在の設定では Gradle 8.9 を使用しているため、問題ありません（Java 21をサポート）。

### ステップ4: ビルドとテスト

変更後、プラグインをビルドしてテストします：

```bash
./gradlew clean build
./gradlew runIde
```

## サポート範囲の調整（オプション）

もし古いIntelliJ IDEAバージョンもサポートしたい場合は、`gradle.properties`を調整できます：

### オプション1: 2024.2～2025.3をサポート（Java 21）
```properties
pluginSinceBuild = 242
pluginUntilBuild = 253.*
platformVersion = 2024.3  # または 2025.3
```

### オプション2: 2024.1までサポート（Java 17のまま）
```properties
pluginSinceBuild = 241
pluginUntilBuild = 241.*
platformVersion = 2024.1
```
※ただしこの場合、最新のIntelliJ IDEA 2024.3以降（2025.3含む）では動作しません

## 予想される影響

### ポジティブな影響
- 最新のIntelliJ IDEA 2024.3～2025.3で動作する
- 最新のプラットフォームAPIを使用できる
- パフォーマンスの向上（JDK 21の最適化）
- 2025.3の統合ディストリビューションに対応

### 潜在的な課題
- 開発者全員がJDK 21をインストールする必要がある
- 古いIntelliJ IDEAバージョン（2024.1以前）のサポートを継続する場合は、別ブランチの管理が必要

## 検証手順

1. **ローカル環境での検証**
   ```bash
   ./gradlew clean build
   ./gradlew test
   ./gradlew runIde
   ```

2. **プラグインバリデータの実行**
   ```bash
   ./gradlew runPluginVerifier
   ```
   IntelliJ Platform Gradle Pluginの設定により、推奨されるIDEバージョンで自動検証されます。

3. **手動テスト**
   - IntelliJ IDEAで開発用インスタンスを起動
   - プラグインの各機能をテスト：
     - Specファイルの構文ハイライト
     - コード補完
     - Goto Declaration
     - テスト実行

## 参考リンク

- [IntelliJ Platform Plugin SDK - Creating a Plugin Gradle Project](https://plugins.jetbrains.com/docs/intellij/creating-plugin-project.html)
- [IntelliJ Platform 2024.* API Changes](https://plugins.jetbrains.com/docs/intellij/api-changes-list-2024.html)
- [IntelliJ Platform 2025.3: What Plugin Developers Should Know](https://blog.jetbrains.com/platform/2025/11/intellij-platform-2025-3-what-plugin-developers-should-know/)
- [JetBrains Runtime GitHub Repository](https://github.com/JetBrains/JetBrainsRuntime)

## まとめ

**必須アクション:**
1. JDK 21をインストール
2. `build.gradle.kts`のjvmToolchainを21に変更
3. ビルドとテストの実施

**推定所要時間:** 30分～1時間

この変更により、最新のIntelliJ IDEA 2024.3～2025.3（現在の最新版）でプラグインが正常に動作するようになります。

## 2025.3の追加情報

IntelliJ Platform 2025.3では以下の重要な変更があります：

- **統合ディストリビューション**: IntelliJ IDEA CommunityとUltimateが統合され、ライセンスによる機能制御に変更
- **後方互換性の維持**: 2025.2以前でビルドしたプラグインも2025.3で動作（ただしJava 21要件は継続）
- **IntelliJ Platform Gradle Plugin 2.10.4以降推奨**: `intellijIdea()`依存関係ヘルパーの使用が推奨される
- **プラグイン検証**: `./gradlew verifyPlugin`でJetBrains Plugin Verifierと同じチェックをローカルで実行可能
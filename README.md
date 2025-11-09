# Zip Extractor Tool

特定フォルダ配下の ZIP ファイルから、指定されたファイル名パターン（正規表現）に一致するファイルを抽出する Java ツールです。  
Windows／Linux 双方で動作し、ZIP 内フォルダ構造を維持したままファイルを展開します。

---

## 📘 概要

このツールは、以下のような用途を想定しています。

- フォルダ内に多数ある ZIP ファイルから、特定の命名規則（例：`DATA_20251012.csv`）に一致するファイルだけを抽出したい
- ZIP 内部のフォルダ構造を維持したまま展開したい
- Windows 製 ZIP も Linux 製 ZIP も文字化けせずに扱いたい
- バッチやスケジュールタスクから自動実行したい

---

## 🧱 プロジェクト構成

```
zip-extractor/
├─ build.gradle.kts
├─ settings.gradle.kts
├─ config.properties
├─ run.bat
└─ src/
   └─ main/
      └─ java/com/kunieda/zipx/
         ├─ App.java
         ├─ AppConfig.java
         ├─ PropertyLoader.java
         ├─ ZipFinder.java
         ├─ ZipEntryExtractor.java
         ├─ Layout.java
         ├─ SafePaths.java
         ├─ Names.java
         ├─ IO.java
         ├─ Log.java
         └─ Defaults.java
```

---

## ⚙️ 開発環境

| 項目 | 推奨設定 |
|------|----------|
| **JDK** | 8 |
| **ビルドツール** | Gradle (Kotlin DSL) |
| **IDE** | IntelliJ IDEA |
| **対応OS** | Windows / Linux / macOS |
| **外部依存** | なし（標準ライブラリのみ） |

---

## 🚀 セットアップ手順

### 1. IntelliJ IDEA でプロジェクトを開く
1. IntelliJ を起動 → **「File → Open」** から `zip-extractor` フォルダを選択
2. Gradle プロジェクトとしてインポートされるのを待つ
3. `src/main/java` 以下に Java クラスが認識されていれば OK

---

### 2. Gradle 設定ファイル

#### `settings.gradle.kts`
```kotlin
rootProject.name = "zip-extractor"
```

#### `build.gradle.kts`
```kotlin
plugins {
    application
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

application {
    mainClass.set("com.kunieda.zipx.App")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.kunieda.zipx.App"
        attributes["Implementation-Title"] = "zip-extractor"
        attributes["Implementation-Version"] = project.version
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
```

---

### 3. Gradle ビルドの実行

#### IntelliJ からビルドする場合
- 右側の **Gradle タブ** → `Tasks > build > build` をダブルクリック
- 画面下部に `BUILD SUCCESSFUL` と出れば成功
- 成果物：  
  `build/libs/zip-extractor-<version>.jar`

#### コマンドラインからビルド
```bash
# 初回のみ Gradle Wrapper 作成
gradle wrapper

# ビルド実行
./gradlew clean build
```

---

### 4. 実行方法

#### A. IntelliJ IDEA から直接実行
1. 「Run → Edit Configurations...」を開く
2. 新しい **Application** 構成を追加
    - Main class: `com.kunieda.zipx.App`
    - Program arguments: `config.properties`
3. Run ボタン ▶ を押す

#### B. コマンドラインから実行
```bat
java -Xms64m -Xmx512m -jar build\libs\zip-extractor-1.0.jar config.properties
```

#### C. `run.bat` で自動ビルド＋実行
```bat
@echo off
chcp 65001 >NUL
setlocal

set "JAR=%~dp0build\libs\zip-extractor.jar"

set "CONF=%~dp0config.properties"

echo [INFO] Running %JAR%
java -Xms64m -Xmx512m -jar "%JAR%" "%CONF%"
endlocal
```

---

## ⚙️ 設定ファイル `config.properties`

```properties
targetDir=C:\work\zips
namePattern=^DATA_\d{8}.*\.csv$
zipPattern=(?i).*\.zip$
zipCharset=MS932
includeSubdirs=true
zipGlob=**/*.zip
createZipNameFolder=true
```

---

## 📂 出力フォルダ構造

```
C:\work\zips
 ├─ 20251101\data.zip
 ├─ 20251102\report.zip
 ↓
C:\work\result_zips
 ├─ 20251101\data\DATA_20251101.csv
 ├─ 20251102\report\DATA_20251102.csv
```

---

## 🔍 各プロパティ詳細

| プロパティ名 | 型 | 説明 | 例 |
|---------------|----|------|----|
| `targetDir` | Path | ZIPファイルを探す基点ディレクトリ | `C:\work\zips` |
| `namePattern` | Regex | ZIP内部で抽出するファイル名のパターン | `^DATA_\d{8}.*\.csv$` |
| `zipPattern` | Regex | 処理対象ZIPファイル名のパターン | `(?i).*\.zip$` |
| `zipCharset` | String | ZIP内部ファイル名の文字コード | `MS932` または `UTF-8` |
| `includeSubdirs` | Boolean | サブフォルダを探索するか | `true` |
| `zipGlob` | String | ZIPファイル探索のglob | `**/*.zip` |
| `createZipNameFolder` | Boolean | ZIP名フォルダを出力側に作るか | `true` |

---

## 🧠 処理の流れ

1. `targetDir` 以下のファイルを列挙
2. `zipPattern` に一致するZIPを取得
3. 各ZIPを `zipCharset` でオープン
4. ZIP内部のファイルを `namePattern` でフィルタ
5. 一致ファイルを `result_<targetDir名>` に展開
6. ZIPフォルダ構造を再現しつつ書き出し
7. ログ出力で進捗を報告

---

## 🧩 文字コード `zipCharset` について

ZIPファイル内の**ファイル名文字化け防止**のための設定です。

| 環境 | 推奨設定 | 説明 |
|------|-----------|------|
| Windows標準ZIP（エクスプローラなどで作成） | `MS932` | Shift_JISベースの拡張 |
| Linux/macOS／7-ZipなどUTF-8保存ZIP | `UTF-8` | 国際標準 |
| 不明な場合 | まずMS932で試し、文字化け時はUTF-8 |

---

## 🧰 内部構造とクラス概要

| クラス | 役割 |
|--------|------|
| `App` | エントリポイント（全体制御） |
| `AppConfig` | 設定情報を保持（プロパティ読込結果） |
| `PropertyLoader` | `config.properties` の解析 |
| `ZipFinder` | 対象ZIPファイルの検索 |
| `ZipEntryExtractor` | ZIPの展開とフィルタ処理 |
| `Layout` | 出力パス計算（result_xxx構造の生成） |
| `IO` | InputStream ⇔ OutputStream のバイト転送 |
| `Log` | ログ出力ユーティリティ |
| `Defaults` | デフォルト値（MS932など） |
| `Names`, `SafePaths` | パス安全処理・名前操作補助 |

---

## 🔧 主要コード例

### IO.copy()
```java
static void copy(InputStream in, OutputStream out) throws IOException {
    byte[] buf = new byte[8192];
    int r;
    while ((r = in.read(buf)) != -1) {
        out.write(buf, 0, r);
    }
}
```
> 8KBバッファで読み書きし、ストリーム間のデータを効率転送します。

---

## 🧾 実行ログ例

```
[2025-11-09 22:15:01][INFO ] targetDir     : C:\work\zips
[2025-11-09 22:15:01][INFO ] resultRoot    : C:\work\result_zips
[2025-11-09 22:15:01][INFO ] namePattern   : ^DATA_\d{8}.*\.csv$
[2025-11-09 22:15:02][XTRCT] C:\work\zips\20251101\data.zip :: DATA_20251101.csv -> C:\work\result_zips\20251101\data\DATA_20251101.csv
[2025-11-09 22:15:02][INFO ] DONE. zips=12, extracted=37
```

---

## 💡 トラブルシューティング

| 症状 | 原因 | 対策 |
|------|------|------|
| 日本語ファイル名が文字化け | ZIPの文字コードがUTF-8 | `zipCharset=UTF-8` に変更 |
| ZIPが見つからない | サブフォルダ無視設定 | `includeSubdirs=true` |
| ファイルが抽出されない | 正規表現ミス | `namePattern` を確認 |
| 出力フォルダが無い | 権限・パスエラー | 管理者権限実行またはフォルダ作成 |


# 第21章: アーキテクチャの検証

## 21.1 ArchUnit によるルール強制

### アーキテクチャルールの自動検証

アーキテクチャは設計時点で定義されても、開発が進むにつれて逸脱が発生しがちです。ArchUnit は、Java のアーキテクチャルールをテストコードとして記述し、自動的に検証するライブラリです。

```plantuml
@startuml
title ArchUnit によるアーキテクチャ検証

rectangle "アーキテクチャルール" as rules {
  note as N1
    - レイヤー間の依存関係
    - 命名規則
    - パッケージ構造
  end note
}

rectangle "テストコード" as test {
  class ArchitectureRuleTest {
    + presentationLayerShouldOnlyAccessServiceLayerAndDomainLayer()
    + serviceLayerShouldOnlyAccessDomainAndInfrastructureLayers()
    + domainLayerShouldNotAccessOtherLayers()
    + infrastructureLayerShouldNotAccessNonDomainLayers()
  }
}

rectangle "ソースコード" as source {
  package presentation
  package service
  package domain
  package infrastructure
}

rules --> test : "ルールを\nテストで表現"
test --> source : "自動検証"

@enduml
```

### 依存関係の設定

ArchUnit を使用するには、build.gradle に依存関係を追加します。

```groovy
dependencies {
    testImplementation 'com.tngtech.archunit:archunit:1.4.1'
    testImplementation 'com.tngtech.archunit:archunit-junit5:1.4.1'
}
```

### レイヤードアーキテクチャのルール

本プロジェクトでは、以下のレイヤー間依存関係を ArchUnit でテストしています。

```plantuml
@startuml
title レイヤー間の依存関係ルール

rectangle "Presentation Layer" as presentation {
  note as P1
    API Controller
    Web Controller
  end note
}

rectangle "Service Layer" as service {
  note as S1
    Application Service
  end note
}

rectangle "Domain Layer" as domain {
  note as D1
    Entity
    Value Object
    Domain Service
    Repository Interface
  end note
}

rectangle "Infrastructure Layer" as infrastructure {
  note as I1
    Repository Implementation
    Datasource
  end note
}

presentation -down-> service : "OK"
presentation -down-> domain : "OK"
presentation -[#red,dashed]-> infrastructure : "NG"

service -down-> domain : "OK"
service -down-> infrastructure : "OK"
service -[#red,dashed]-> presentation : "NG"

domain -[#red,dashed]-> presentation : "NG"
domain -[#red,dashed]-> service : "NG"
domain -[#red,dashed]-> infrastructure : "NG"

infrastructure -down-> domain : "OK"
infrastructure -[#red,dashed]-> presentation : "NG"

@enduml
```

### テストの実装

```java
@AnalyzeClasses(packages = "com.example.sms")
@DisplayName("アーキテクチャルール")
public class ArchitectureRuleTest {

    @Test
    @DisplayName("プレゼンテーション層はサービス層とドメイン層にアクセスできる")
    public void presentationLayerShouldOnlyAccessServiceLayerAndDomainLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
                .importPackages("com.example.sms");
        ArchRuleDefinition.noClasses()
                .that()
                .resideInAPackage("..presentation..")
                .should()
                .accessClassesThat()
                .resideInAPackage("..infrastructure..")
                .allowEmptyShould(true)
                .check(importedClasses);
    }

    @Test
    @DisplayName("サービス層はドメイン層とインフラストラクチャ層のみにアクセスできる")
    public void serviceLayerShouldOnlyAccessDomainAndInfrastructureLayers() {
        JavaClasses importedClasses = new ClassFileImporter()
                .importPackages("com.example.sms");
        ArchRuleDefinition.noClasses()
                .that()
                .resideInAPackage("..service..")
                .should()
                .accessClassesThat()
                .resideInAPackage("..presentation..")
                .allowEmptyShould(true)
                .check(importedClasses);
    }

    @Test
    @DisplayName("ドメイン層は他の層にアクセスできない")
    public void domainLayerShouldNotAccessOtherLayers() {
        JavaClasses importedClasses = new ClassFileImporter()
                .importPackages("com.example.sms");

        // プレゼンテーション層へのアクセス禁止
        ArchRuleDefinition.noClasses()
                .that()
                .resideInAPackage("..domain..")
                .should()
                .accessClassesThat()
                .resideInAPackage("..presentation..")
                .allowEmptyShould(true)
                .check(importedClasses);

        // サービス層へのアクセス禁止
        ArchRuleDefinition.noClasses()
                .that()
                .resideInAPackage("..domain..")
                .should()
                .accessClassesThat()
                .resideInAPackage("..service..")
                .allowEmptyShould(true)
                .check(importedClasses);

        // インフラストラクチャ層へのアクセス禁止
        ArchRuleDefinition.noClasses()
                .that()
                .resideInAPackage("..domain..")
                .should()
                .accessClassesThat()
                .resideInAPackage("..infrastructure..")
                .allowEmptyShould(true)
                .check(importedClasses);
    }

    @Test
    @DisplayName("インフラストラクチャ層はドメイン層とサービス層以外にアクセスできない")
    public void infrastructureLayerShouldNotAccessNonDomainLayers() {
        JavaClasses importedClasses = new ClassFileImporter()
                .importPackages("com.example.sms");
        ArchRuleDefinition.noClasses()
                .that()
                .resideInAPackage("..infrastructure..")
                .should()
                .accessClassesThat()
                .resideInAPackage("..presentation..")
                .allowEmptyShould(true)
                .check(importedClasses);
    }
}
```

### ArchUnit の利点

```plantuml
@startuml
title ArchUnit の利点

rectangle "従来のアプローチ" as old {
  note as N1
    - ドキュメントでルールを記載
    - コードレビューで確認
    - 人的なチェックに依存
    - 見落としが発生しやすい
  end note
}

rectangle "ArchUnit アプローチ" as new {
  note as N2
    - テストコードでルールを定義
    - CI/CD で自動検証
    - 違反があればビルド失敗
    - 継続的にルールを強制
  end note
}

old -right-> new : "改善"

@enduml
```

## 21.2 JIG によるドキュメント生成

### JIG とは

JIG（Java Instant-documentation Generator）は、Java のソースコードから自動的にドキュメントを生成するツールです。ドメイン駆動設計の観点からコードを分析し、ビジネスルールの可視化を支援します。

```plantuml
@startuml
title JIG によるドキュメント生成フロー

rectangle "ソースコード" as source {
  file "*.java"
  file "*.xml (MyBatis)"
}

rectangle "JIG 分析" as analysis {
  note as A1
    - クラス構造の解析
    - パッケージ依存関係
    - メソッド呼び出し関係
    - MyBatis マッパーの解析
  end note
}

rectangle "出力ドキュメント" as output {
  file "domain.html"
  file "application.html"
  file "entrypoint.html"
  file "usecase.html"
  file "enum.html"
  file "glossary.html"
}

source --> analysis
analysis --> output

@enduml
```

### Gradle への設定

JIG を Gradle プロジェクトに導入するには、以下のようにプラグインを追加します。

```groovy
plugins {
    id 'org.dddjava.jig-gradle-plugin' version '2025.10.1'
}
```

JIG の設定ファイル（jig.properties）で出力形式をカスタマイズできます。

```properties
jig.erd.output.directory=./build/jig-erd
jig.erd.output.prefix=library-er
jig.erd.output.format=svg
```

### 生成されるドキュメント

JIG は以下のドキュメントを生成します。

| ドキュメント | 説明 |
|------------|------|
| domain.html | ドメインモデルの一覧と関連図 |
| application.html | アプリケーションサービスの一覧 |
| entrypoint.html | API エントリーポイントの一覧 |
| usecase.html | ユースケースの一覧 |
| enum.html | 列挙型の一覧と値 |
| glossary.html | 用語集（クラス名と Javadoc） |
| package.html | パッケージ依存関係図 |
| repository.html | リポジトリの一覧 |
| sequence.html | シーケンス図 |
| insight.html | コード品質のインサイト |

### ドキュメントの構造

```plantuml
@startuml
title JIG ドキュメントの構造

package "index.html" {
  rectangle "ドメイン" {
    file "domain.html" : ビジネスルール
    file "enum.html" : 区分
    file "glossary.html" : 用語集
  }

  rectangle "アプリケーション" {
    file "application.html" : サービス
    file "usecase.html" : ユースケース
    file "repository.html" : リポジトリ
  }

  rectangle "エントリーポイント" {
    file "entrypoint.html" : API/画面
  }

  rectangle "構造" {
    file "package.html" : パッケージ関連
    file "sequence.html" : シーケンス
  }

  rectangle "インサイト" {
    file "insight.html" : 品質メトリクス
  }
}

@enduml
```

### JIG の活用シーン

```plantuml
@startuml
title JIG の活用シーン

actor 開発者 as dev
actor 新規メンバー as new
actor アーキテクト as arch

rectangle "JIG ドキュメント" as jig

dev --> jig : "ドメインモデルの確認"
new --> jig : "コードベースの理解"
arch --> jig : "アーキテクチャレビュー"

note right of jig
  - リリースごとにドキュメント生成
  - バージョン間の比較が可能
  - コードと常に同期
end note

@enduml
```

## 21.3 SonarQube による品質メトリクス

### 継続的コード品質

SonarQube は、コードの品質を継続的に測定・監視するプラットフォームです。GitHub Actions と連携して、プルリクエストごとにコード品質をチェックします。

```plantuml
@startuml
title SonarQube による品質管理フロー

actor 開発者 as dev
rectangle "GitHub" as github {
  file "Pull Request"
}

rectangle "GitHub Actions" as actions {
  note as A1
    ./gradlew build sonar
  end note
}

rectangle "SonarCloud" as sonar {
  note as S1
    - バグ検出
    - 脆弱性検出
    - コードスメル検出
    - テストカバレッジ
    - 重複コード検出
  end note
}

dev --> github : "コード push"
github --> actions : "トリガー"
actions --> sonar : "分析結果送信"
sonar --> github : "ステータスチェック"

@enduml
```

### GitHub Actions の設定

```yaml
name: SonarQube
on:
  push:
    branches:
      - main
  pull_request:
    types: [opened, synchronize, reopened]
jobs:
  build:
    name: Build and analyze
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
        with:
          fetch-depth: 0  # 完全な履歴を取得

      - name: Set up JDK
        uses: actions/setup-java@v4
        with:
          java-version: '25'
          distribution: 'oracle'

      - name: Grant execute permission for Gradle wrapper
        run: chmod +x ./gradlew
        working-directory: app/backend/sms

      - name: Cache SonarQube packages
        uses: actions/cache@v4
        with:
          path: ~/.sonar/cache
          key: ${{ runner.os }}-sonar
          restore-keys: ${{ runner.os }}-sonar

      - name: Cache Gradle packages
        uses: actions/cache@v4
        with:
          path: ~/.gradle/caches
          key: ${{ runner.os }}-gradle-${{ hashFiles('**/*.gradle') }}
          restore-keys: ${{ runner.os }}-gradle

      - name: Build and analyze
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
          SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
        run: ./gradlew build sonar --info
        working-directory: app/backend/sms
```

### Gradle での SonarQube 設定

```groovy
plugins {
    id "org.sonarqube" version "7.0.1.6134"
    id 'jacoco'
}

sonar {
    properties {
        property "sonar.projectKey", "k2works_case-study-sales"
        property "sonar.organization", "k2works"
        property "sonar.host.url", "https://sonarcloud.io"
        property "sonar.exclusions", "**/autogen/**"
    }
}

test {
    finalizedBy jacocoTestReport
}

jacocoTestReport {
    reports {
        xml.required = true
        html.required = true
    }
}
```

### 品質ゲート

SonarQube の品質ゲートは、コードがリリース可能かどうかを判断する基準です。

```plantuml
@startuml
title SonarQube 品質ゲート

rectangle "品質ゲートの条件" {
  note as N1
    **新規コードの条件:**
    - カバレッジ >= 80%
    - 重複コード <= 3%
    - 脆弱性 = 0
    - バグ = 0
    - コードスメル(Maintainability) = A

    **全体コードの条件:**
    - カバレッジ >= 70%
    - 重複コード <= 5%
  end note
}

rectangle "結果" {
  (Passed) #lightgreen
  (Failed) #pink
}

N1 --> (Passed) : "条件を満たす"
N1 --> (Failed) : "条件を満たさない"

note bottom of (Failed)
  マージがブロックされる
end note

@enduml
```

### メトリクスの種類

| メトリクス | 説明 | 目標値 |
|-----------|------|--------|
| Bugs | 潜在的なバグ | 0 |
| Vulnerabilities | セキュリティ脆弱性 | 0 |
| Code Smells | 保守性の問題 | 最小限 |
| Coverage | テストカバレッジ | 80% 以上 |
| Duplications | コードの重複率 | 3% 以下 |
| Technical Debt | 技術的負債の推定時間 | 最小限 |

### 品質改善サイクル

```plantuml
@startuml
title 品質改善サイクル

start
:コード変更;
:テスト実行;
:SonarQube 分析;

if (品質ゲート通過?) then (yes)
  :プルリクエストマージ;
else (no)
  :問題の修正;
  -> コード変更;
endif

:リリース;
stop

@enduml
```

## 21.4 アーキテクチャの進化

### バージョン間の比較

JIG ドキュメントをリリースごとに保存することで、アーキテクチャの進化を追跡できます。

```plantuml
@startuml
title バージョン間のアーキテクチャ比較

rectangle "v0.1.0" as v1 {
  note as N1
    - 基本的な CRUD
    - 貧血モデル
  end note
}

rectangle "v0.5.0" as v5 {
  note as N2
    - 値オブジェクト導入
    - ドメインサービス追加
  end note
}

rectangle "v0.10.0" as v10 {
  note as N3
    - リッチモデル
    - イベント駆動
    - 完全な DDD
  end note
}

v1 --> v5 : "リファクタリング"
v5 --> v10 : "機能拡張"

@enduml
```

### 継続的検証の仕組み

```plantuml
@startuml
title 継続的アーキテクチャ検証

rectangle "開発フロー" {
  (コミット) --> (ビルド)
  (ビルド) --> (ArchUnit テスト)
  (ArchUnit テスト) --> (JIG 生成)
  (JIG 生成) --> (SonarQube 分析)
  (SonarQube 分析) --> (デプロイ)
}

note bottom of (ArchUnit テスト)
  アーキテクチャルール違反で
  ビルド失敗
end note

note bottom of (SonarQube 分析)
  品質ゲート未達で
  マージブロック
end note

@enduml
```

## まとめ

この章では、アーキテクチャの検証について解説しました。

**重要なポイント:**

1. **ArchUnit によるルール強制**: アーキテクチャルールをテストコードとして記述し、CI/CD パイプラインで自動的に検証します。レイヤー間の依存関係違反を早期に検出できます。

2. **JIG によるドキュメント生成**: ソースコードから自動的にドキュメントを生成し、ドメインモデルやアプリケーション構造を可視化します。コードと常に同期したドキュメントが得られます。

3. **SonarQube による品質メトリクス**: コードの品質を継続的に測定し、バグや脆弱性、コードスメルを検出します。品質ゲートにより、一定の品質基準を維持できます。

4. **継続的検証**: これらのツールを組み合わせることで、アーキテクチャの整合性と品質を継続的に保証します。リリースごとにドキュメントを保存することで、アーキテクチャの進化も追跡できます。

これで第7部「品質とリファクタリング」は完了です。次の部では、付録としてリファレンス情報を提供します。

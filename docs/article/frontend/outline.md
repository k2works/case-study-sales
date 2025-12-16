# 販売管理システムのケーススタディ（フロントエンド実装）- アウトライン

## 書籍概要

**タイトル案**: 販売管理システムのケーススタディ - React + TypeScript によるモダンフロントエンド開発

**対象読者**:
- React による Web アプリケーション開発経験者
- TypeScript を活用したフロントエンド開発に興味のあるエンジニア
- Component / View パターンを学びたい開発者
- Context API による状態管理を学びたいエンジニア

**本書の特徴**:
- 実際の販売管理システムを題材にした実践的な内容
- Container / View パターンによる責務分離
- React Context によるグローバル状態管理
- TypeScript による型安全な開発
- Cypress による E2E テスト

**執筆方針**:
- ダイアグラムは plantuml を使う
- 実コード解説によりドキュメントからプロジェクトを再現できる構成にする

---

## 第1部: 導入と基盤

### 第1章: プロジェクト概要

#### 1.1 フロントエンドアーキテクチャ概要
- Component / View パターン
- Provider パターンによる状態管理
- Service 層による API 連携
- Model 層による型定義

#### 1.2 ディレクトリ構成
- src/components - コンテナコンポーネント
- src/views - プレゼンテーションコンポーネント
- src/providers - React Context プロバイダー
- src/services - API クライアント
- src/models - TypeScript 型定義

#### 1.3 技術スタック
- React 18 + TypeScript
- Vite によるビルド
- React Router による SPA ルーティング
- React Modal / React Tabs / React Icons
- Cypress による E2E テスト

#### 1.4 本書の構成
- 各章で扱う機能とテーマ
- バックエンド API との連携

---

### 第2章: 開発環境の構築

#### 2.1 技術スタックの選定
- React + TypeScript を選んだ理由
- Vite の利点
- ESLint による静的解析

#### 2.2 プロジェクト初期化
- Vite プロジェクトのセットアップ
- TypeScript 設定
- ESLint 設定

#### 2.3 依存パッケージ
- react-router-dom - ルーティング
- react-modal - モーダルダイアログ
- react-tabs - タブコンポーネント
- react-icons - アイコンライブラリ
- react-spinners - ローディングインジケータ

#### 2.4 開発サーバー
- Vite 開発サーバーの設定
- 環境変数の管理
- プロキシ設定

#### 2.5 テスト環境
- Jest による単体テスト
- Testing Library
- Cypress による E2E テスト

---

### 第3章: アーキテクチャ設計

#### 3.1 Component / View パターン
- Container コンポーネントの役割
- View コンポーネントの役割
- 責務分離の原則

#### 3.2 状態管理パターン
- React Context による状態管理
- Provider パターン
- カスタムフックの活用

#### 3.3 API 連携パターン
- Service 層の設計
- fetch API の抽象化
- エラーハンドリング

#### 3.4 型定義パターン
- Model 層の設計
- TypeScript インターフェース
- 型の再利用

---

## 第2部: 共通コンポーネント

### 第4章: アプリケーション基盤

#### 4.1 アプリケーションエントリポイント
- App.tsx の構成
- Providers.tsx による Context 集約
- RouteConfig.tsx によるルーティング

#### 4.2 認証ガード
- RouteAuthGuard コンポーネント
- ロールベースアクセス制御
- リダイレクト処理

#### 4.3 レイアウト
- SiteLayout コンポーネント
- ナビゲーション
- ヘッダー/フッター

#### 4.4 共通コンポーネント
- ErrorBoundary
- Message コンポーネント
- Loading インジケータ

---

### 第5章: モーダルパターン

#### 5.1 編集モーダル
- EditModal パターン
- フォーム状態管理
- バリデーション

#### 5.2 検索モーダル
- SearchModal パターン
- 検索条件入力
- 結果表示

#### 5.3 選択モーダル
- SelectModal パターン
- 一覧からの選択
- 選択結果の返却

---

## 第3部: マスタ管理機能

### 第6章: 認証・ユーザー管理

#### 6.1 認証フロー
- ログイン画面の実装
- 認証状態の管理
- ログアウト処理

#### 6.2 AuthUser Provider
- 認証コンテキスト
- トークン管理
- 自動ログイン

#### 6.3 ユーザー管理
- UserContainer
- UserCollection / UserSingle
- ユーザー CRUD

---

### 第7章: 部門・社員マスタ

#### 7.1 部門管理
- DepartmentContainer
- DepartmentCollection / DepartmentSingle
- 階層構造の表現

#### 7.2 社員管理
- EmployeeContainer
- EmployeeCollection / EmployeeSingle
- 部門との関連

#### 7.3 カスタムフック
- useDepartment
- useEmployee

---

### 第8章: 商品マスタ

#### 8.1 商品分類管理
- ProductCategoryContainer
- 分類一覧・詳細
- 階層構造

#### 8.2 商品管理
- ProductItemContainer
- 商品一覧・詳細
- 分類との関連

#### 8.3 BOM・代替品管理
- ProductItemBomModal
- ProductItemSubstituteModal
- 関連商品の管理

#### 8.4 Provider 設計
- ProductCategory Provider
- ProductItem Provider
- ProductBom Provider
- ProductSubstitute Provider

---

### 第9章: 取引先管理

#### 9.1 取引先分類
- PartnerCategoryContainer
- 分類タイプの管理

#### 9.2 取引先グループ
- PartnerGroupContainer
- グループ管理

#### 9.3 取引先一覧
- PartnerListContainer
- 顧客・仕入先の統合表示

#### 9.4 顧客管理
- CustomerContainer
- 顧客固有情報

#### 9.5 仕入先管理
- VendorContainer
- 仕入先固有情報

#### 9.6 Provider 設計
- PartnerCategory Provider
- PartnerGroup Provider
- PartnerList Provider
- Customer Provider
- Vendor Provider

---

### 第10章: コードマスタ

#### 10.1 コード管理
- CodeContainer
- コード種別管理

#### 10.2 地域コード
- RegionContainer
- 地域マスタ管理

#### 10.3 口座マスタ
- AccountContainer
- 口座情報管理

---

## 第4部: 販売管理機能

### 第11章: 受注管理

#### 11.1 受注一覧
- OrderTabContainer
- OrderContainer
- OrderCollection

#### 11.2 受注詳細
- OrderSingle
- OrderEditModal
- 明細行の管理

#### 11.3 受注一括登録
- OrderUploadContainer
- OrderUploadModal
- CSV アップロード処理

#### 11.4 受注ルール
- OrderRuleContainer
- ビジネスルール表示

#### 11.5 Provider 設計
- Order Provider
- 状態管理とAPI連携

---

### 第12章: 出荷・売上管理

#### 12.1 出荷管理
- ShippingTabContainer
- ShippingContainer
- 出荷指示一覧

#### 12.2 出荷指示
- ShippingOrderContainer
- 受注からの出荷指示作成

#### 12.3 出荷確認
- ShippingConfirmContainer
- 出荷実績の登録

#### 12.4 出荷ルール
- ShippingRuleContainer
- ビジネスルール表示

#### 12.5 売上管理
- SalesTabContainer
- SalesContainer
- 売上一覧・詳細

#### 12.6 売上集計
- SalesAggregateContainer
- 集計表示

---

### 第13章: 請求・回収管理

#### 13.1 請求管理
- InvoiceTabContainer
- InvoiceContainer
- 請求一覧・詳細

#### 13.2 請求集計
- InvoiceAggregateContainer
- 請求集計表示

#### 13.3 入金管理
- PaymentTabContainer
- PaymentContainer
- 入金一覧

#### 13.4 入金集計
- PaymentAggregateContainer
- 入金集計表示

---

## 第5部: 調達管理機能

### 第14章: 発注管理

#### 14.1 発注一覧
- PurchaseOrderTabContainer
- PurchaseOrderContainer
- PurchaseOrderCollection

#### 14.2 発注詳細
- PurchaseOrderSingle
- PurchaseOrderEditModal
- 明細行の管理

#### 14.3 発注一括登録
- PurchaseOrderUploadContainer
- CSV アップロード処理

#### 14.4 発注ルール
- PurchaseOrderRuleContainer
- ビジネスルール表示

#### 14.5 Provider 設計
- PurchaseOrder Provider

---

### 第15章: 仕入・支払管理

#### 15.1 仕入管理
- PurchaseTabContainer
- PurchaseContainer
- 仕入一覧

#### 15.2 仕入ルール
- PurchaseRuleContainer
- ビジネスルール表示

#### 15.3 支払管理
- PaymentTabContainer（調達）
- PaymentContainer
- 支払一覧

#### 15.4 支払集計
- PurchasePaymentAggregateContainer
- 支払集計表示

---

## 第6部: 在庫管理機能

### 第16章: 在庫管理

#### 16.1 在庫一覧
- InventoryTabContainer
- InventoryContainer
- 在庫一覧表示

#### 16.2 在庫一括登録
- InventoryUploadContainer
- InventoryUploadModal
- CSV アップロード処理

#### 16.3 在庫ルール
- InventoryRuleContainer
- ビジネスルール表示

#### 16.4 Provider 設計
- Inventory Provider

---

### 第17章: 倉庫・棚番マスタ

#### 17.1 在庫マスタ
- InventoryMasterContainer
- 倉庫・棚番のタブ切り替え

#### 17.2 倉庫管理
- WarehouseContainer
- 倉庫一覧・詳細

#### 17.3 棚番管理
- LocationNumberContainer
- 棚番一覧・詳細

#### 17.4 Provider 設計
- Warehouse Provider
- LocationNumber Provider

---

## 第7部: システム機能

### 第18章: ダウンロード機能

#### 18.1 ダウンロード画面
- DownloadContainer
- ダウンロード対象一覧

#### 18.2 ダウンロード処理
- DownloadService
- ファイルダウンロード実装

#### 18.3 Provider 設計
- Download Provider

---

### 第19章: 監査機能

#### 19.1 実行履歴画面
- AuditContainer
- 実行履歴一覧

#### 19.2 検索機能
- AuditSearchModal
- 条件検索

#### 19.3 Provider 設計
- Audit Provider
- useAudit フック

---

## 第8部: テストと品質

### 第20章: 単体テスト

#### 20.1 Jest 設定
- jest.config.js
- Testing Library 設定

#### 20.2 コンポーネントテスト
- render / screen
- fireEvent / userEvent

#### 20.3 カスタムフックテスト
- renderHook
- act

---

### 第21章: E2E テスト

#### 21.1 Cypress 設定
- cypress.config.ts
- サポートファイル

#### 21.2 マスタ管理テスト
- master/ ディレクトリ
- CRUD テスト

#### 21.3 販売管理テスト
- sales/ ディレクトリ
- 受注フローテスト

#### 21.4 調達管理テスト
- procurement/ ディレクトリ
- 発注フローテスト

#### 21.5 在庫管理テスト
- inventory/ ディレクトリ
- 在庫操作テスト

#### 21.6 システムテスト
- system/ ディレクトリ
- ダウンロード・監査テスト

---

## 付録

### 付録A: 技術スタック一覧

| カテゴリ | 技術 | バージョン |
|---------|------|-----------|
| 言語 | TypeScript | 5.5 |
| フレームワーク | React | 18.3 |
| ルーティング | React Router | 6.26 |
| ビルドツール | Vite | 5.4 |
| テスト | Jest | 29.7 |
| E2E テスト | Cypress | 14.5 |
| UI ライブラリ | react-modal, react-tabs, react-icons | - |

### 付録B: ディレクトリ構成

```
src/
├── components/           # コンテナコンポーネント
│   ├── application/      # アプリケーション基盤
│   ├── master/           # マスタ管理
│   │   ├── account/
│   │   ├── code/
│   │   ├── department/
│   │   ├── employee/
│   │   ├── inventory/
│   │   ├── partner/
│   │   └── product/
│   ├── sales/            # 販売管理
│   │   ├── invoice/
│   │   ├── order/
│   │   ├── payment/
│   │   ├── sales/
│   │   └── shipping/
│   ├── procurement/      # 調達管理
│   │   ├── order/
│   │   ├── payment/
│   │   └── purchase/
│   ├── inventory/        # 在庫管理
│   │   ├── list/
│   │   ├── rule/
│   │   └── upload/
│   └── system/           # システム機能
│       ├── audit/
│       ├── download/
│       └── user/
├── views/                # プレゼンテーションコンポーネント
│   └── (components と同構成)
├── providers/            # React Context プロバイダー
│   ├── master/
│   ├── sales/
│   ├── procurement/
│   ├── inventory/
│   └── system/
├── services/             # API クライアント
│   ├── master/
│   ├── sales/
│   ├── procurement/
│   ├── inventory/
│   └── system/
├── models/               # TypeScript 型定義
│   ├── master/
│   ├── sales/
│   ├── procurement/
│   ├── inventory/
│   └── system/
└── App.tsx
```

### 付録C: コンポーネント一覧

#### マスタ管理
- DepartmentContainer / DepartmentCollection / DepartmentSingle
- EmployeeContainer / EmployeeCollection / EmployeeSingle
- ProductCategoryContainer / ProductCategoryCollection / ProductCategorySingle
- ProductItemContainer / ProductItemCollection / ProductItemSingle
- PartnerCategoryContainer / PartnerCategoryCollection / PartnerCategorySingle
- PartnerGroupContainer / PartnerGroupCollection / PartnerGroupSingle
- PartnerListContainer / PartnerListCollection / PartnerListSingle
- CustomerContainer / CustomerCollection / CustomerSingle
- VendorContainer / VendorCollection / VendorSingle
- RegionContainer / RegionCollection / RegionSingle
- AccountContainer
- WarehouseContainer
- LocationNumberContainer

#### 販売管理
- OrderContainer / OrderCollection / OrderSingle
- OrderUploadContainer / OrderUploadCollection / OrderUploadSingle
- OrderRuleContainer / OrderRuleCollection
- ShippingContainer / ShippingCollection
- ShippingOrderContainer / ShippingConfirmContainer
- ShippingRuleContainer / ShippingRuleCollection
- SalesContainer / SalesCollection / SalesSingle
- SalesAggregateContainer / SalesAggregateCollection
- InvoiceContainer / InvoiceCollection / InvoiceSingle
- InvoiceAggregateContainer / InvoiceAggregateCollection
- PaymentContainer / PaymentCollection
- PaymentAggregateContainer / PaymentAggregateCollection

#### 調達管理
- PurchaseOrderContainer / PurchaseOrderCollection / PurchaseOrderSingle
- PurchaseOrderUploadContainer
- PurchaseOrderRuleContainer / PurchaseOrderRuleCollection
- PurchaseContainer / PurchaseCollection
- PurchaseRuleContainer / PurchaseRuleCollection
- PaymentContainer（調達）/ PaymentCollection
- PurchasePaymentAggregateContainer

#### 在庫管理
- InventoryContainer / InventoryCollection / InventorySingle
- InventoryUploadContainer
- InventoryRuleContainer / InventoryRuleCollection

#### システム機能
- UserContainer
- AuditContainer / AuditCollection
- DownloadContainer

### 付録D: Provider 一覧

| カテゴリ | Provider | 主な責務 |
|---------|----------|---------|
| 認証 | AuthUser | 認証状態管理 |
| マスタ | Department | 部門データ管理 |
| マスタ | Employee | 社員データ管理 |
| マスタ | ProductCategory | 商品分類データ管理 |
| マスタ | ProductItem | 商品データ管理 |
| マスタ | ProductBom | BOM データ管理 |
| マスタ | ProductSubstitute | 代替品データ管理 |
| マスタ | PartnerCategory | 取引先分類データ管理 |
| マスタ | PartnerGroup | 取引先グループデータ管理 |
| マスタ | PartnerList | 取引先データ管理 |
| マスタ | Customer | 顧客データ管理 |
| マスタ | Vendor | 仕入先データ管理 |
| マスタ | Region | 地域データ管理 |
| マスタ | Account | 口座データ管理 |
| マスタ | Warehouse | 倉庫データ管理 |
| マスタ | LocationNumber | 棚番データ管理 |
| 販売 | Order | 受注データ管理 |
| 販売 | Shipping | 出荷データ管理 |
| 販売 | Sales | 売上データ管理 |
| 販売 | Invoice | 請求データ管理 |
| 販売 | Payment | 入金データ管理 |
| 調達 | PurchaseOrder | 発注データ管理 |
| 調達 | Purchase | 仕入データ管理 |
| 調達 | Payment（調達） | 支払データ管理 |
| 在庫 | Inventory | 在庫データ管理 |
| システム | User | ユーザーデータ管理 |
| システム | Audit | 監査データ管理 |
| システム | Download | ダウンロードデータ管理 |

### 付録E: Service 一覧

| カテゴリ | Service | API エンドポイント |
|---------|---------|-------------------|
| 認証 | auth | /api/auth/* |
| マスタ | department | /api/departments/* |
| マスタ | employee | /api/employees/* |
| マスタ | productCategory | /api/product-categories/* |
| マスタ | product | /api/products/* |
| マスタ | partnerCategory | /api/partner-categories/* |
| マスタ | partnerGroup | /api/partner-groups/* |
| マスタ | partner | /api/partners/* |
| マスタ | customer | /api/customers/* |
| マスタ | vendor | /api/vendors/* |
| マスタ | region | /api/regions/* |
| マスタ | account | /api/accounts/* |
| マスタ | warehouse | /api/warehouses/* |
| マスタ | locationnumber | /api/locationnumbers/* |
| 販売 | order | /api/orders/* |
| 販売 | shipping | /api/shippings/* |
| 販売 | sales | /api/sales/* |
| 販売 | invoice | /api/invoices/* |
| 販売 | payment | /api/payments/* |
| 調達 | purchaseOrder | /api/purchase-orders/* |
| 調達 | purchase | /api/purchases/* |
| 調達 | payment（調達） | /api/purchase-payments/* |
| 在庫 | inventory | /api/inventories/* |
| システム | user | /api/users/* |
| システム | audit | /api/audits/* |
| システム | download | /api/downloads/* |

---

## 執筆計画

### 想定ページ数
- 本文: 約300〜350ページ
- 付録: 約30ページ
- 合計: 約330〜380ページ

### 章ごとの想定ページ数

| 部 | 章数 | 想定ページ |
|----|------|-----------|
| 第1部: 導入と基盤 | 3章 | 45ページ |
| 第2部: 共通コンポーネント | 2章 | 30ページ |
| 第3部: マスタ管理機能 | 5章 | 60ページ |
| 第4部: 販売管理機能 | 3章 | 45ページ |
| 第5部: 調達管理機能 | 2章 | 30ページ |
| 第6部: 在庫管理機能 | 2章 | 25ページ |
| 第7部: システム機能 | 2章 | 20ページ |
| 第8部: テストと品質 | 2章 | 30ページ |
| 付録 | 5項目 | 30ページ |

---

## 本書のポイント

### 1. Component / View パターン
Container コンポーネントと View コンポーネントの責務分離を徹底し、テスタビリティと保守性を向上。

### 2. Provider パターン
React Context を活用した状態管理により、Props drilling を回避しつつグローバル状態を管理。

### 3. TypeScript 活用
型定義により API レスポンスとの整合性を保証し、コンパイル時にエラーを検出。

### 4. 一貫した設計パターン
Collection / Single / EditModal / SearchModal の一貫したパターンにより、学習コストを低減。

### 5. E2E テスト重視
Cypress による E2E テストで、実際のユーザー操作を模したテストを実施。

### 6. バックエンド API との連携
Spring Boot バックエンドとの API 連携を通じて、フルスタック開発の実践例を提供。

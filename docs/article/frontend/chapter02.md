# 第2章: 開発環境の構築

本章では、販売管理システムのフロントエンド開発環境の構築方法について解説します。技術スタックの選定理由、プロジェクト初期化、各種設定ファイルの詳細を説明します。

## 2.1 技術スタックの選定

### React + TypeScript を選んだ理由

本システムでは、フロントエンドフレームワークとして React を、言語として TypeScript を採用しています。

**React を選んだ理由**
- コンポーネントベースの設計による再利用性
- 仮想 DOM による効率的なレンダリング
- 豊富なエコシステムとコミュニティ
- React Context による状態管理の柔軟性

**TypeScript を選んだ理由**
- 型安全性によるバグの早期発見
- IDE のインテリセンス機能の向上
- API レスポンスの型定義による整合性保証
- リファクタリング時の安全性

### Vite を選んだ理由

ビルドツールとして Vite を採用しています。

- **高速な開発サーバー起動**: ESM ベースの開発サーバー
- **高速な HMR（Hot Module Replacement）**: 変更の即座の反映
- **最適化されたビルド**: Rollup ベースの本番ビルド
- **シンプルな設定**: 最小限の設定で動作

### ESLint による静的解析

コード品質を維持するために ESLint を使用しています。

- **一貫したコードスタイル**: チーム全体で統一されたコーディング規約
- **潜在的なバグの検出**: 未使用変数、到達不能コードの検出
- **React Hooks ルールの強制**: Hooks の正しい使用を保証

## 2.2 プロジェクト初期化

### Vite プロジェクトの作成

新規プロジェクトの作成は以下のコマンドで行います。

```bash
npm create vite@latest app -- --template react-ts
cd app
npm install
```

### 追加パッケージのインストール

本システムで使用するパッケージをインストールします。

```bash
# UI ライブラリ
npm install react-router-dom react-modal react-tabs react-icons react-spinners

# 型定義
npm install -D @types/react-modal

# テスト関連
npm install -D jest @testing-library/react @testing-library/jest-dom @testing-library/user-event
npm install -D @types/jest ts-jest jest-environment-jsdom

# Cypress
npm install -D cypress cypress-file-upload allure-cypress

# ESLint
npm install -D @eslint/js globals eslint-plugin-react-hooks eslint-plugin-react-refresh typescript-eslint
```

## 2.3 TypeScript 設定

### tsconfig.json

プロジェクトルートの `tsconfig.json` は参照ファイルを指定しています。

```json
{
  "files": [],
  "references": [
    { "path": "./tsconfig.app.json" },
    { "path": "./tsconfig.node.json" }
  ]
}
```

### tsconfig.app.json

アプリケーションコード用の TypeScript 設定です。

```json
{
  "compilerOptions": {
    "target": "ES2020",
    "useDefineForClassFields": true,
    "lib": ["ES2020", "DOM", "DOM.Iterable"],
    "module": "ESNext",
    "skipLibCheck": true,

    /* Bundler mode */
    "moduleResolution": "bundler",
    "allowImportingTsExtensions": true,
    "isolatedModules": true,
    "moduleDetection": "force",
    "noEmit": true,
    "jsx": "react-jsx",

    /* Linting */
    "strict": true,
    "noUnusedLocals": false,
    "noUnusedParameters": true,
    "noFallthroughCasesInSwitch": true
  },
  "include": [
    "src"
  ]
}
```

**主要な設定項目**

| 項目 | 設定値 | 説明 |
|------|--------|------|
| target | ES2020 | 出力する JavaScript のバージョン |
| lib | ES2020, DOM, DOM.Iterable | 使用する型定義ライブラリ |
| module | ESNext | モジュールシステム |
| moduleResolution | bundler | Vite のバンドラーモードに対応 |
| jsx | react-jsx | React 17+ の新しい JSX 変換 |
| strict | true | 厳格な型チェックを有効化 |

## 2.4 ESLint 設定

### eslint.config.js

ESLint の設定ファイルです。

```javascript
import js from '@eslint/js'
import globals from 'globals'
import reactHooks from 'eslint-plugin-react-hooks'
import reactRefresh from 'eslint-plugin-react-refresh'
import tseslint from 'typescript-eslint'

export default tseslint.config(
  { ignores: ['dist'] },
  {
    extends: [js.configs.recommended, ...tseslint.configs.recommended],
    files: ['**/*.{ts,tsx}'],
    languageOptions: {
      ecmaVersion: 2020,
      globals: globals.browser,
    },
    plugins: {
      'react-hooks': reactHooks,
      'react-refresh': reactRefresh,
    },
    rules: {
      ...reactHooks.configs.recommended.rules,
      'react-refresh/only-export-components': [
        'warn',
        { allowConstantExport: true },
      ],
    },
  },
)
```

**適用されるルール**

| プラグイン | ルール | 説明 |
|-----------|--------|------|
| react-hooks | rules of hooks | Hooks の使用ルールを強制 |
| react-hooks | exhaustive-deps | useEffect の依存配列を検証 |
| react-refresh | only-export-components | HMR のためのエクスポート規則 |

## 2.5 Vite 設定

### vite.config.ts

Vite の設定ファイルです。

```typescript
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import env from 'vite-plugin-env-compatible'

export default defineConfig({
  plugins: [
      react(),
      env({ prefix: "VITE",  mountedPath: "process.env" }),
  ],
  server: {
      port: 5173,
      host: '127.0.0.1'
  }
})
```

**設定項目の説明**

| 項目 | 説明 |
|------|------|
| plugins.react() | React のトランスパイルを有効化 |
| plugins.env() | 環境変数を process.env に展開 |
| server.port | 開発サーバーのポート番号 |
| server.host | 開発サーバーのホスト |

## 2.6 環境変数の管理

### env.ts

環境変数を管理するユーティリティです。

```typescript
const Env = (() => {
    const isProduction = (): boolean => process.env.NODE_ENV === "production";
    const isStaging = (): boolean => process.env.VITE_MODE === "staging";
    const prdApiUrl = ((): string => {
        if (isProduction()) {
            return process.env.VITE_APP_API_URL as string;
        }
        return "" as string
    })();
    const devApiUrl = ((): string => {
        if (isStaging()) {
            return process.env.VITE_STAGING_API_URL as string;
        }
        return "http://localhost:8080/api" as string
    })();
    const currentEnv = (): string => {
        if (isProduction()) {
            return "Production";
        }
        if (isStaging()) {
            return "Staging";
        }
        return "Development";
    }

    return {
        isProduction,
        prdApiUrl,
        devApiUrl,
        currentEnv
    };
})();

export default Env;
```

### 環境別設定ファイル

環境ごとに異なる設定を `.env` ファイルで管理します。

**.env（開発環境）**
```
VITE_MODE=development
VITE_APP_API_URL=https://case-study-sales-api-dev.herokuapp.com/api
```

**.env.staging（ステージング環境）**
```
VITE_MODE=staging
VITE_STAGING_API_URL=https://case-study-sales-api-dev.herokuapp.com/api
```

### API 設定

`services/config.ts` で API エンドポイントと認証ヘッダーを設定します。

```typescript
import Env from "../env.ts";

const Config = () => {
    let config: { apiUrl: string, authHeader: string };
    const getApiUrl = () => Env.isProduction() ? Env.prdApiUrl : Env.devApiUrl;
    const user = window.localStorage.getItem("user");
    if (user) {
        config = {apiUrl: getApiUrl(), authHeader: "Bearer " + JSON.parse(user).token};
        return config;
    }
    config = {apiUrl: getApiUrl(), authHeader: ""};
    return config;
};

export default Config;
```

## 2.7 開発サーバーの起動

### npm scripts

`package.json` で定義された npm scripts を使用して開発サーバーを起動します。

```json
{
  "scripts": {
    "dev": "vite",
    "stg": "vite --mode staging",
    "test": "jest",
    "build": "tsc -b && vite build",
    "lint": "eslint .",
    "preview": "vite preview",
    "cypress:open": "cypress open",
    "cypress:run": "cypress run"
  }
}
```

| コマンド | 説明 |
|---------|------|
| npm run dev | 開発サーバーの起動（ローカル API 接続） |
| npm run stg | 開発サーバーの起動（ステージング API 接続） |
| npm run test | Jest による単体テストの実行 |
| npm run build | 本番ビルドの実行 |
| npm run lint | ESLint によるコード検証 |
| npm run preview | ビルド結果のプレビュー |
| npm run cypress:open | Cypress テストランナーの起動 |
| npm run cypress:run | Cypress テストの実行（ヘッドレス） |

### 開発サーバーの起動

```bash
# 開発モードで起動（ローカル API に接続）
npm run dev

# ステージングモードで起動（ステージング API に接続）
npm run stg
```

開発サーバーは `http://127.0.0.1:5173` で起動します。

## 2.8 テスト環境

### Jest 設定

`jest.config.cjs` で Jest の設定を行います。

```javascript
module.exports = {
    moduleFileExtensions: [
        "js",
        "ts",
        "tsx"
    ],
    testMatch: [
        "**/src/**/*.test.ts",
        "**/src/**/*.test.tsx"
    ],
    roots: [
        "<rootDir>/src"
    ],
    preset: "ts-jest",
    testEnvironment: "jest-environment-jsdom",
    transform: {
        '^.+\\.jsx?$': 'babel-jest',
        '^.+\\.tsx?$': 'babel-jest',
        '\\.css$': 'jest-transform-css',
    },
    moduleNameMapper: {
        '\\.(css|scss)$': 'identity-obj-proxy',
        "\\.(gif|ttf|eot|svg|png)$": "<rootDir>/test/__mocks__/fileMock.js",
    },
};
```

### Cypress 設定

`cypress.config.cjs` で Cypress の設定を行います。

```javascript
const { allureCypress } = require("allure-cypress/reporter");

module.exports = {
  e2e: {
    setupNodeEvents(on, config) {
      allureCypress(on, config, {
        resultsDir: "allure-results",
      });
      return config;
    },
  },
  viewportWidth: 1920,
  viewportHeight: 1080,
};
```

### Cypress カスタムコマンド

`cypress/support/e2e.js` で再利用可能なコマンドを定義します。

```javascript
import './commands.js'
import "allure-cypress";

Cypress.Commands.add('login', (username, password) => {
    cy.visit('http://127.0.0.1:5173/login');
    cy.get('#userId').clear();
    cy.get('#userId').type(username);
    cy.get('#password').clear();
    cy.get('#password').type(password);
    cy.get('#login').click();
});

Cypress.Commands.add('loginWithSession', (username, password) => {
    cy.session(
        username,
        () => {
            cy.visit('http://127.0.0.1:5173/login');
            cy.get('#userId').clear();
            cy.get('#userId').type(username);
            cy.get('#password').clear();
            cy.get('#password').type(password);
            cy.get('#login').click();

            cy.contains('HOME').should('be.visible');
        }
    )
});
```

## 2.9 ディレクトリ構成の作成

プロジェクト初期化後、以下のディレクトリ構成を作成します。

```bash
mkdir -p src/components/application
mkdir -p src/components/master
mkdir -p src/components/sales
mkdir -p src/components/procurement
mkdir -p src/components/inventory
mkdir -p src/components/system
mkdir -p src/views/application
mkdir -p src/views/master
mkdir -p src/views/sales
mkdir -p src/views/procurement
mkdir -p src/views/inventory
mkdir -p src/views/system
mkdir -p src/providers/master
mkdir -p src/providers/sales
mkdir -p src/providers/procurement
mkdir -p src/providers/inventory
mkdir -p src/providers/system
mkdir -p src/services/master
mkdir -p src/services/sales
mkdir -p src/services/procurement
mkdir -p src/services/inventory
mkdir -p src/services/system
mkdir -p src/models/master
mkdir -p src/models/sales
mkdir -p src/models/procurement
mkdir -p src/models/inventory
mkdir -p src/models/system
mkdir -p cypress/e2e
mkdir -p cypress/fixtures
mkdir -p cypress/support
```

## まとめ

本章では、フロントエンド開発環境の構築について解説しました。

- **技術スタック**: React + TypeScript + Vite
- **TypeScript 設定**: strict モードによる型安全性
- **ESLint 設定**: React Hooks ルールの適用
- **環境変数管理**: 開発/ステージング/本番の切り替え
- **テスト環境**: Jest + Cypress

次章では、アーキテクチャ設計について詳しく解説します。

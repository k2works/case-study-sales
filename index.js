import "./style.css";
import render from "@k2works/full-stack-lab";

const mindmap = `
@startmindmap
* 取引先管理
-- ユースケース
--- 取引先分類
---- 取引先分類一覧
---- 取引先分類詳細
---- 取引先分類検索
---- 取引先分類登録
---- 取引先分類編集
---- 取引先分類削除
--- 取引先グループ
---- 取引先グループ一覧
---- 取引先グループ詳細
---- 取引先グループ検索
---- 取引先グループ登録
---- 取引先グループ編集
---- 取引先グループ削除
--- 取引先
---- 取引先一覧
---- 取引先詳細
---- 取引先検索
---- 取引先登録
---- 取引先編集
---- 取引先削除
--- 顧客
---- 顧客一覧
---- 顧客詳細
---- 顧客検索
---- 顧客登録
---- 顧客編集
---- 顧客削除
--- 仕入先
---- 仕入先一覧
---- 仕入先詳細
---- 仕入先検索
---- 仕入先登録
---- 仕入先編集
---- 仕入先削除
--- 地域
---- 地域一覧
---- 地域詳細
---- 地域検索
---- 地域登録
---- 地域編集
---- 地域削除
-- ドメインモデル
--- 取引先分類一覧
--- 取引先分類種別
--- 取引先分類
--- 取引先分類所属
--- 取引先グループ一覧
--- 取引先グループ
--- 取引先一覧
--- 取引先
--- 取引先分類種別
--- 取引先分類
--- 取引先分類所属
--- 顧客一覧
--- 顧客
--- 仕入先一覧
--- 仕入先
--- 地域一覧
--- 地域
-- データモデル
--- 取引先グループマスタ
--- 取引先分類種別マスタ
--- 取引先分類マスタ
--- 取引先分類所属マスタ
--- 取引先マスタ
--- 顧客マスタ
--- 仕入先マスタ
--- 出荷先マスタ
--- 地域マスタ
** ユーザーインターフェース
*** ビュー
**** 取引先分類一覧
**** 取引先分類詳細
**** 取引先分類検索
**** 取引先グループ一覧
**** 取引先グループ詳細
**** 取引先グループ検索
**** 取引先一覧
**** 取引先詳細
**** 取引先検索
**** 顧客一覧
**** 顧客詳細
**** 顧客検索
**** 仕入先一覧
**** 仕入先詳細
**** 仕入先検索
**** 出荷先一覧
**** 地域一覧
**** 地域詳細
**** 地域検索
*** モデル
**** ナビゲーション
**** 取引先分類一覧
**** 取引先分類詳細
**** 取引先グループ一覧
**** 取引先グループ詳細
**** 取引先一覧
**** 取引先詳細
**** 顧客一覧
**** 顧客詳細
**** 仕入先一覧
**** 仕入先詳細
**** 出荷先一覧
**** 地域一覧
**** 地域詳細
*** インタラクション
**** ナビゲーション
**** 取引先分類一覧
**** 取引先分類詳細
**** 取引先グループ一覧
**** 取引先グループ詳細
**** 取引先一覧
**** 取引先詳細
**** 顧客一覧
**** 顧客詳細
**** 仕入先一覧
**** 仕入先詳細
**** 出荷先一覧
**** 地域一覧
**** 地域詳細

@endmindmap
`;

const contents = `
## 取引先管理

- 管理者は取引先を管理できる

## TODOリスト

- [x] 取引先管理APIを作成する
    - [x] 取引先グループAPIを作成する
      - [x] データモデルを実装する
      - [x] ドメインモデルを実装する
    - [x] 取引先分類APIを作成する
      - [x] データモデルを実装する
      - [x] ドメインモデルを実装する
    - [x] 地域APIを作成する
      - [x] データモデルを実装する
      - [x] ドメインモデルを実装する
    - [x] 取引先APIを作成する
      - [x] データモデルを実装する
      - [x] ドメインモデルを実装する
    - [x] 顧客APIを作成する
      - [x] データモデルを実装する
      - [x] ドメインモデルを実装する
    - [x] 仕入先APIを作成する
      - [x] データモデルを実装する
      - [x] ドメインモデルを実装する
- [x] 取引先管理画面を作成する
    - [x] 取引先グループ画面を作成する
    - [x] 取引先分類画面を作成する
    - [x] 地域画面を作成する
    - [x] 取引先画面を作成する
    - [x] 顧客画面を作成する
    - [x] 仕入先画面を作成する
- [x] 実行履歴検索対象追加
- [x] ダウンロード対象追加
- [x] 商品アイテムの顧客別販売単価を取引先を選択できるようにする
- [x] 商品アイテムで仕入先を選択できるようにする
- [x] 安全性を考えた例外失敗時の対策を記事にまとめる
- [x] 安全性を確立する実装テクニックについて記事にまとめる
- [x] 設計をより安全なものにする単体テストについて記事にまとめる
- [ ] リリース手順を詳細にまとめる
`;

const usecase = `
@startuml
left to right direction
actor "管理者" as admin
rectangle 取引先管理 {
    rectangle 取引先分類 {
       (取引先分類一覧)
       (取引先分類詳細)
       (取引先分類検索)
       (取引先分類登録)
       (取引先分類編集)
       (取引先分類削除)
    }
    rectangle 取引先グループ {
       (グループ一覧)
       (グループ詳細)
       (グループ検索)
       (グループ登録)
       (グループ編集)
       (グループ削除)
    }
    rectangle 取引先 {
         (取引先一覧)
         (取引先詳細)
         (取引先検索)
         (取引先登録)
         (取引先編集)
         (取引先削除)
        rectangle 顧客 {
             (顧客一覧)
             (顧客詳細)
             (顧客検索)
             (顧客登録)
             (顧客編集)
             (顧客削除)
             
             rectangle 出荷先 {
                    (出荷先一覧)
                    (出荷先登録)
                    (出荷先編集)
                    (出荷先削除)
             }
        }
        rectangle 仕入先 {
             (仕入先一覧)
             (仕入先詳細)
             (仕入先検索)
             (仕入先登録)
             (仕入先編集)
             (仕入先削除)
        }        
    }
        rectangle 地域 {
                (地域一覧)
                (地域詳細)
                (地域検索)
                (地域登録)
                (地域編集)
                (地域削除)
            }
}

admin --> (取引先分類一覧)
admin --> (取引先分類詳細)
admin --> (取引先分類検索)
admin --> (取引先分類登録)
admin --> (取引先分類編集)
admin --> (取引先分類削除)
admin --> (グループ一覧)
admin --> (グループ詳細)
admin --> (グループ検索)
admin --> (グループ登録)
admin --> (グループ編集)
admin --> (グループ削除)
admin --> (取引先一覧)
admin --> (取引先詳細)
admin --> (取引先検索)
admin --> (取引先登録)
admin --> (取引先編集)
admin --> (取引先削除)
admin --> (顧客一覧)
admin --> (顧客詳細)
admin --> (顧客検索)
admin --> (顧客登録)
admin --> (顧客編集)
admin --> (顧客削除)
admin --> (仕入先一覧)
admin --> (仕入先詳細)
admin --> (仕入先検索)
admin --> (仕入先登録)
admin --> (仕入先編集)
admin --> (仕入先削除)
admin --> (出荷先登録)
admin --> (出荷先編集)
admin --> (出荷先削除)
admin --> (出荷先一覧)
admin --> (地域一覧)
admin --> (地域詳細)
admin --> (地域検索)
admin --> (地域登録)

@enduml
`;

const uml = `
@startuml
class 取引先グループ一覧
class 取引先グループ
class 取引先一覧
class 取引先
class 取引先分類種別
class 取引先分類
class 取引先分類所属
class 顧客一覧
class 顧客
class 仕入先一覧
class 仕入先
class 地域一覧
class 地域

取引先グループ一覧 *-- 取引先グループ
取引先グループ o- 取引先
取引先一覧 *-- 取引先
取引先 *-- 顧客
顧客 *-- 出荷先
出荷先一覧 *- 出荷先
取引先 o- 取引先分類所属
取引先分類所属 -* 取引先分類
取引先分類 -* 取引先分類種別
取引先分類一覧 *-- 取引先分類種別
顧客一覧 *- 顧客
取引先 *-- 仕入先
仕入先一覧 *- 仕入先
地域一覧 *-- 地域
出荷先 o- 地域

@enduml
`;

const erd = `
@startuml

entity 取引先グループマスタ {
    + 取引先グループコード [PK]
    --
    + 取引先グループ名
}

entity 取引先分類種別マスタ {
    + 取引先分類種別コード [PK]
    --
    + 種別名
}

entity 取引先分類マスタ {
    + 取引先分類種別コード [PK]
    + 取引先分類コード [FK]
    --
    + 分類名
}

entity 取引先分類所属マスタ {
    + 取引先分類種別コード [FK]
    + 取引先分類コード [FK]
    + 取引先コード [FK]
}

entity 取引先マスタ {
    + 取引先コード [PK]
    --
    + 取引先グループコード [FK]
    + 取引先名
}

entity 顧客マスタ {
    + 取引先コード [FK]
    + 顧客枝番 [PK]
    --
    + 顧客名
}

entity 仕入先マスタ {
    + 取引先コード [FK]
    + 仕入先枝番 [PK]
    --
    + 仕入先名
}

entity 出荷先マスタ {
    + 取引先コード [FK]
    + 顧客枝番 [FK]
    + 出荷先枝番 [PK]
    --
    + 出荷先名 [FK]
    + 地域コード
}

entity 地域マスタ {
    + 地域コード [PK]
    --
    + 地域名
}

取引先グループマスタ ||--o{ 取引先マスタ
取引先分類種別マスタ ||--o{ 取引先分類マスタ
取引先分類マスタ ||--o{ 取引先分類所属マスタ
取引先マスタ ||--o{ 顧客マスタ
顧客マスタ ||--o{ 出荷先マスタ
地域マスタ --o{ 出荷先マスタ
取引先マスタ ||-o{ 取引先分類所属マスタ
取引先マスタ ||--o{ 仕入先マスタ
@enduml
`;

const ui = `
@startsalt
{+
  取引先分類一覧画面（コレクション）
  {+
     {
      ホーム
      ユーザー
      マスタ
      ログアウト
      } |
      {
        {
          <b>取引先分類一覧</b>
          [ 検索 ] | [  新規登録  ] | [ 一括削除  ]
        }

        ---------------------
        {
          1xxxxxx | 分類1    | [  編集  ] | [  削除  ]
          1xxxxxx | 分類2    | [  編集  ] | [  削除  ]
          1xxxxxx | 分類3    | [  編集  ] | [  削除  ]
        }
      }
  }
  ----------------
  取引先分類画面（シングル）
  {+
      {
        [  保存   ]
        ---------------------
        分類種別コード    | "1xxxxxx"   "
        分類コード    | "1xxxxxx"   "
        分類所属コード    | "1xxxxxx"   "
      }
      <b>取引先一覧</b>
      [ 追加 ]
      ---------------------
      {
        1xxxxxx | 取引先1    | [  削除  ]
        1xxxxxx | 取引先2    | [  削除  ]
        1xxxxxx | 取引先3    | [  削除  ]
      }
  }
  ----------------
  取引先グループ一覧画面（コレクション）
  {+
     {
      ホーム
      ユーザー
      マスタ
      ログアウト
      } |
      {
        {
          <b>取引先グループ一覧</b>
          [ 検索 ] | [  新規登録  ] | [ 一括削除  ]
        }

        ---------------------
        {
          1xxxxxx | グループ1    | [  編集  ] | [  削除  ]
          1xxxxxx | グループ2    | [  編集  ] | [  削除  ]
          1xxxxxx | グループ3    | [  編集  ] | [  削除  ]
        }
      }
  }
  ----------------
  取引先グループ画面（シングル）
  {+
      {
        [  保存   ]
        ---------------------
        グループコード    | "1xxxxxx"   "
      }
      <b>取引先一覧</b>
      [ 追加 ]
      ---------------------
      {
        1xxxxxx | 取引先1    | [  削除  ]
        1xxxxxx | 取引先2    | [  削除  ]
        1xxxxxx | 取引先3    | [  削除  ]
      }
  }
  ----------------
  地域一覧画面（コレクション）
  {+
     {
      ホーム
      ユーザー
      マスタ
      ログアウト
      } |
      {
        {
          <b>地域一覧</b>
          [ 検索 ] | [  新規登録  ] | [ 一括削除  ]
        }

        ---------------------
        {
          1xxxxxx | 地域1    | [  編集  ] | [  削除  ]
          1xxxxxx | 地域2    | [  編集  ] | [  削除  ]
          1xxxxxx | 地域3    | [  編集  ] | [  削除  ]
        }
      }
  }
  ----------------
  地域画面（シングル）
  {+
      {
        [  保存   ]
        ---------------------
        地域コード    | "1xxxxxx"   "
        地域名    | "地域1"   "
      }
  }
  ----------------
  取引先一覧画面（コレクション）
  {+
     {
      ホーム
      ユーザー
      マスタ
      ログアウト
      } |
      {
        {
          {/ <b>取引先 | 顧客 | 仕入先}
          [ 検索 ] | [  新規登録  ] | [ 一括削除  ]
        }
      {+
        ---------------------
        {
          1xxxxxx | 取引先1    | [  編集  ] | [  削除  ]
          1xxxxxx | 取引先2    | [  編集  ] | [  削除  ]
          1xxxxxx | 取引先3    | [  編集  ] | [  削除  ]
        }
      }
     }
  }
  ----------------
  取引先画面（シングル）
  {+
      {
        [  保存   ]
        ---------------------
        取引先コード    | "1xxxxxx"   "
        取引先名        | "商品1    "
      }
  }
  ----------------
  顧客一覧画面（コレクション）
  {+
     {
      ホーム
      ユーザー
      マスタ
      ログアウト
      } |
      {
        {
          {/ 取引先 | <b>顧客 | 仕入先}
          [ 検索 ] | [  新規登録  ] | [ 一括削除  ]
        }
      {+
        ---------------------
        {
          1xxxxxx | 顧客1    | [  編集  ] | [  削除  ]
          1xxxxxx | 顧客2    | [  編集  ] | [  削除  ]
          1xxxxxx | 顧客3    | [  編集  ] | [  削除  ]
        }
      }
     }
  }
  ----------------
  顧客画面（シングル）
  {+
      {
        [  保存   ]
        ---------------------
        取引先コード    | "1xxxxxx"   "
        顧客名        | "顧客1    "
      }
      <b>出荷先一覧</b>
      [ 追加 ]
      ---------------------
      {
        1xxxxxx | 出荷先1    | [  削除  ]
        1xxxxxx | 出荷先2    | [  削除  ]
        1xxxxxx | 出荷先3    | [  削除  ]
      }
  }
  ----------------
  仕入先一覧画面（コレクション）
  {+
     {
      ホーム
      ユーザー
      マスタ
      ログアウト
      } |
      {
        {
          {/ 取引先 | 顧客 | <b>仕入先}
          [ 検索 ] | [  新規登録  ] | [ 一括削除  ]
        }
      {+
        ---------------------
        {
          1xxxxxx | 仕入先1    | [  編集  ] | [  削除  ]
          1xxxxxx | 仕入先2    | [  編集  ] | [  削除  ]
          1xxxxxx | 仕入先3    | [  編集  ] | [  削除  ]
        }
      }
     }
  }
  ----------------
  仕入先画面（シングル）
  {+
      {
        [  保存   ]
        ---------------------
        取引先コード    | "1xxxxxx"   "
        仕入先名        | "仕入先1    "
      }
  }
}
@endsalt
`;

const uiModel = `
@startuml
    class 取引先分類一覧 {
        検索()
        一括削除()
        詳細()
        削除()
    }
    class 取引先分類 {
        保存()
        追加()
    }
    class 取引先グループ一覧 {
      検索()
      一括削除()
      詳細()
      削除()
    }
    class 取引先グループ {
      保存()
      追加()
    }
    class 地域一覧 {
        検索()
        一括削除()
        詳細()
        削除()
    }
    class 地域 {
        保存()
    }
    class 取引先一覧 {
        検索()
        一括削除()
        詳細()
        削除()
    }
    class 取引先 {
      保存()
    }
    class 顧客一覧 {
        検索()
        一括削除()
        詳細()
        削除()
    }
    class 顧客 {
      保存()
      追加()
    }
    class 出荷先一覧 {
       削除()
    }
    class 仕入先一覧 {
        検索()
        一括削除()
        詳細()
        削除()
    }
    class 仕入先 {
      保存()
    }
    class 取引先一覧選択 {
    }
  
  class ナビゲーション {
    ホーム()
    マスタ()
    ユーザー()
    ログアウト()
  }

  ナビゲーション --* 取引先分類一覧
  取引先分類一覧 *-- 取引先分類
  取引先分類 *-- 取引先一覧選択
  ナビゲーション --* 取引先グループ一覧
  取引先グループ一覧 *-- 取引先グループ
  取引先グループ *-- 取引先一覧選択
  ナビゲーション --* 地域一覧
  地域一覧 *-- 地域
  ナビゲーション --* 取引先一覧
  取引先一覧 *-- 取引先
  ナビゲーション --* 顧客一覧
  顧客一覧 *-- 顧客
  顧客 *-- 出荷先一覧
  ナビゲーション --* 仕入先一覧
  仕入先一覧 *-- 仕入先
@enduml
`;

const uiInteraction = `
@startuml
  ログイン_シングル --> 取引先分類_コレクション
    取引先分類_コレクション --> 取引先分類_シングル
    取引先分類_シングル --> 取引先分類_コレクション
  ログイン_シングル <-- 取引先分類_コレクション
  
  ログイン_シングル --> 取引先グループ_コレクション
    取引先グループ_コレクション --> 取引先グループ_シングル
    取引先グループ_シングル --> 取引先グループ_コレクション
  ログイン_シングル <-- 取引先グループ_コレクション
    
  ログイン_シングル --> 地域_コレクション
    地域_コレクション --> 地域_シングル
    地域_シングル --> 地域_コレクション
  ログイン_シングル <-- 地域_コレクション
  
  ログイン_シングル --> 取引先_コレクション
    取引先_コレクション --> 取引先_シングル
    取引先_シングル --> 取引先_コレクション
  ログイン_シングル <-- 取引先_コレクション
  
  ログイン_シングル --> 顧客_コレクション
    顧客_コレクション --> 顧客_シングル
    顧客_シングル --> 顧客_コレクション
  ログイン_シングル <-- 顧客_コレクション
  
  ログイン_シングル --> 仕入先_コレクション
    仕入先_コレクション --> 仕入先_シングル
    仕入先_シングル --> 仕入先_コレクション
  ログイン_シングル <-- 仕入先_コレクション
@enduml
`;

const mode = "APP"; // "UI" or "API" or "DOC"
render({ mindmap, contents, ui, uiModel, uiInteraction, usecase, uml, erd, mode });

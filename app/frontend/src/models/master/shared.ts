export type PriceType = {
    amount: number;
    currency: string;
};

export type QuantityType = {
    amount: number;
    unit: string;
}

export type AddressType = {
    postalCode: PostalCodeType; // 郵便番号
    prefecture: PrefectureEnumType; // 都道府県
    address1: string; // 住所1
    address2: string; // 住所2
};

export type PostalCodeType = {
    value: string; // 郵便番号の値
    regionCode: string; // 地域コード
};

export enum PrefectureEnumType {
    北海道 = "北海道",
    青森県 = "青森県",
    岩手県 = "岩手県",
    宮城県 = "宮城県",
    秋田県 = "秋田県",
    山形県 = "山形県",
    福島県 = "福島県",
    茨城県 = "茨城県",
    栃木県 = "栃木県",
    群馬県 = "群馬県",
    埼玉県 = "埼玉県",
    千葉県 = "千葉県",
    東京都 = "東京都",
    神奈川県 = "神奈川県",
    新潟県 = "新潟県",
    富山県 = "富山県",
    石川県 = "石川県",
    福井県 = "福井県",
    山梨県 = "山梨県",
    長野県 = "長野県",
    岐阜県 = "岐阜県",
    静岡県 = "静岡県",
    愛知県 = "愛知県",
    三重県 = "三重県",
    滋賀県 = "滋賀県",
    京都府 = "京都府",
    大阪府 = "大阪府",
    兵庫県 = "兵庫県",
    奈良県 = "奈良県",
    和歌山県 = "和歌山県",
    鳥取県 = "鳥取県",
    島根県 = "島根県",
    岡山県 = "岡山県",
    広島県 = "広島県",
    山口県 = "山口県",
    徳島県 = "徳島県",
    香川県 = "香川県",
    愛媛県 = "愛媛県",
    高知県 = "高知県",
    福岡県 = "福岡県",
    佐賀県 = "佐賀県",
    長崎県 = "長崎県",
    熊本県 = "熊本県",
    大分県 = "大分県",
    宮崎県 = "宮崎県",
    鹿児島県 = "鹿児島県",
    沖縄県 = "沖縄県",
}

// 電話番号型
export type PhoneNumberType = {
    value: string; // 電話番号全体の値
    areaCode: string; // 市外局番
    localExchange: string; // 市内局番
    subscriberNumber: string; // 加入者番号
};

// メールアドレス型
export type EmailType = {
    value: string; // メールアドレス
};

// 締請求型 (既存の定義を流用)
export type ClosingInvoiceType = {
    closingDay: ClosingDateEnumType; // 締日
    paymentMonth: PaymentMonthEnumType; // 支払月
    paymentDay: PaymentDayEnumType; // 支払日
    paymentMethod: PaymentMethodEnumType; // 支払方法
};

// 締日型 (既存の定義を流用、または参考に作成)
export enum ClosingDateEnumType {
    十日 = "十日",
    二十日 = "二十日",
    末日 = "末日",
}

// 支払月型
export enum PaymentMonthEnumType {
    当月 = "当月",
    翌月 = "翌月",
    翌々月 = "翌々月",
}

// 支払日型
export enum PaymentDayEnumType {
    十日 = "十日",
    二十日 = "二十日",
    末日 = "末日",
}

// 支払方法型
export enum PaymentMethodEnumType {
    振込 = "振込",
    手形 = "手形",
}

// 倉庫区分型
export enum WarehouseCategoryEnumType {
    通常倉庫 = "通常倉庫",
    得意先 = "得意先",
    仕入先 = "仕入先",
    部門倉庫 = "部門倉庫",
    製品倉庫 = "製品倉庫",
    原材料倉庫 = "原材料倉庫",
}


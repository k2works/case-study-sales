# 第15章: 請求・回収管理

## 15.1 請求データの生成

### 請求ドメインモデルの概要

請求は、売上データを集計して顧客に対する請求書を発行するプロセスを管理します。締め処理によって売上を集約し、請求データを生成します。

```plantuml
@startuml
title 請求ドメインモデル

class Invoice <<Entity>> {
  - invoiceNumber: InvoiceNumber
  - invoiceDate: InvoiceDate
  - partnerCode: PartnerCode
  - customerCode: CustomerCode
  - previousPaymentAmount: Money
  - currentMonthSalesAmount: Money
  - currentMonthPaymentAmount: Money
  - currentMonthInvoiceAmount: Money
  - consumptionTaxAmount: Money
  - invoiceReconciliationAmount: Money
  - invoiceLines: List<InvoiceLine>
}

class InvoiceLine <<Entity>> {
  - invoiceNumber: InvoiceNumber
  - salesNumber: SalesNumber
  - salesLineNumber: Integer
}

class Sales <<Entity>>
class SalesLine <<Entity>>

Invoice "1" *-- "*" InvoiceLine
InvoiceLine --> Sales : 売上参照

note right of Invoice
  請求の構成要素:
  - 前回入金額
  - 当月売上額
  - 当月入金額
  - 当月請求額
  - 消費税額
end note

@enduml
```

### 売上からの請求作成

請求は締め日に売上データを集計して生成されます。

```plantuml
@startuml
title 請求作成フロー

[*] --> 売上データ収集

売上データ収集 --> 締め処理 : 締め日到来
締め処理 --> 請求データ生成 : 顧客別集計
請求データ生成 --> 請求書発行 : 請求番号採番
請求書発行 --> [*]

state 締め処理 {
  [*] --> 顧客別集計
  顧客別集計 --> 売上合計
  売上合計 --> 消費税計算
  消費税計算 --> 前回繰越計算
}

note right of 締め処理
  締め日:
  - 5日締め
  - 10日締め
  - 15日締め
  - 20日締め
  - 25日締め
  - 末締め
end note

@enduml
```

### 請求エンティティの実装

```java
/**
 * 請求
 */
@Value
@RequiredArgsConstructor
@Builder(toBuilder = true)
public class Invoice {
    final InvoiceNumber invoiceNumber; // 請求番号
    final InvoiceDate invoiceDate; // 請求日
    final PartnerCode partnerCode; // 取引先コード
    final CustomerCode customerCode; // 顧客コード
    final Money previousPaymentAmount; // 前回入金額
    final Money currentMonthSalesAmount; // 当月売上額
    final Money currentMonthPaymentAmount; // 当月入金額
    final Money currentMonthInvoiceAmount; // 当月請求額
    final Money consumptionTaxAmount; // 消費税金額
    final Money invoiceReconciliationAmount; // 請求消込金額
    final List<InvoiceLine> invoiceLines; // 請求明細

    public static Invoice of(
            String invoiceNumber,
            LocalDateTime invoiceDate,
            String partnerCode,
            Integer customerBranchNumber,
            Integer previousPaymentAmount,
            Integer currentMonthSalesAmount,
            Integer currentMonthPaymentAmount,
            Integer currentMonthInvoiceAmount,
            Integer consumptionTaxAmount,
            Integer invoiceReconciliationAmount,
            List<InvoiceLine> invoiceLines) {
        return new Invoice(
            new InvoiceNumber(invoiceNumber),
            InvoiceDate.of(invoiceDate),
            PartnerCode.of(partnerCode),
            CustomerCode.of(partnerCode, customerBranchNumber),
            Money.of(previousPaymentAmount),
            Money.of(currentMonthSalesAmount),
            Money.of(currentMonthPaymentAmount),
            Money.of(currentMonthInvoiceAmount),
            Money.of(consumptionTaxAmount),
            Money.of(invoiceReconciliationAmount),
            invoiceLines != null ? invoiceLines : new ArrayList<>()
        );
    }

    /**
     * 請求明細を追加
     */
    public Invoice addInvoiceLine(InvoiceLine invoiceLine) {
        List<InvoiceLine> newInvoiceLines = new ArrayList<>(this.invoiceLines);
        newInvoiceLines.add(invoiceLine);
        return new Invoice(
            this.invoiceNumber,
            this.invoiceDate,
            this.partnerCode,
            this.customerCode,
            this.previousPaymentAmount,
            this.currentMonthSalesAmount,
            this.currentMonthPaymentAmount,
            this.currentMonthInvoiceAmount,
            this.consumptionTaxAmount,
            this.invoiceReconciliationAmount,
            newInvoiceLines
        );
    }
}
```

### 請求明細の実装

請求明細は、売上データへの参照を保持します。

```java
/**
 * 請求明細
 */
@Value
@RequiredArgsConstructor
@Builder(toBuilder = true)
public class InvoiceLine {
    final InvoiceNumber invoiceNumber; // 請求番号
    final SalesNumber salesNumber; // 売上番号
    final Integer salesLineNumber; // 売上行番号

    public static InvoiceLine of(
            String invoiceNumber,
            String salesNumber,
            Integer salesLineNumber) {
        return new InvoiceLine(
            InvoiceNumber.of(invoiceNumber),
            SalesNumber.of(salesNumber),
            salesLineNumber
        );
    }
}
```

### 請求番号の採番

請求番号は、受注番号と同様に自動採番されます。

```java
/**
 * 請求番号
 */
@Value
@RequiredArgsConstructor
public class InvoiceNumber {
    String value;

    public static InvoiceNumber of(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("請求番号は必須です");
        }
        return new InvoiceNumber(value);
    }

    public static String generate(String code, LocalDateTime yearMonth,
                                  Integer autoNumber) {
        isTrue(code.equals(DocumentTypeCode.請求.getCode()),
               "請求番号は先頭が" + DocumentTypeCode.請求.getCode() +
               "で始まる必要があります");
        return code +
               yearMonth.format(DateTimeFormatter.ofPattern("yyMM")) +
               String.format("%04d", autoNumber);
    }
}
```

### 締め処理の設計

締め処理は、顧客の請求条件に基づいて実行されます。

```plantuml
@startuml
title 締め処理の流れ

participant "締めバッチ" as batch
participant "顧客マスタ" as customer
participant "売上データ" as sales
participant "請求データ" as invoice

batch -> customer : 締め日該当顧客を取得
customer --> batch : 顧客リスト

loop 顧客ごとに処理
  batch -> sales : 未請求売上を取得
  sales --> batch : 売上データリスト

  batch -> batch : 金額集計
  batch -> invoice : 請求データ生成

  batch -> sales : 請求番号を設定
end

@enduml
```

---

## 15.2 入金管理

### 入金ドメインモデル

入金は、顧客からの支払いを記録し、請求との消込を行います。

```plantuml
@startuml
title 入金・消込の構造

class Payment <<Entity>> {
  - paymentNumber: PaymentNumber
  - paymentDate: PaymentDate
  - customerCode: CustomerCode
  - paymentAmount: Money
  - paymentMethod: PaymentMethod
}

class InvoiceReconciliation <<Entity>> {
  - invoiceNumber: InvoiceNumber
  - paymentNumber: PaymentNumber
  - reconciliationAmount: Money
  - reconciliationDate: LocalDateTime
}

class Invoice <<Entity>> {
  - invoiceNumber: InvoiceNumber
  - currentMonthInvoiceAmount: Money
  - invoiceReconciliationAmount: Money
}

Payment "1" --> "*" InvoiceReconciliation
InvoiceReconciliation --> Invoice

note right of InvoiceReconciliation
  消込処理:
  1. 入金を登録
  2. 請求と入金を紐付け
  3. 消込金額を更新
end note

@enduml
```

### 入金登録の流れ

```plantuml
@startuml
title 入金登録フロー

[*] --> 入金データ入力

入金データ入力 --> 入金先特定 : 顧客コード
入金先特定 --> 未消込請求検索 : 請求一覧取得
未消込請求検索 --> 消込処理 : 請求選択

state 消込処理 {
  [*] --> 自動消込
  自動消込 --> 手動消込 : 残高あり
  手動消込 --> [*]
  自動消込 --> [*] : 完全消込
}

消込処理 --> 入金完了 : 消込額確定
入金完了 --> [*]

@enduml
```

### 請求残高の計算

```plantuml
@startuml
title 請求残高の構成

rectangle "前月繰越残高" as prev
rectangle "当月売上" as sales
rectangle "当月入金" as payment
rectangle "当月請求残高" as balance

prev --> balance : +
sales --> balance : +
payment --> balance : -

note bottom of balance
  請求残高 = 前月繰越 + 当月売上 - 当月入金

  完全消込: 残高 = 0
  部分消込: 残高 > 0
end note

@enduml
```

### 消込処理の実装

```java
/**
 * 消込処理サービス
 */
@Service
@RequiredArgsConstructor
public class ReconciliationService {
    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;

    /**
     * 自動消込処理
     * FIFO（先入先出）方式で古い請求から消込
     */
    public List<InvoiceReconciliation> autoReconcile(
            CustomerCode customerCode,
            Money paymentAmount) {

        // 未消込請求を請求日順に取得
        List<Invoice> unpaidInvoices = invoiceRepository
            .findUnpaidByCustomer(customerCode)
            .stream()
            .sorted(Comparator.comparing(
                i -> i.getInvoiceDate().getValue()))
            .collect(Collectors.toList());

        List<InvoiceReconciliation> reconciliations = new ArrayList<>();
        Money remainingAmount = paymentAmount;

        for (Invoice invoice : unpaidInvoices) {
            if (remainingAmount.isZero()) {
                break;
            }

            Money invoiceBalance = invoice.getCurrentMonthInvoiceAmount()
                .subtract(invoice.getInvoiceReconciliationAmount());

            Money reconcileAmount = remainingAmount.isGreaterThan(invoiceBalance)
                ? invoiceBalance
                : remainingAmount;

            reconciliations.add(new InvoiceReconciliation(
                invoice.getInvoiceNumber(),
                reconcileAmount,
                LocalDateTime.now()
            ));

            remainingAmount = remainingAmount.subtract(reconcileAmount);
        }

        return reconciliations;
    }
}
```

---

## 請求・入金の状態管理

### 請求ステータス

```plantuml
@startuml
title 請求ステータスの遷移

[*] --> 請求作成

請求作成 --> 請求発行 : 請求書発行
請求発行 --> 部分入金 : 一部入金
請求発行 --> 消込完了 : 全額入金
部分入金 --> 部分入金 : 追加入金
部分入金 --> 消込完了 : 残額入金

消込完了 --> [*]

state 請求発行 {
  state "未入金" as unpaid
}

state 部分入金 {
  state "残高あり" as partial
}

state 消込完了 {
  state "残高なし" as paid
}

@enduml
```

### 入金方法の定義

```java
/**
 * 入金方法
 */
public enum PaymentMethodType {
    銀行振込(1, "銀行口座への振込"),
    現金(2, "現金による支払い"),
    手形(3, "手形による支払い"),
    小切手(4, "小切手による支払い"),
    相殺(5, "債権債務の相殺"),
    その他(9, "その他の方法");

    private final int code;
    private final String description;

    PaymentMethodType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static PaymentMethodType fromCode(int code) {
        for (PaymentMethodType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("不正な入金方法: " + code);
    }
}
```

---

## TDD によるテスト

### 請求作成のテスト

```java
@DisplayName("請求")
class InvoiceTest {

    @Test
    @DisplayName("請求を作成できる")
    void shouldCreateInvoice() {
        Invoice invoice = Invoice.of(
            "IV24010001",
            LocalDateTime.of(2024, 1, 31, 0, 0),
            "001",
            1,
            10000,  // 前回入金額
            50000,  // 当月売上額
            0,      // 当月入金額
            60000,  // 当月請求額
            5000,   // 消費税額
            0,      // 請求消込金額
            List.of()
        );

        assertAll(
            () -> assertEquals("IV24010001", invoice.getInvoiceNumber().getValue()),
            () -> assertEquals("001", invoice.getPartnerCode().getValue()),
            () -> assertEquals(10000, invoice.getPreviousPaymentAmount().getAmount()),
            () -> assertEquals(50000, invoice.getCurrentMonthSalesAmount().getAmount()),
            () -> assertEquals(60000, invoice.getCurrentMonthInvoiceAmount().getAmount())
        );
    }

    @Test
    @DisplayName("請求明細を追加できる")
    void shouldAddInvoiceLine() {
        Invoice invoice = Invoice.of(
            "IV24010001",
            LocalDateTime.now(),
            "001", 1,
            0, 10000, 0, 10000, 1000, 0,
            List.of()
        );

        InvoiceLine line = InvoiceLine.of("IV24010001", "SL24010001", 1);
        Invoice updatedInvoice = invoice.addInvoiceLine(line);

        assertEquals(1, updatedInvoice.getInvoiceLines().size());
        assertEquals("SL24010001",
            updatedInvoice.getInvoiceLines().get(0).getSalesNumber().getValue());
    }
}
```

### 請求番号のテスト

```java
@Nested
@DisplayName("請求番号")
class InvoiceNumberTest {

    @Test
    @DisplayName("請求番号を生成できる")
    void shouldGenerateInvoiceNumber() {
        LocalDateTime yearMonth = LocalDateTime.of(2024, 1, 1, 0, 0);
        String invoiceNumber = InvoiceNumber.generate("IV", yearMonth, 1);

        assertEquals("IV24010001", invoiceNumber);
    }

    @Test
    @DisplayName("請求番号は必須")
    void shouldThrowExceptionWhenInvoiceNumberIsEmpty() {
        assertThrows(IllegalArgumentException.class,
            () -> InvoiceNumber.of(null));
        assertThrows(IllegalArgumentException.class,
            () -> InvoiceNumber.of(""));
    }
}
```

### 消込処理のテスト

```java
@Nested
@DisplayName("消込処理")
class ReconciliationTest {

    @Test
    @DisplayName("請求額と入金額が一致する場合は完全消込")
    void shouldFullyReconcileWhenAmountsMatch() {
        Invoice invoice = createInvoiceWithAmount(10000);
        Money paymentAmount = Money.of(10000);

        // 消込後の残高は0
        Money balance = invoice.getCurrentMonthInvoiceAmount()
            .subtract(paymentAmount);

        assertEquals(0, balance.getAmount());
    }

    @Test
    @DisplayName("入金額が請求額より少ない場合は部分消込")
    void shouldPartiallyReconcileWhenPaymentIsLess() {
        Invoice invoice = createInvoiceWithAmount(10000);
        Money paymentAmount = Money.of(5000);

        Money balance = invoice.getCurrentMonthInvoiceAmount()
            .subtract(paymentAmount);

        assertEquals(5000, balance.getAmount());
    }

    @Test
    @DisplayName("複数請求への自動消込（FIFO方式）")
    void shouldAutoReconcileMultipleInvoices() {
        // 請求1: 10,000円（古い）
        // 請求2: 20,000円（新しい）
        // 入金: 25,000円
        // 期待結果: 請求1は完全消込、請求2は15,000円消込

        List<Invoice> invoices = List.of(
            createInvoiceWithAmountAndDate(10000,
                LocalDateTime.of(2024, 1, 10, 0, 0)),
            createInvoiceWithAmountAndDate(20000,
                LocalDateTime.of(2024, 1, 20, 0, 0))
        );

        Money paymentAmount = Money.of(25000);
        Money remaining = paymentAmount;

        for (Invoice invoice : invoices) {
            Money reconcileAmount = remaining.isGreaterThan(
                invoice.getCurrentMonthInvoiceAmount())
                ? invoice.getCurrentMonthInvoiceAmount()
                : remaining;
            remaining = remaining.subtract(reconcileAmount);
        }

        assertEquals(5000, remaining.getAmount()); // 残り5,000円未消込
    }
}
```

---

## 15.3 React コンポーネントの実装

### 請求・入金画面のコンポーネント構成

請求・回収管理画面は、請求一覧、入金一覧、集計画面で構成されています。

```plantuml
@startuml
title 請求・回収管理のコンポーネント構成

package "請求管理" {
  class InvoiceContainer {
    + fetchInvoices()
    + handleDelete()
  }
  class InvoiceAggregateContainer {
    + fetchAggregates()
  }
}

package "入金管理" {
  class PaymentContainer {
    + fetchPayments()
    + handleCreateOrUpdate()
    + handleDelete()
  }
  class PaymentAggregateContainer {
    + fetchAggregates()
  }
}

package "View" {
  class InvoiceCollectionView
  class InvoiceSingleView
  class InvoiceAggregateCollectionView
  class PaymentCollectionView
  class PaymentSingleView
  class PaymentAggregateCollectionView
}

InvoiceContainer --> InvoiceCollectionView
InvoiceContainer --> InvoiceSingleView
InvoiceAggregateContainer --> InvoiceAggregateCollectionView
PaymentContainer --> PaymentCollectionView
PaymentContainer --> PaymentSingleView
PaymentAggregateContainer --> PaymentAggregateCollectionView

@enduml
```

### 請求一覧画面の実装

請求一覧画面では、請求番号、請求日、顧客コード、当月請求額を表示します。

```typescript
interface InvoiceCollectionViewProps {
    error: string | null;
    message: string | null;
    searchItems: {
        searchInvoiceCriteria: InvoiceCriteriaType;
        setSearchInvoiceCriteria: React.Dispatch<React.SetStateAction<InvoiceCriteriaType>>;
        handleOpenSearchModal: () => void;
    };
    headerItems: {
        handleOpenModal: (invoice?: InvoiceType) => void;
        handleCheckToggleCollection: () => void;
        handleDeleteCheckedCollection: () => void;
    };
    collectionItems: {
        invoices: InvoiceType[];
        handleOpenModal: (invoice: InvoiceType) => void;
        handleDeleteInvoice: (invoiceNumber: string) => void;
        handleCheckInvoice: (invoice: InvoiceType) => void;
    };
    pageNationItems: PageNationItemsProps;
}

export const InvoiceCollectionView: React.FC<Props> = ({
    error,
    message,
    searchItems,
    headerItems,
    collectionItems,
    pageNationItems
}) => (
    <div className="collection-view-object-container">
        <Message error={error} message={message} />
        <div className="collection-view-container">
            <div className="collection-view-header">
                <h1 className="single-view-title">請求一覧</h1>
            </div>
            <div className="collection-view-content">
                <Search
                    searchCriteria={searchItems.searchInvoiceCriteria}
                    setSearchCriteria={searchItems.setSearchInvoiceCriteria}
                    handleSearchAudit={searchItems.handleOpenSearchModal}
                />
                <div className="button-container">
                    <button onClick={headerItems.handleCheckToggleCollection}>
                        一括選択
                    </button>
                    <button onClick={headerItems.handleDeleteCheckedCollection}>
                        一括削除
                    </button>
                </div>
                <ul className="collection-object-list">
                    {collectionItems.invoices.map((invoice) => (
                        <li key={invoice.invoiceNumber}>
                            <input
                                type="checkbox"
                                checked={invoice.checked || false}
                                onChange={() => collectionItems.handleCheckInvoice(invoice)}
                            />
                            <div>請求番号: {invoice.invoiceNumber}</div>
                            <div>請求日: {invoice.invoiceDate.split("T")[0]}</div>
                            <div>顧客コード: {invoice.customerCode}</div>
                            <div>当月請求額: {invoice.currentMonthInvoiceAmount}</div>
                            <button onClick={() => collectionItems.handleOpenModal(invoice)}>
                                編集
                            </button>
                            <button onClick={() =>
                                collectionItems.handleDeleteInvoice(invoice.invoiceNumber)}>
                                削除
                            </button>
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    </div>
);
```

### 消込処理 UI

入金詳細画面では、請求との消込処理を行います。未消込の請求を選択して消込金額を入力します。

```plantuml
@startuml
title 消込処理の画面遷移

actor ユーザー
participant "入金一覧" as PaymentList
participant "入金詳細" as PaymentDetail
participant "請求選択モーダル" as InvoiceModal
participant "API" as API

ユーザー -> PaymentList : 新規入金登録
PaymentList -> PaymentDetail : モーダル表示
ユーザー -> PaymentDetail : 顧客コード選択
PaymentDetail -> InvoiceModal : 未消込請求一覧表示
InvoiceModal -> API : 未消込請求取得
API --> InvoiceModal : 請求データ
ユーザー -> InvoiceModal : 消込対象請求を選択
InvoiceModal -> PaymentDetail : 請求情報設定
ユーザー -> PaymentDetail : 消込金額入力
PaymentDetail -> API : 入金・消込登録
API --> PaymentDetail : 登録完了

@enduml
```

### 入金詳細画面の実装

```typescript
interface PaymentSingleViewProps {
    error: string | null;
    message: string | null;
    isEditing: boolean;
    newPayment: PaymentType;
    setNewPayment: React.Dispatch<React.SetStateAction<PaymentType>>;
    handleCreateOrUpdatePayment: () => void;
    handleCloseModal: () => void;
    handleCustomerSelect: () => void;
    handleInvoiceSelect: () => void;
}

const PaymentForm: React.FC<PaymentFormProps> = ({
    isEditing,
    newPayment,
    setNewPayment,
    handleCustomerSelect,
    handleInvoiceSelect
}) => (
    <div className="single-view-content-item-form">
        <FormInput
            label="入金番号"
            id="paymentNumber"
            value={newPayment.paymentNumber}
            onChange={(e) => setNewPayment({
                ...newPayment,
                paymentNumber: e.target.value
            })}
            disabled={isEditing}
        />
        <FormInput
            label="入金日"
            id="paymentDate"
            type="date"
            value={convertToDateInputFormat(newPayment.paymentDate)}
            onChange={(e) => setNewPayment({
                ...newPayment,
                paymentDate: e.target.value
            })}
        />
        <FormInput
            label="顧客コード"
            id="customerCode"
            value={newPayment.customerCode}
            onClick={handleCustomerSelect}
        />
        <FormInput
            label="入金金額"
            id="paymentAmount"
            type="number"
            value={newPayment.paymentAmount}
            onChange={(e) => setNewPayment({
                ...newPayment,
                paymentAmount: parseInt(e.target.value)
            })}
        />
        <FormSelect
            label="入金方法"
            id="paymentMethod"
            value={newPayment.paymentMethod}
            options={PaymentMethodEnumType}
            onChange={(e) => setNewPayment({
                ...newPayment,
                paymentMethod: e
            })}
        />
        <FormInput
            label="請求番号"
            id="invoiceNumber"
            value={newPayment.invoiceNumber ?? ""}
            onClick={handleInvoiceSelect}
        />
        <FormInput
            label="消込金額"
            id="reconciliationAmount"
            type="number"
            value={newPayment.reconciliationAmount ?? 0}
            onChange={(e) => setNewPayment({
                ...newPayment,
                reconciliationAmount: parseInt(e.target.value)
            })}
        />
    </div>
);
```

### 集計画面の実装

請求集計画面では、顧客別・期間別の請求残高を表示します。

```typescript
interface InvoiceAggregateCollectionViewProps {
    aggregates: InvoiceAggregateType[];
    criteria: InvoiceAggregateCriteriaType;
    setCriteria: React.Dispatch<React.SetStateAction<InvoiceAggregateCriteriaType>>;
    handleSearch: () => void;
}

export const InvoiceAggregateCollectionView: React.FC<Props> = ({
    aggregates,
    criteria,
    setCriteria,
    handleSearch
}) => (
    <div className="collection-view-object-container">
        <div className="collection-view-header">
            <h1>請求集計</h1>
        </div>
        <div className="collection-view-content">
            <div className="search-container">
                <FormInput
                    label="集計開始日"
                    type="date"
                    value={criteria.startDate}
                    onChange={(e) => setCriteria({
                        ...criteria,
                        startDate: e.target.value
                    })}
                />
                <FormInput
                    label="集計終了日"
                    type="date"
                    value={criteria.endDate}
                    onChange={(e) => setCriteria({
                        ...criteria,
                        endDate: e.target.value
                    })}
                />
                <button onClick={handleSearch}>集計</button>
            </div>
            <table className="aggregate-table">
                <thead>
                    <tr>
                        <th>顧客コード</th>
                        <th>顧客名</th>
                        <th>請求金額</th>
                        <th>入金金額</th>
                        <th>残高</th>
                    </tr>
                </thead>
                <tbody>
                    {aggregates.map((agg) => (
                        <tr key={agg.customerCode}>
                            <td>{agg.customerCode}</td>
                            <td>{agg.customerName}</td>
                            <td>{agg.invoiceAmount.toLocaleString()}</td>
                            <td>{agg.paymentAmount.toLocaleString()}</td>
                            <td>{agg.balance.toLocaleString()}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    </div>
);
```

### 型定義

```typescript
// 請求の型定義
export interface InvoiceType {
    invoiceNumber: string;
    invoiceDate: string;
    partnerCode: string;
    customerCode: string;
    customerBranchNumber: number;
    previousPaymentAmount: number;
    currentMonthSalesAmount: number;
    currentMonthPaymentAmount: number;
    currentMonthInvoiceAmount: number;
    consumptionTaxAmount: number;
    invoiceReconciliationAmount: number;
    invoiceLines: InvoiceLineType[];
    checked?: boolean;
}

// 請求明細の型定義
export interface InvoiceLineType {
    invoiceNumber: string;
    salesNumber: string;
    salesLineNumber: number;
}

// 入金の型定義
export interface PaymentType {
    paymentNumber: string;
    paymentDate: string;
    customerCode: string;
    customerBranchNumber: number;
    paymentAmount: number;
    paymentMethod: PaymentMethodEnumType;
    invoiceNumber?: string;
    reconciliationAmount?: number;
    checked?: boolean;
}

// 入金方法の列挙型
export enum PaymentMethodEnumType {
    銀行振込 = "銀行振込",
    現金 = "現金",
    手形 = "手形",
    小切手 = "小切手",
    相殺 = "相殺",
    その他 = "その他"
}

// 請求集計の型定義
export interface InvoiceAggregateType {
    customerCode: string;
    customerName: string;
    invoiceAmount: number;
    paymentAmount: number;
    balance: number;
}
```

---

## まとめ

本章では、請求・回収管理の実装について解説しました。

- **請求データの生成**: 売上からの請求作成、締め処理、請求番号の採番
- **入金管理**: 入金登録、FIFO方式による自動消込、残高管理
- **React コンポーネント**: 請求・入金画面、消込処理 UI、集計画面

第4部「販売管理機能」では、受注から請求・回収までの一連の販売プロセスを実装しました。次の第5部では、調達管理機能として発注・仕入・支払の実装に進みます。

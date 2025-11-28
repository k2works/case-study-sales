# 第17章: 仕入・支払管理

## 17.1 集中開発の実例

### 小さなステップの積み重ね

仕入・支払管理機能は、発注管理を基盤として構築されました。発注から仕入、そして支払への一連のワークフローを実現するために、小さなステップを積み重ねる TDD アプローチを採用しています。

```plantuml
@startuml
title 調達管理の全体フロー

[*] --> 発注作成
発注作成 --> 発注確定 : 登録
発注確定 --> 入荷待ち : 仕入先送付

入荷待ち --> 仕入確認 : 商品入荷
仕入確認 --> 仕入登録 : 検品完了

仕入登録 --> 支払予定生成 : 締め処理
支払予定生成 --> 支払実行 : 支払日到来
支払実行 --> 支払完了 : 振込/決済
支払完了 --> [*]

note right of 発注確定
  PurchaseOrder エンティティ
  - 発注明細管理
  - 納期管理
end note

note right of 仕入登録
  Purchase エンティティ
  - 発注との紐付け
  - 入荷数量管理
end note

note right of 支払完了
  PurchasePayment エンティティ
  - 仕入先ごとに集計
  - 支払方法管理
end note

@enduml
```

### コミット履歴に見る開発プロセス

実際の開発では、以下のような小さなコミットを積み重ねています。

```
feat: 仕入エンティティの基本実装
feat: 仕入明細の金額計算ロジック追加
test: 仕入サービスの単体テスト作成
feat: 支払エンティティの実装
feat: 支払方法区分の列挙型追加
feat: 仕入から支払への集計機能
refactor: 金額計算ロジックの共通化
test: 支払集計処理のテスト追加
```

各コミットは単一の責任を持ち、テストがパスする状態を維持しています。

## 17.2 仕入管理

### 仕入ドメインモデルの概要

仕入は、発注に基づいて実際に商品を受け取る処理を管理します。発注との紐付けにより、発注から仕入までの追跡が可能です。

```plantuml
@startuml
title 仕入ドメインモデル

class Purchase <<Entity>> {
  - purchaseNumber: PurchaseNumber
  - purchaseDate: PurchaseDate
  - supplierCode: SupplierCode
  - purchaseManagerCode: EmployeeCode
  - startDate: LocalDateTime
  - purchaseOrderNumber: PurchaseOrderNumber
  - departmentCode: DepartmentCode
  - totalPurchaseAmount: Money
  - totalConsumptionTax: Money
  - remarks: String
  + calcTotalPurchaseAmount(): Money
  + calcTotalConsumptionTax(): Money
}

class PurchaseLine <<Entity>> {
  - purchaseNumber: PurchaseNumber
  - purchaseLineNumber: Integer
  - purchaseLineDisplayNumber: Integer
  - purchaseOrderLineNumber: Integer
  - productCode: ProductCode
  - warehouseCode: WarehouseCode
  - productName: String
  - purchaseUnitPrice: Money
  - purchaseQuantity: Quantity
  + calcPurchaseAmount(): Money
  + calcConsumptionTax(): Money
}

class PurchaseNumber <<Value Object>> {
  - value: String
}

class PurchaseDate <<Value Object>> {
  - value: LocalDateTime
}

Purchase "1" *-- "*" PurchaseLine
Purchase --> PurchaseNumber
Purchase --> PurchaseDate

note right of Purchase
  仕入の特徴:
  - 発注番号で発注と紐付け
  - 部門ごとの管理
  - 倉庫への入庫管理
end note

@enduml
```

### 仕入エンティティの実装

仕入エンティティは、商品の入荷情報を管理し、金額を自動計算します。

```java
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder(toBuilder = true)
public class Purchase {
    PurchaseNumber purchaseNumber; // 仕入番号
    PurchaseDate purchaseDate; // 仕入日
    SupplierCode supplierCode; // 仕入先コード
    EmployeeCode purchaseManagerCode; // 仕入担当者コード
    LocalDateTime startDate; // 開始日
    PurchaseOrderNumber purchaseOrderNumber; // 発注番号
    DepartmentCode departmentCode; // 部門コード
    Money totalPurchaseAmount; // 仕入金額合計
    Money totalConsumptionTax; // 消費税合計
    String remarks; // 備考
    List<PurchaseLine> purchaseLines; // 仕入明細
    Supplier supplier; // 仕入先
    Employee purchaseManager; // 仕入担当者

    public static Purchase of(
            String purchaseNumber,
            LocalDateTime purchaseDate,
            String supplierCode,
            Integer supplierBranchNumber,
            String purchaseManagerCode,
            LocalDateTime startDate,
            String purchaseOrderNumber,
            String departmentCode,
            Integer totalPurchaseAmount,
            Integer totalConsumptionTax,
            String remarks,
            List<PurchaseLine> purchaseLines) {

        // 金額を明細から自動計算
        Money calcTotalPurchaseAmount = purchaseLines.stream()
                .map(PurchaseLine::calcPurchaseAmount)
                .reduce(Money.of(0), Money::plusMoney);

        Money calcTotalConsumptionTax = purchaseLines.stream()
                .map(PurchaseLine::calcConsumptionTax)
                .reduce(Money.of(0), Money::plusMoney);

        return Purchase.builder()
                .purchaseNumber(PurchaseNumber.of(purchaseNumber))
                .purchaseDate(PurchaseDate.of(purchaseDate))
                .supplierCode(SupplierCode.of(supplierCode, supplierBranchNumber))
                .purchaseManagerCode(EmployeeCode.of(purchaseManagerCode))
                .startDate(startDate)
                .purchaseOrderNumber(purchaseOrderNumber != null
                        ? PurchaseOrderNumber.of(purchaseOrderNumber)
                        : null)
                .departmentCode(DepartmentCode.of(departmentCode))
                .totalPurchaseAmount(calcTotalPurchaseAmount)
                .totalConsumptionTax(calcTotalConsumptionTax)
                .remarks(remarks)
                .purchaseLines(purchaseLines)
                .build();
    }

    /**
     * 仕入金額合計計算
     */
    public Money calcTotalPurchaseAmount() {
        return purchaseLines.stream()
                .map(PurchaseLine::calcPurchaseAmount)
                .reduce(Money.of(0), Money::plusMoney);
    }

    /**
     * 消費税合計計算
     */
    public Money calcTotalConsumptionTax() {
        return purchaseLines.stream()
                .map(PurchaseLine::calcConsumptionTax)
                .reduce(Money.of(0), Money::plusMoney);
    }
}
```

### 仕入明細エンティティ

仕入明細は、入荷した商品の詳細情報を管理します。

```java
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder(toBuilder = true)
public class PurchaseLine {
    PurchaseNumber purchaseNumber; // 仕入番号
    Integer purchaseLineNumber; // 仕入行番号
    Integer purchaseLineDisplayNumber; // 仕入行表示番号
    Integer purchaseOrderLineNumber; // 発注行番号
    ProductCode productCode; // 商品コード
    WarehouseCode warehouseCode; // 倉庫コード
    String productName; // 商品名
    Money purchaseUnitPrice; // 仕入単価
    Quantity purchaseQuantity; // 仕入数量
    LocalDateTime createdDateTime; // 作成日時
    String createdBy; // 作成者名
    LocalDateTime updatedDateTime; // 更新日時
    String updatedBy; // 更新者名
    Integer version; // バージョン
    Product product; // 商品

    public static PurchaseLine of(
            String purchaseNumber,
            Integer purchaseLineNumber,
            Integer purchaseLineDisplayNumber,
            Integer purchaseOrderLineNumber,
            String productCode,
            String warehouseCode,
            String productName,
            Integer purchaseUnitPrice,
            Integer purchaseQuantity) {

        return PurchaseLine.builder()
                .purchaseNumber(PurchaseNumber.of(purchaseNumber))
                .purchaseLineNumber(purchaseLineNumber)
                .purchaseLineDisplayNumber(purchaseLineDisplayNumber)
                .purchaseOrderLineNumber(purchaseOrderLineNumber)
                .productCode(ProductCode.of(productCode))
                .warehouseCode(WarehouseCode.of(warehouseCode))
                .productName(productName)
                .purchaseUnitPrice(Money.of(purchaseUnitPrice))
                .purchaseQuantity(Quantity.of(purchaseQuantity))
                .build();
    }

    /**
     * 仕入金額計算（消費税抜き）
     */
    public Money calcPurchaseAmount() {
        return purchaseUnitPrice.multiply(purchaseQuantity);
    }

    /**
     * 消費税計算
     */
    public Money calcConsumptionTax() {
        double taxRate = 0.10;
        int lineTotal = purchaseUnitPrice.getAmount() * purchaseQuantity.getAmount();
        return Money.of((int) (lineTotal * taxRate));
    }
}
```

### 仕入ステータス管理

仕入と発注の関係は、発注番号を通じて管理されます。

```plantuml
@startuml
title 発注と仕入の連携

class PurchaseOrder <<Entity>> {
  - purchaseOrderNumber: PurchaseOrderNumber
  - purchaseOrderLines: List<PurchaseOrderLine>
}

class Purchase <<Entity>> {
  - purchaseNumber: PurchaseNumber
  - purchaseOrderNumber: PurchaseOrderNumber
  - purchaseLines: List<PurchaseLine>
}

class PurchaseOrderLine <<Entity>> {
  - purchaseOrderQuantity: Quantity
  - receivedQuantity: Quantity
  - completionFlag: CompletionFlag
}

class PurchaseLine <<Entity>> {
  - purchaseOrderLineNumber: Integer
  - purchaseQuantity: Quantity
}

PurchaseOrder "1" <-- "*" Purchase : 発注に基づく仕入
PurchaseOrder "1" *-- "*" PurchaseOrderLine
Purchase "1" *-- "*" PurchaseLine
PurchaseOrderLine "1" <-- "*" PurchaseLine : 発注明細との紐付け

note bottom of PurchaseOrderLine
  receivedQuantity は仕入によって更新
  purchaseOrderQuantity == receivedQuantity で完了
end note

@enduml
```

### 仕入サービスの実装

仕入サービスは、仕入の CRUD 操作とビジネスロジックを提供します。

```java
@Service
@Transactional
@Slf4j
public class PurchaseService {
    final PurchaseRepository purchaseRepository;
    final PurchaseDomainService purchaseDomainService;
    final AutoNumberService autoNumberService;

    /**
     * 仕入新規登録
     */
    public void register(Purchase purchase) {
        if (purchase.getPurchaseNumber() == null) {
            String purchaseNumber = generatePurchaseNumber(purchase);

            purchase = Purchase.of(
                    purchaseNumber,
                    Objects.requireNonNull(purchase.getPurchaseDate().getValue()),
                    Objects.requireNonNull(purchase.getSupplierCode().getCode()).getValue(),
                    purchase.getSupplierCode().getBranchNumber(),
                    Objects.requireNonNull(purchase.getPurchaseManagerCode()).getValue(),
                    Objects.requireNonNull(purchase.getStartDate()),
                    purchase.getPurchaseOrderNumber() != null
                            ? purchase.getPurchaseOrderNumber().getValue()
                            : null,
                    Objects.requireNonNull(purchase.getDepartmentCode()).getValue(),
                    Objects.requireNonNull(purchase.getTotalPurchaseAmount()).getAmount(),
                    Objects.requireNonNull(purchase.getTotalConsumptionTax()).getAmount(),
                    purchase.getRemarks(),
                    Objects.requireNonNull(purchase.getPurchaseLines())
            );
        }
        purchaseRepository.save(purchase);
    }

    /**
     * 仕入番号生成
     * 形式: SI + YYMM + 連番4桁 (例: SI25010001)
     */
    private String generatePurchaseNumber(Purchase purchase) {
        String code = DocumentTypeCode.仕入.getCode();
        LocalDateTime purchaseDate = Objects.requireNonNull(purchase.getPurchaseDate().getValue());
        LocalDateTime yearMonth = YearMonth.of(
                purchaseDate.getYear(),
                purchaseDate.getMonth()
        ).atDay(1).atStartOfDay();

        Integer autoNumber = autoNumberService.getNextDocumentNumber(code, yearMonth);
        String purchaseNumber = code
                + yearMonth.format(DateTimeFormatter.ofPattern("yyMM"))
                + String.format("%04d", autoNumber);

        autoNumberService.save(AutoNumber.of(code, yearMonth, autoNumber));
        autoNumberService.incrementDocumentNumber(code, yearMonth);

        return purchaseNumber;
    }
}
```

### 仕入ルールの実装

仕入にも発注と同様のルールチェック機構があります。

```java
/**
 * 仕入ルール（抽象基底クラス）
 */
public abstract class PurchaseRule {
    public abstract boolean isSatisfiedBy(Purchase purchase);
    public abstract boolean isSatisfiedBy(PurchaseLine purchaseLine);
    public abstract boolean isSatisfiedBy(Purchase purchase, PurchaseLine purchaseLine);
}

/**
 * 仕入金額ルール
 * 仕入金額が500万円を超過している場合は要確認とする
 */
public class PurchaseAmountRule extends PurchaseRule {
    private static final Money THRESHOLD = Money.of(5000000);

    @Override
    public boolean isSatisfiedBy(Purchase purchase) {
        return purchase.getTotalPurchaseAmount().isGreaterThan(THRESHOLD);
    }

    @Override
    public boolean isSatisfiedBy(PurchaseLine purchaseLine) {
        return false;
    }

    @Override
    public boolean isSatisfiedBy(Purchase purchase, PurchaseLine purchaseLine) {
        return false;
    }
}
```

```plantuml
@startuml
title 仕入ルールの Strategy パターン

abstract class PurchaseRule {
  + {abstract} isSatisfiedBy(purchase): boolean
  + {abstract} isSatisfiedBy(purchaseLine): boolean
  + {abstract} isSatisfiedBy(purchase, purchaseLine): boolean
}

class PurchaseAmountRule {
  - THRESHOLD: Money = 5,000,000
  + isSatisfiedBy(purchase): boolean
}

class PurchaseDateRule {
  + isSatisfiedBy(purchase): boolean
}

PurchaseRule <|-- PurchaseAmountRule
PurchaseRule <|-- PurchaseDateRule

note bottom of PurchaseAmountRule
  仕入金額が500万円を
  超過している場合は要確認
end note

note bottom of PurchaseDateRule
  仕入日が未来日の
  場合はエラー
end note

@enduml
```

### 仕入ドメインサービス

仕入ドメインサービスは、複数のルールを適用してチェック結果を返します。

```java
@Service
public class PurchaseDomainService {

    /**
     * 仕入金額合計計算
     */
    public Money calculateTotalPurchaseAmount(Purchase purchase) {
        if (purchase.getPurchaseLines() == null || purchase.getPurchaseLines().isEmpty()) {
            return Money.of(0);
        }
        return purchase.calcTotalPurchaseAmount();
    }

    /**
     * 仕入ルールチェック
     */
    public PurchaseRuleCheckList checkRule(PurchaseList purchases) {
        List<Map<String, String>> checkList = new ArrayList<>();

        List<Purchase> purchaseList = purchases.asList();
        PurchaseRule purchaseAmountRule = new PurchaseAmountRule();
        PurchaseRule purchaseDateRule = new PurchaseDateRule();

        BiConsumer<String, String> addCheck = (purchaseNumber, message) -> {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put(purchaseNumber, message);
            checkList.add(errorMap);
        };

        purchaseList.forEach(purchase -> {
            // 仕入金額ルールチェック
            if (purchaseAmountRule.isSatisfiedBy(purchase)) {
                addCheck.accept(purchase.getPurchaseNumber().getValue(),
                               "仕入金額が500万円を超えています。");
            }

            // 仕入日ルールチェック
            if (purchaseDateRule.isSatisfiedBy(purchase)) {
                addCheck.accept(purchase.getPurchaseNumber().getValue(),
                               "仕入日が未来日です。");
            }
        });

        return new PurchaseRuleCheckList(checkList);
    }
}
```

## 17.3 支払管理

### 支払ドメインモデルの概要

支払は、仕入先への代金支払を管理します。仕入データを集計して、仕入先ごとの支払データを生成します。

```plantuml
@startuml
title 支払ドメインモデル

class PurchasePayment <<Entity>> {
  - paymentNumber: PurchasePaymentNumber
  - paymentDate: PurchasePaymentDate
  - departmentCode: DepartmentCode
  - departmentStartDate: LocalDateTime
  - supplierCode: SupplierCode
  - paymentMethodType: PurchasePaymentMethodType
  - paymentAmount: Money
  - totalConsumptionTax: Money
  - paymentCompletedFlag: Boolean
  + isCompleted(): boolean
  + aggregateFromPurchases(): PurchasePayment
}

class PurchasePaymentNumber <<Value Object>> {
  - value: String
  + generate(): String
}

class PurchasePaymentDate <<Value Object>> {
  - value: Integer
  + now(): PurchasePaymentDate
}

enum PurchasePaymentMethodType {
  現金(1)
  小切手(2)
  手形(3)
  振込(4)
  相殺(5)
  その他(9)
  + fromCode(): PurchasePaymentMethodType
}

PurchasePayment --> PurchasePaymentNumber
PurchasePayment --> PurchasePaymentDate
PurchasePayment --> PurchasePaymentMethodType

note right of PurchasePayment
  支払の特徴:
  - 仕入先ごとに集計
  - 支払方法を管理
  - 支払完了フラグで状態管理
end note

@enduml
```

### 支払エンティティの実装

支払エンティティは、仕入先への支払情報を管理します。

```java
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder(toBuilder = true)
public class PurchasePayment {
    PurchasePaymentNumber paymentNumber; // 支払番号
    PurchasePaymentDate paymentDate; // 支払日
    DepartmentCode departmentCode; // 部門コード
    LocalDateTime departmentStartDate; // 部門開始日
    SupplierCode supplierCode; // 仕入先コード
    PurchasePaymentMethodType paymentMethodType; // 支払方法区分
    Money paymentAmount; // 支払金額
    Money totalConsumptionTax; // 消費税合計
    Boolean paymentCompletedFlag; // 支払完了フラグ
    LocalDateTime createdDateTime; // 作成日時
    String createdBy; // 作成者名
    LocalDateTime updatedDateTime; // 更新日時
    String updatedBy; // 更新者名

    /**
     * 支払が完了しているかどうかを判定する
     */
    public boolean isCompleted() {
        return Boolean.TRUE.equals(paymentCompletedFlag);
    }

    /**
     * 仕入データから支払データを集計する
     */
    public static PurchasePayment aggregateFromPurchases(
            List<Purchase> purchases,
            PurchasePaymentNumber paymentNumber,
            PurchasePaymentDate paymentDate,
            PurchasePaymentMethodType paymentMethodType) {

        if (purchases == null || purchases.isEmpty()) {
            throw new IllegalArgumentException("仕入データが存在しません");
        }

        // 最初の仕入データから共通情報を取得
        Purchase firstPurchase = purchases.get(0);

        // 仕入金額と消費税を合計
        Money totalAmount = purchases.stream()
                .map(Purchase::getTotalPurchaseAmount)
                .reduce(Money.of(0), Money::plusMoney);

        Money totalTax = purchases.stream()
                .map(Purchase::getTotalConsumptionTax)
                .reduce(Money.of(0), Money::plusMoney);

        return PurchasePayment.builder()
                .paymentNumber(paymentNumber)
                .paymentDate(paymentDate)
                .departmentCode(firstPurchase.getDepartmentCode())
                .departmentStartDate(firstPurchase.getStartDate())
                .supplierCode(firstPurchase.getSupplierCode())
                .paymentMethodType(paymentMethodType)
                .paymentAmount(totalAmount)
                .totalConsumptionTax(totalTax)
                .paymentCompletedFlag(false)
                .build();
    }
}
```

### 支払方法区分

支払方法は列挙型で定義されています。

```java
@AllArgsConstructor
@Getter
public enum PurchasePaymentMethodType {
    現金(1, "現金"),
    小切手(2, "小切手"),
    手形(3, "手形"),
    振込(4, "振込"),
    相殺(5, "相殺"),
    その他(9, "その他");

    private final Integer code;
    private final String name;

    /**
     * コードから支払方法区分を取得する
     */
    public static PurchasePaymentMethodType fromCode(Integer code) {
        if (code == null) {
            return 振込; // デフォルトは振込
        }

        for (PurchasePaymentMethodType type : PurchasePaymentMethodType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }

        return 振込;
    }
}
```

### 支払予定の生成

支払サービスは、仕入データを集計して支払予定を生成します。

```java
@Service
@Transactional
public class PurchasePaymentService {

    final PurchasePaymentRepository purchasePaymentRepository;
    final PurchaseRepository purchaseRepository;
    final PartnerRepository partnerRepository;

    /**
     * 仕入データから支払データを集計する
     * 仕入先ごとに当月の仕入金額を合計して支払データを作成する
     */
    public void aggregate() {
        PurchaseList purchaseList = purchaseRepository.selectAll();

        // 仕入先コードでグループ化
        Map<String, List<Purchase>> purchasesBySupplier = purchaseList.asList().stream()
                .collect(Collectors.groupingBy(purchase ->
                    purchase.getSupplierCode().getCode().getValue() + "-" +
                    purchase.getSupplierCode().getBranchNumber()
                ));

        // 仕入先ごとに支払データを作成
        final int[] counter = {1};
        purchasesBySupplier.forEach((supplierKey, purchases) -> {
            registerPurchasePaymentApplication(purchases, counter[0]++);
        });
    }

    /**
     * 仕入データリストから支払データを登録する
     */
    public void registerPurchasePaymentApplication(List<Purchase> purchases, int counter) {
        if (purchases.isEmpty()) {
            return;
        }

        Purchase firstPurchase = purchases.get(0);
        PurchasePaymentDate paymentDate = PurchasePaymentDate.now();

        // 既存の支払データを検索
        PurchasePaymentCriteria criteria = PurchasePaymentCriteria.builder()
                .supplierCode(firstPurchase.getSupplierCode().getCode().getValue())
                .supplierBranchNumber(firstPurchase.getSupplierCode().getBranchNumber())
                .paymentDate(paymentDate.getValue())
                .build();

        PageInfo<PurchasePayment> existingPayments =
                purchasePaymentRepository.searchWithPageInfo(criteria);

        // 支払済みのデータは更新しない
        if (!existingPayments.getList().isEmpty()) {
            PurchasePayment existingPayment = existingPayments.getList().get(0);
            if (existingPayment.isCompleted()) {
                return;
            }
        }

        // 支払番号を決定
        PurchasePaymentNumber paymentNumber;
        if (!existingPayments.getList().isEmpty()) {
            paymentNumber = existingPayments.getList().get(0).getPaymentNumber();
        } else {
            paymentNumber = PurchasePaymentNumber.generate(counter);
        }

        // 仕入先マスタから支払方法を取得
        VendorCode vendorCode = VendorCode.of(
                firstPurchase.getSupplierCode().getCode().getValue(),
                firstPurchase.getSupplierCode().getBranchNumber()
        );
        Optional<Vendor> vendorOpt = partnerRepository.findVendorById(vendorCode);
        PurchasePaymentMethodType paymentMethodType = vendorOpt
                .map(vendor -> PurchasePaymentMethodType.fromCode(
                        vendor.getVendorClosingBilling().getPaymentMethod().getValue()))
                .orElse(PurchasePaymentMethodType.fromCode(1));

        // 支払データを集計・登録
        PurchasePayment payment = PurchasePayment.aggregateFromPurchases(
                purchases,
                paymentNumber,
                paymentDate,
                paymentMethodType
        );

        purchasePaymentRepository.save(payment);
    }
}
```

```plantuml
@startuml
title 支払集計処理のシーケンス

actor ユーザー
participant "PurchasePaymentService" as Service
participant "PurchaseRepository" as PurchaseRepo
participant "PurchasePaymentRepository" as PaymentRepo
participant "PartnerRepository" as PartnerRepo
participant "PurchasePayment" as Payment

ユーザー -> Service: aggregate()
Service -> PurchaseRepo: selectAll()
PurchaseRepo --> Service: PurchaseList

Service -> Service: 仕入先コードでグループ化

loop 仕入先ごと
  Service -> PaymentRepo: searchWithPageInfo(criteria)
  PaymentRepo --> Service: 既存の支払データ

  alt 既存データが支払済み
    Service -> Service: スキップ
  else 新規または未払い
    Service -> PartnerRepo: findVendorById(vendorCode)
    PartnerRepo --> Service: Vendor（支払方法取得）

    Service -> Payment: aggregateFromPurchases()
    note right
      仕入金額を合計
      消費税を合計
      支払データ作成
    end note
    Payment --> Service: PurchasePayment

    Service -> PaymentRepo: save(payment)
  end
end

Service --> ユーザー: 完了

@enduml
```

### 支払実績の登録

支払が完了したら、支払完了フラグを更新します。

```plantuml
@startuml
title 支払ステータス管理

[*] --> 支払予定生成

支払予定生成 --> 支払待ち : aggregate()
支払待ち --> 支払処理中 : 支払日到来

支払処理中 --> 支払完了 : 振込/決済実行
支払完了 --> [*]

state 支払予定生成 {
  note "仕入データから集計"
}

state 支払待ち {
  note "paymentCompletedFlag = false"
}

state 支払完了 {
  note "paymentCompletedFlag = true"
}

@enduml
```

## 17.4 メソッド抽出リファクタリング

### 複雑度の低減

支払集計処理は複雑になりがちです。メソッド抽出によって可読性を向上させています。

**リファクタリング前:**

```java
public void aggregate() {
    PurchaseList purchaseList = purchaseRepository.selectAll();

    Map<String, List<Purchase>> purchasesBySupplier = purchaseList.asList().stream()
            .collect(Collectors.groupingBy(purchase ->
                purchase.getSupplierCode().getCode().getValue() + "-" +
                purchase.getSupplierCode().getBranchNumber()
            ));

    final int[] counter = {1};
    purchasesBySupplier.forEach((supplierKey, purchases) -> {
        if (purchases.isEmpty()) return;

        Purchase firstPurchase = purchases.get(0);
        PurchasePaymentDate paymentDate = PurchasePaymentDate.now();

        // ... 長い処理 ...
    });
}
```

**リファクタリング後:**

```java
public void aggregate() {
    PurchaseList purchaseList = purchaseRepository.selectAll();
    Map<String, List<Purchase>> purchasesBySupplier = groupBySupplier(purchaseList);

    final int[] counter = {1};
    purchasesBySupplier.forEach((supplierKey, purchases) -> {
        registerPurchasePaymentApplication(purchases, counter[0]++);
    });
}

private Map<String, List<Purchase>> groupBySupplier(PurchaseList purchaseList) {
    return purchaseList.asList().stream()
            .collect(Collectors.groupingBy(this::getSupplierKey));
}

private String getSupplierKey(Purchase purchase) {
    return purchase.getSupplierCode().getCode().getValue() + "-" +
           purchase.getSupplierCode().getBranchNumber();
}
```

### テストの維持

リファクタリング後もテストがパスすることを確認します。

```java
@Test
@DisplayName("仕入データから支払データを集計できる")
void shouldAggregatePaymentsFromPurchases() {
    // Arrange
    Purchase purchase1 = createTestPurchase("SI25010001", "SUPP001", 0, 10000, 1000);
    Purchase purchase2 = createTestPurchase("SI25010002", "SUPP001", 0, 20000, 2000);

    PurchaseList purchaseList = PurchaseList.of(List.of(purchase1, purchase2));
    when(purchaseRepository.selectAll()).thenReturn(purchaseList);
    when(purchasePaymentRepository.searchWithPageInfo(any())).thenReturn(new PageInfo<>(List.of()));

    // Act
    purchasePaymentService.aggregate();

    // Assert
    verify(purchasePaymentRepository).save(argThat(payment ->
        payment.getPaymentAmount().getAmount() == 30000 &&
        payment.getTotalConsumptionTax().getAmount() == 3000
    ));
}
```

## まとめ

この章では、仕入・支払管理の実装について解説しました。

**重要なポイント:**

1. **仕入と発注の連携**: 発注番号を通じて仕入と発注を紐付け、トレーサビリティを確保しています。

2. **支払の集計処理**: 仕入先ごとに仕入データを集計し、自動的に支払データを生成します。

3. **支払方法の管理**: 列挙型で支払方法を定義し、仕入先マスタの設定に基づいて支払方法を決定します。

4. **Strategy パターン**: 仕入ルールも Strategy パターンで実装され、金額ルールや日付ルールなどを柔軟に適用できます。

5. **リファクタリング**: 複雑な集計処理は、メソッド抽出によって可読性を向上させています。TDD のアプローチにより、リファクタリング後もテストがパスすることを保証しています。

次の章では、在庫管理について解説します。倉庫・棚番マスタの設計と、在庫データの実装を学びます。

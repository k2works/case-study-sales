package com.example.sms.infrastructure.datasource.sales.sales;

import com.example.sms.domain.model.master.employee.Employee;
import com.example.sms.domain.model.master.partner.customer.*;
import com.example.sms.domain.model.master.partner.billing.ClosingBilling;
import com.example.sms.domain.model.master.partner.billing.Billing;
import com.example.sms.domain.model.sales.order.OrderNumber;
import com.example.sms.domain.model.sales.order.TaxRateType;
import com.example.sms.domain.model.sales.sales.*;
import com.example.sms.domain.type.address.Address;
import com.example.sms.domain.type.mail.EmailAddress;
import com.example.sms.domain.type.phone.FaxNumber;
import com.example.sms.domain.type.phone.PhoneNumber;
import com.example.sms.infrastructure.datasource.autogen.model.出荷先マスタ;
import com.example.sms.infrastructure.datasource.autogen.model.売上データ;
import com.example.sms.infrastructure.datasource.autogen.model.売上データ明細;
import com.example.sms.infrastructure.datasource.autogen.model.売上データ明細Key;
import com.example.sms.infrastructure.datasource.master.employee.EmployeeCustomEntity;
import com.example.sms.infrastructure.datasource.master.partner.customer.CustomerCustomEntity;
import com.example.sms.infrastructure.datasource.master.product.ProductEntityMapper;
import com.example.sms.infrastructure.datasource.sales.sales.sales_line.SalesLineCustomEntity;
import com.example.sms.infrastructure.datasource.system.download.SalesDownloadCSV;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Component
public class SalesEntityMapper {

    public 売上データ mapToEntity(Sales sales) {
        // salesがnullの場合、新しい空のSalesオブジェクトを返す
        if (sales == null) {
            return new 売上データ();
        }

        売上データ salesEntity = new 売上データ();

        // 各プロパティにnullチェックを追加
        salesEntity.set売上番号(sales.getSalesNumber().getValue());
        salesEntity.set受注番号(sales.getOrderNumber().getValue());
        salesEntity.set売上日(sales.getSalesDate().getValue());
        salesEntity.set売上区分(sales.getSalesType().getCode());
        salesEntity.set部門コード(sales.getDepartmentId().getDeptCode().getValue());
        salesEntity.set部門開始日(sales.getDepartmentId().getDepartmentStartDate().getValue());
        salesEntity.set取引先コード(sales.getPartnerCode().getValue());
        salesEntity.set顧客コード(sales.getCustomerCode() != null ? sales.getCustomerCode().getCode().getValue() : null);
        salesEntity.set顧客枝番(sales.getCustomerCode() != null ? sales.getCustomerCode().getBranchNumber() : null);
        salesEntity.set社員コード(sales.getEmployeeCode().getValue());
        salesEntity.set売上金額合計(
                sales.getTotalSalesAmount() != null ? sales.getTotalSalesAmount().getAmount() : 0
        );
        salesEntity.set消費税合計(
                sales.getTotalConsumptionTax() != null ? sales.getTotalConsumptionTax().getAmount() : 0
        );
        salesEntity.set備考(sales.getRemarks());
        salesEntity.set赤黒伝票番号(sales.getVoucherNumber());
        salesEntity.set元伝票番号(sales.getOriginalVoucherNumber());

        return salesEntity;
    }

    public 売上データ明細 mapToEntity(売上データ明細Key key, SalesLine salesLine) {
        売上データ明細 salesEntity = new 売上データ明細();
        salesEntity.set売上番号(key.get売上番号());
        salesEntity.set売上行番号(key.get売上行番号());
        salesEntity.set受注番号(Optional.ofNullable(salesLine.getOrderNumber())
                .map(OrderNumber::getValue)
                .orElse(null));
        salesEntity.set受注行番号(salesLine.getOrderLineNumber());
        salesEntity.set商品コード(Objects.requireNonNull(salesLine.getProductCode()).getValue());
        salesEntity.set商品名(salesLine.getProductName());
        salesEntity.set販売単価(Objects.requireNonNull(salesLine.getSalesUnitPrice()).getAmount());
        salesEntity.set売上数量(Objects.requireNonNull(salesLine.getSalesQuantity()).getAmount());
        salesEntity.set出荷数量(Objects.requireNonNull(salesLine.getShippedQuantity()).getAmount());
        salesEntity.set値引金額(Objects.requireNonNull(salesLine.getDiscountAmount()).getAmount());
        salesEntity.set請求日(Optional.of(salesLine)
                .map(SalesLine::getBillingDate)
                .map(BillingDate::getValue)
                .orElse(null));
        salesEntity.set請求番号(Optional.of(salesLine)
                .map(SalesLine::getBillingNumber)
                .map(BillingNumber::getValue)
                .orElse(null));
        salesEntity.set請求遅延区分(Optional.of(salesLine)
                .map(SalesLine::getBillingDelayType)
                .map(BillingDelayType::getCode)
                .orElse(null));
        salesEntity.set自動仕訳日(Optional.of(salesLine)
                .map(SalesLine::getAutoJournalDate)
                .map(AutoJournalDate::getValue)
                .orElse(null));
        salesEntity.set消費税率(Objects.requireNonNull(salesLine.getTaxRate()).getRate());
        return salesEntity;
    }

    public 売上データ明細Key mapToKey(SalesLine salesLine) {
        売上データ明細Key key = new 売上データ明細Key();
        key.set売上番号(salesLine.getSalesNumber().getValue());
        key.set売上行番号(salesLine.getSalesLineNumber());
        return key;
    }

    public Sales mapToDomainModel(SalesCustomEntity salesData) {
        Function<出荷先マスタ, Shipping> mapToShipping = e -> Shipping.of(
                e.get顧客コード(),
                e.get出荷先番号(),
                e.get顧客枝番(),
                e.get出荷先名(),
                e.get地域コード(),
                e.get出荷先郵便番号(),
                e.get出荷先住所１(),
                e.get出荷先住所２()
        );

        Function<CustomerCustomEntity, Customer> mapToCustomer = e -> Customer.of(
                CustomerCode.of(e.get顧客コード(), e.get顧客枝番()),
                CustomerType.fromCode(e.get顧客区分()),
                BillingCode.of(e.get請求先コード(), e.get請求先枝番()),
                CollectionCode.of(e.get回収先コード(), e.get回収先枝番()),
                CustomerName.of(e.get顧客名(), e.get顧客名カナ()),
                e.get自社担当者コード(),
                e.get顧客担当者名(),
                e.get顧客部門名(),
                Address.of(
                        e.get顧客郵便番号(),
                        e.get顧客都道府県(),
                        e.get顧客住所１(),
                        e.get顧客住所２()
                ),
                PhoneNumber.of(e.get顧客電話番号()),
                FaxNumber.of(e.get顧客ｆａｘ番号()),
                EmailAddress.of(e.get顧客メールアドレス()),
                Billing.of(
                        CustomerBillingCategory.fromCode(e.get顧客請求区分()),
                        ClosingBilling.of(
                                e.get顧客締日１(),
                                e.get顧客支払月１(),
                                e.get顧客支払日１(),
                                e.get顧客支払方法１()
                        ),
                        ClosingBilling.of(
                                e.get顧客締日２(),
                                e.get顧客支払月２(),
                                e.get顧客支払日２(),
                                e.get顧客支払方法２()
                        )
                ),
                e.get出荷先マスタ().stream().map(mapToShipping).toList()
        );


        Function<EmployeeCustomEntity, Employee> mapToEmployee = e -> Employee.of(
                e.get社員コード(),
                e.get社員名(),
                e.get社員名カナ(),
                e.get電話番号(),
                e.getFax番号(),
                e.get職種コード()
        );

        Function<SalesLineCustomEntity, SalesLine> salesLineMapper = e -> {
            if (Objects.isNull(e)) {
                return SalesLine.of(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
            }

            return SalesLine.of(
                    e.get売上番号(),
                    e.get売上行番号(),
                    e.get受注番号(),
                    e.get受注行番号(),
                    e.get商品コード(),
                    e.get商品名(),
                    e.get販売単価(),
                    e.get売上数量(),
                    e.get出荷数量(),
                    e.get値引金額(),
                    e.get請求日(),
                    e.get請求番号(),
                    e.get請求遅延区分(),
                    e.get自動仕訳日(),
                    e.get商品マスタ()  != null ? ProductEntityMapper.mapToDomainModel(e.get商品マスタ()) : null,
                    TaxRateType.of(e.get消費税率())
            );
        };


        Sales sales = Sales.of(
                salesData.get売上番号(),
                salesData.get受注番号(),
                salesData.get売上日(),
                salesData.get売上区分(),
                salesData.get部門コード(),
                salesData.get部門開始日(),
                salesData.get顧客コード(),
                salesData.get顧客枝番(),
                salesData.get社員コード(),
                salesData.get赤黒伝票番号(),
                salesData.get元伝票番号(),
                salesData.get備考(),
                salesData.get売上データ明細().stream()
                        .map(salesLineMapper)
                        .toList()
        );

        // 顧客コード、顧客枝番を設定
        sales = new Sales(
                sales.getSalesNumber(),
                sales.getOrderNumber(),
                sales.getSalesDate(),
                sales.getSalesType(),
                sales.getDepartmentId(),
                sales.getPartnerCode(),
                salesData.get顧客コード() != null ? CustomerCode.of(salesData.get顧客コード(), salesData.get顧客枝番()) : null,
                sales.getEmployeeCode(),
                sales.getTotalSalesAmount(),
                sales.getTotalConsumptionTax(),
                sales.getRemarks(),
                sales.getVoucherNumber(),
                sales.getOriginalVoucherNumber(),
                sales.getSalesLines(),
                null,
                null
        );

        return Sales.of(sales,
                salesData.get顧客マスタ() != null ? mapToCustomer.apply(salesData.get顧客マスタ()) : null,
                salesData.get社員マスタ() != null ? mapToEmployee.apply(salesData.get社員マスタ()) : null
        );
    }

    public SalesLine mapToDomainModel(SalesLineCustomEntity salesLineData) {
        if (salesLineData == null) {
            return SalesLine.of(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        }

        return SalesLine.of(
                salesLineData.get売上番号(),
                salesLineData.get売上行番号(),
                salesLineData.get受注番号(),
                salesLineData.get受注行番号(),
                salesLineData.get商品コード(),
                salesLineData.get商品名(),
                salesLineData.get販売単価(),
                salesLineData.get売上数量(),
                salesLineData.get出荷数量(),
                salesLineData.get値引金額(),
                salesLineData.get請求日(),
                salesLineData.get請求番号(),
                salesLineData.get請求遅延区分(),
                salesLineData.get自動仕訳日(),
                Optional.ofNullable(salesLineData.get商品マスタ())
                        .map(ProductEntityMapper::mapToDomainModel)
                        .orElse(null),
                TaxRateType.of(salesLineData.get消費税率())
        );
    }

    public SalesDownloadCSV mapToCsvModel(Sales sales, SalesLine salesLine) {
        return new SalesDownloadCSV(
                sales.getSalesNumber().getValue(),
                sales.getOrderNumber().getValue(),
                sales.getSalesDate().getValue(),
                sales.getSalesType().getCode(),
                sales.getDepartmentId().getDeptCode().getValue(),
                sales.getDepartmentId().getDepartmentStartDate().getValue(),
                sales.getPartnerCode().getValue(),
                sales.getEmployeeCode().getValue(),
                sales.getCustomerCode() != null ? sales.getCustomerCode().getCode().getValue() : null,
                sales.getCustomerCode() != null ? sales.getCustomerCode().getBranchNumber() : null,
                sales.getTotalSalesAmount().getAmount(),
                sales.getTotalConsumptionTax().getAmount(),
                sales.getRemarks(),
                sales.getVoucherNumber(),
                sales.getOriginalVoucherNumber(),
                salesLine.getSalesLineNumber(),
                salesLine.getProductCode().getValue(),
                salesLine.getProductName(),
                salesLine.getSalesUnitPrice().getAmount(),
                salesLine.getSalesQuantity().getAmount(),
                salesLine.getShippedQuantity().getAmount(),
                salesLine.getDiscountAmount().getAmount(),
                salesLine.getBillingDate() != null ? salesLine.getBillingDate().getValue() : null,
                salesLine.getBillingNumber() != null ? salesLine.getBillingNumber().getValue() : null,
                salesLine.getBillingDelayType() != null ? salesLine.getBillingDelayType().getCode() : null,
                salesLine.getAutoJournalDate() != null ? salesLine.getAutoJournalDate().getValue() : null,
                salesLine.getTaxRate().getRate()
        );
    }
}

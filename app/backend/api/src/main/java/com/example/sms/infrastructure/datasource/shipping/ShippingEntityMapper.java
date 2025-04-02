package com.example.sms.infrastructure.datasource.shipping;

import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.department.DepartmentId;
import com.example.sms.domain.model.master.employee.Employee;
import com.example.sms.domain.model.master.partner.customer.*;
import com.example.sms.domain.model.master.partner.invoice.ClosingInvoice;
import com.example.sms.domain.model.master.partner.invoice.Invoice;
import com.example.sms.domain.model.master.product.*;
import com.example.sms.domain.model.sales_order.SalesOrder;
import com.example.sms.domain.model.sales_order.SalesOrderLine;
import com.example.sms.domain.type.address.Address;
import com.example.sms.domain.type.mail.EmailAddress;
import com.example.sms.domain.type.phone.FaxNumber;
import com.example.sms.domain.type.phone.PhoneNumber;
import com.example.sms.infrastructure.datasource.autogen.model.*;
import com.example.sms.infrastructure.datasource.master.department.DepartmentCustomEntity;
import com.example.sms.infrastructure.datasource.master.employee.EmployeeCustomEntity;
import com.example.sms.infrastructure.datasource.master.partner.customer.CustomerCustomEntity;
import com.example.sms.infrastructure.datasource.master.product.ProductCustomEntity;
import com.example.sms.infrastructure.datasource.sales_order.SalesOrderCustomEntity;
import com.example.sms.infrastructure.datasource.sales_order.sales_order_line.SalesOrderLineCustomEntity;
import com.example.sms.infrastructure.datasource.system.download.SalesOrderDownloadCSV;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Component
public class ShippingEntityMapper {
    public 受注データ mapToEntity(SalesOrder salesOrder) {
        受注データ salesOrderEntity = new 受注データ();
        salesOrderEntity.set受注番号(salesOrder.getOrderNumber().getValue());
        salesOrderEntity.set受注日(salesOrder.getOrderDate().getValue());
        salesOrderEntity.set部門コード(salesOrder.getDepartmentCode().getValue());
        salesOrderEntity.set部門開始日(salesOrder.getDepartmentStartDate());
        salesOrderEntity.set顧客コード(salesOrder.getCustomerCode().getCode().getValue());
        salesOrderEntity.set顧客枝番(salesOrder.getCustomerCode().getBranchNumber());
        salesOrderEntity.set社員コード(salesOrder.getEmployeeCode().getValue());
        salesOrderEntity.set希望納期(salesOrder.getDesiredDeliveryDate().getValue());
        salesOrderEntity.set客先注文番号(salesOrder.getCustomerOrderNumber());
        salesOrderEntity.set倉庫コード(salesOrder.getWarehouseCode());
        salesOrderEntity.set受注金額合計(salesOrder.getTotalOrderAmount().getAmount());
        salesOrderEntity.set消費税合計(salesOrder.getTotalConsumptionTax().getAmount());
        salesOrderEntity.set備考(salesOrder.getRemarks());

        return salesOrderEntity;
    }

    public 受注データ明細 mapToEntity(受注データ明細Key key, SalesOrderLine salesOrderLine) {
        受注データ明細 salesOrderLineEntity = new 受注データ明細();
        salesOrderLineEntity.set受注番号(key.get受注番号());
        salesOrderLineEntity.set受注行番号(key.get受注行番号());
        salesOrderLineEntity.set商品コード(salesOrderLine.getProductCode().getValue());
        salesOrderLineEntity.set商品名(salesOrderLine.getProductName());
        salesOrderLineEntity.set販売単価(salesOrderLine.getSalesUnitPrice().getAmount());
        salesOrderLineEntity.set受注数量(salesOrderLine.getOrderQuantity().getAmount());
        salesOrderLineEntity.set消費税率(salesOrderLine.getTaxRate().getRate());
        salesOrderLineEntity.set引当数量(salesOrderLine.getAllocationQuantity().getAmount());
        salesOrderLineEntity.set出荷指示数量(salesOrderLine.getShipmentInstructionQuantity().getAmount());
        salesOrderLineEntity.set出荷済数量(salesOrderLine.getShippedQuantity().getAmount());
        salesOrderLineEntity.set完了フラグ(salesOrderLine.getCompletionFlag().getValue());
        salesOrderLineEntity.set値引金額(salesOrderLine.getDiscountAmount().getAmount());
        salesOrderLineEntity.set納期(salesOrderLine.getDeliveryDate().getValue());

        return salesOrderLineEntity;
    }

    public SalesOrder mapToDomainModel(SalesOrderCustomEntity salesOrderCustomEntity) {
        Function<代替商品, SubstituteProduct> mapToSubstituteProduct = s -> (
                SubstituteProduct.of(
                        s.get商品コード(),
                        s.get代替商品コード(),
                        s.get優先順位()
                )
        );

        Function<部品表, Bom> mapToBom = b -> (
                Bom.of(
                        b.get商品コード(),
                        b.get部品コード(),
                        b.get部品数量()
                )
        );

        Function<顧客別販売単価, CustomerSpecificSellingPrice> mapToCustomerSpecificSellingPrice = c -> (
                CustomerSpecificSellingPrice.of(
                        c.get商品コード(),
                        c.get取引先コード(),
                        c.get販売単価()
                )
        );
        Function<ProductCustomEntity, Product> mapToProduct = e -> Product.of(
                Product.of(
                        e.get商品コード(),
                        e.get商品正式名(),
                        e.get商品略称(),
                        e.get商品名カナ(),
                        ProductType.fromCode(e.get商品区分()),
                        e.get販売単価(),
                        e.get仕入単価(),
                        e.get売上原価(),
                        TaxType.fromCode(e.get税区分()),
                        e.get商品分類コード(),
                        MiscellaneousType.fromCode(e.get雑区分()),
                        StockManagementTargetType.fromCode(e.get在庫管理対象区分()),
                        StockAllocationType.fromCode(e.get在庫引当区分()),
                        e.get仕入先コード(),
                        e.get仕入先枝番()
                ),
                e.get代替商品().stream().map(mapToSubstituteProduct).toList(),
                e.get部品表().stream().map(mapToBom).toList(),
                e.get顧客別販売単価().stream().map(mapToCustomerSpecificSellingPrice).toList()
        );

        Function<SalesOrderLineCustomEntity, SalesOrderLine> salesOrderLineMapper = e -> {
            if (Objects.isNull(e)) {
                return SalesOrderLine.of(null, null);
            }

            return SalesOrderLine.of(
                    SalesOrderLine.of(
                            e.get受注番号(),
                            e.get受注行番号(),
                            e.get商品コード(),
                            e.get商品名(),
                            e.get販売単価(),
                            e.get受注数量(),
                            e.get消費税率(),
                            e.get引当数量(),
                            e.get出荷指示数量(),
                            e.get出荷済数量(),
                            e.get完了フラグ(),
                            Objects.isNull(e.get値引金額()) ? 0 : e.get値引金額(), // 値引金額のnullチェック
                            e.get納期()
                    ),
                    Objects.nonNull(e.get商品マスタ()) ? mapToProduct.apply(e.get商品マスタ()) : null // 商品マスタのnullチェック
            );
        };

        Function<DepartmentCustomEntity, Department> mapToDepartment = e -> Department.of(
                DepartmentId.of(e.get部門コード(), e.get開始日()),
                e.get終了日(),
                e.get部門名(),
                e.get組織階層(),
                e.get部門パス(),
                e.get最下層区分(),
                e.get伝票入力可否()
        );

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
                Invoice.of(
                        CustomerBillingCategory.fromCode(e.get顧客請求区分()),
                        ClosingInvoice.of(
                                e.get顧客締日１(),
                                e.get顧客支払月１(),
                                e.get顧客支払日１(),
                                e.get顧客支払方法１()
                        ),
                        ClosingInvoice.of(
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

        SalesOrder salesOrder = SalesOrder.of(
                salesOrderCustomEntity.get受注番号(),
                salesOrderCustomEntity.get受注日(),
                salesOrderCustomEntity.get部門コード(),
                salesOrderCustomEntity.get部門開始日(),
                salesOrderCustomEntity.get顧客コード(),
                salesOrderCustomEntity.get顧客枝番(),
                salesOrderCustomEntity.get社員コード(),
                salesOrderCustomEntity.get希望納期(),
                salesOrderCustomEntity.get客先注文番号(),
                salesOrderCustomEntity.get倉庫コード(),
                salesOrderCustomEntity.get受注金額合計(),
                salesOrderCustomEntity.get消費税合計(),
                salesOrderCustomEntity.get備考(),
                salesOrderCustomEntity.get受注データ明細().stream().map(salesOrderLineMapper).toList()
        );

        return SalesOrder.of(
                salesOrder,
                Optional.ofNullable(salesOrderCustomEntity.get部門マスタ())
                        .map(mapToDepartment)
                        .orElse(null),
                Optional.ofNullable(salesOrderCustomEntity.get顧客マスタ())
                        .map(mapToCustomer)
                        .orElse(null),
                Optional.ofNullable(salesOrderCustomEntity.get社員マスタ())
                        .map(mapToEmployee)
                        .orElse(null)
        );
    }
}

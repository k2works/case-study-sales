package com.example.sms.infrastructure.datasource.sales.shipping;

import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.department.DepartmentId;
import com.example.sms.domain.model.master.employee.Employee;
import com.example.sms.domain.model.master.employee.EmployeeCode;
import com.example.sms.domain.model.master.department.DepartmentCode;
import com.example.sms.domain.model.master.partner.customer.*;
import com.example.sms.domain.model.master.partner.billing.ClosingInvoice;
import com.example.sms.domain.model.master.partner.billing.Billing;
import com.example.sms.domain.model.master.product.*;
import com.example.sms.domain.model.sales.order.*;
import com.example.sms.domain.model.sales.shipping.Shipping;
import com.example.sms.domain.model.sales.shipping.ShippingList;
import com.example.sms.domain.type.address.Address;
import com.example.sms.domain.type.mail.EmailAddress;
import com.example.sms.domain.type.money.Money;
import com.example.sms.domain.type.phone.FaxNumber;
import com.example.sms.domain.type.phone.PhoneNumber;
import com.example.sms.domain.type.quantity.Quantity;
import com.example.sms.infrastructure.datasource.autogen.model.*;
import com.example.sms.infrastructure.datasource.master.department.DepartmentCustomEntity;
import com.example.sms.infrastructure.datasource.master.employee.EmployeeCustomEntity;
import com.example.sms.infrastructure.datasource.master.partner.customer.CustomerCustomEntity;
import com.example.sms.infrastructure.datasource.master.product.ProductCustomEntity;
import com.example.sms.infrastructure.datasource.sales.order.OrderCustomEntity;
import com.example.sms.infrastructure.datasource.sales.order.order_line.OrderLineCustomEntity;
import com.example.sms.infrastructure.datasource.system.download.ShippingDownloadCSV;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Component
public class ShippingEntityMapper {
    public 受注データ mapToEntity(Shipping shipping) {
        受注データ salesOrderEntity = new 受注データ();
        salesOrderEntity.set受注番号(shipping.getOrderNumber().getValue());
        salesOrderEntity.set受注日(shipping.getOrderDate().getValue());
        salesOrderEntity.set部門コード(shipping.getDepartmentCode().getValue());
        salesOrderEntity.set部門開始日(shipping.getDepartmentStartDate());
        salesOrderEntity.set顧客コード(shipping.getCustomerCode().getCode().getValue());
        salesOrderEntity.set顧客枝番(shipping.getCustomerCode().getBranchNumber());
        salesOrderEntity.set社員コード(shipping.getEmployeeCode().getValue());
        salesOrderEntity.set希望納期(shipping.getDesiredDeliveryDate().getValue());
        salesOrderEntity.set客先注文番号(shipping.getCustomerOrderNumber());
        salesOrderEntity.set倉庫コード(shipping.getWarehouseCode());
        salesOrderEntity.set受注金額合計(shipping.getTotalOrderAmount().getAmount());
        salesOrderEntity.set消費税合計(shipping.getTotalConsumptionTax().getAmount());
        salesOrderEntity.set備考(shipping.getRemarks());

        return salesOrderEntity;
    }

    public 受注データ明細 mapToEntity(受注データ明細Key key, Shipping shipping) {
        受注データ明細 salesOrderLineEntity = new 受注データ明細();
        salesOrderLineEntity.set受注番号(key.get受注番号());
        salesOrderLineEntity.set受注行番号(key.get受注行番号());
        salesOrderLineEntity.set商品コード(shipping.getProductCode().getValue());
        salesOrderLineEntity.set商品名(shipping.getProductName());
        salesOrderLineEntity.set販売単価(shipping.getSalesUnitPrice().getAmount());
        salesOrderLineEntity.set受注数量(shipping.getOrderQuantity().getAmount());
        salesOrderLineEntity.set消費税率(shipping.getTaxRate().getRate());
        salesOrderLineEntity.set引当数量(shipping.getAllocationQuantity().getAmount());
        salesOrderLineEntity.set出荷指示数量(shipping.getShipmentInstructionQuantity().getAmount());
        salesOrderLineEntity.set出荷済数量(shipping.getShippedQuantity().getAmount());
        salesOrderLineEntity.set完了フラグ(shipping.getCompletionFlag().getValue());
        salesOrderLineEntity.set値引金額(shipping.getDiscountAmount().getAmount());
        salesOrderLineEntity.set納期(shipping.getDeliveryDate().getValue());

        return salesOrderLineEntity;
    }

    public Shipping mapToDomainModel(OrderCustomEntity orderCustomEntity, OrderLineCustomEntity orderLineCustomEntity) {
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

        Function<DepartmentCustomEntity, Department> mapToDepartment = e -> Department.of(
                DepartmentId.of(e.get部門コード(), e.get開始日()),
                e.get終了日(),
                e.get部門名(),
                e.get組織階層(),
                e.get部門パス(),
                e.get最下層区分(),
                e.get伝票入力可否()
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
                new ArrayList<>()
        );

        Function<EmployeeCustomEntity, Employee> mapToEmployee = e -> Employee.of(
                e.get社員コード(),
                e.get社員名(),
                e.get社員名カナ(),
                e.get電話番号(),
                e.getFax番号(),
                e.get職種コード()
        );

        // Calculate SalesAmount and ConsumptionTaxAmount
        Money salesUnitPrice = Money.of(orderLineCustomEntity.get販売単価());
        Quantity orderQuantity = Quantity.of(orderLineCustomEntity.get受注数量());
        Money discountAmount = Money.of(Objects.isNull(orderLineCustomEntity.get値引金額()) ? 0 : orderLineCustomEntity.get値引金額());
        TaxRateType taxRate = TaxRateType.of(orderLineCustomEntity.get消費税率());

        SalesAmount salesAmount = SalesAmount.of(salesUnitPrice, orderQuantity);
        ConsumptionTaxAmount consumptionTaxAmount = ConsumptionTaxAmount.of(salesAmount, taxRate);

        return Shipping.of(
                OrderNumber.of(orderCustomEntity.get受注番号()),
                OrderDate.of(orderCustomEntity.get受注日()),
                DepartmentCode.of(orderCustomEntity.get部門コード()),
                orderCustomEntity.get部門開始日(),
                CustomerCode.of(orderCustomEntity.get顧客コード(), orderCustomEntity.get顧客枝番()),
                EmployeeCode.of(orderCustomEntity.get社員コード()),
                DesiredDeliveryDate.of(orderCustomEntity.get希望納期()),
                orderCustomEntity.get客先注文番号(),
                orderCustomEntity.get倉庫コード(),
                Money.of(orderCustomEntity.get受注金額合計()),
                Money.of(orderCustomEntity.get消費税合計()),
                orderCustomEntity.get備考(),
                orderLineCustomEntity.get受注行番号(),
                ProductCode.of(orderLineCustomEntity.get商品コード()),
                orderLineCustomEntity.get商品名(),
                salesUnitPrice,
                orderQuantity,
                taxRate,
                Quantity.of(orderLineCustomEntity.get引当数量()),
                Quantity.of(orderLineCustomEntity.get出荷指示数量()),
                Quantity.of(orderLineCustomEntity.get出荷済数量()),
                discountAmount,
                DeliveryDate.of(orderLineCustomEntity.get納期()),
                Objects.nonNull(orderLineCustomEntity.get出荷日()) ? ShippingDate.of(orderLineCustomEntity.get出荷日()) : null,
                Objects.nonNull(orderLineCustomEntity.get商品マスタ()) ? mapToProduct.apply(orderLineCustomEntity.get商品マスタ()) : null,
                salesAmount,
                consumptionTaxAmount,
                Optional.ofNullable(orderCustomEntity.get部門マスタ())
                        .map(mapToDepartment)
                        .orElse(null),
                Optional.ofNullable(orderCustomEntity.get顧客マスタ())
                        .map(mapToCustomer)
                        .orElse(null),
                Optional.ofNullable(orderCustomEntity.get社員マスタ())
                        .map(mapToEmployee)
                        .orElse(null)
        );
    }

    public List<Shipping> mapToDomainModelList(List<OrderCustomEntity> salesOrderCustomEntities) {
        List<Shipping> shippingList = new ArrayList<>();

        for (OrderCustomEntity orderCustomEntity : salesOrderCustomEntities) {
            for (OrderLineCustomEntity orderLineCustomEntity : orderCustomEntity.get受注データ明細()) {
                shippingList.add(mapToDomainModel(orderCustomEntity, orderLineCustomEntity));
            }
        }

        return shippingList;
    }

    public ShippingList mapToShippingList(List<Shipping> shippings) {
        return new ShippingList(shippings);
    }

    public ShippingDownloadCSV mapToCsvModel(Shipping shipping) {
        return new ShippingDownloadCSV(
                shipping.getOrderNumber().getValue(),
                shipping.getOrderDate().getValue(),
                shipping.getProductCode().getValue(),
                shipping.getProductName(),
                shipping.getOrderQuantity().getAmount(),
                shipping.getShipmentInstructionQuantity().getAmount(),
                shipping.getShippedQuantity().getAmount(),
                shipping.getCompletionFlag().getValue(),
                shipping.getDeliveryDate().getValue()
        );
    }
}

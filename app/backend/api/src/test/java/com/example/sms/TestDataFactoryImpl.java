package com.example.sms;

import com.example.sms.domain.model.master.employee.EmployeeCode;
import com.example.sms.domain.model.master.partner.billing.Billing;
import com.example.sms.domain.model.master.partner.billing.ClosingBilling;
import com.example.sms.domain.model.master.partner.customer.Customer;
import com.example.sms.domain.model.master.partner.customer.CustomerBillingCategory;
import com.example.sms.domain.model.master.partner.customer.CustomerCode;
import com.example.sms.domain.model.master.partner.customer.Shipping;
import com.example.sms.domain.model.master.partner.vendor.Vendor;
import com.example.sms.domain.model.master.payment.account.incoming.PaymentAccount;
import com.example.sms.domain.model.master.product.MiscellaneousType;
import com.example.sms.domain.model.master.region.Region;
import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.department.DepartmentId;
import com.example.sms.domain.model.master.employee.Employee;
import com.example.sms.domain.model.master.partner.*;
import com.example.sms.domain.model.master.partner.Partner;
import com.example.sms.domain.model.master.product.*;
import com.example.sms.domain.model.sales.invoice.Invoice;
import com.example.sms.domain.model.sales.invoice.InvoiceLine;
import com.example.sms.domain.model.sales.order.Order;
import com.example.sms.domain.model.sales.order.OrderLine;
import com.example.sms.domain.model.sales.order.OrderList;
import com.example.sms.domain.model.sales.order.TaxRateType;
import com.example.sms.domain.model.sales.payment.incoming.Payment;
import com.example.sms.domain.model.sales.payment.incoming.PaymentMethodType;
import com.example.sms.domain.model.sales.sales.Sales;
import com.example.sms.domain.model.sales.sales.SalesLine;
import com.example.sms.domain.model.procurement.purchase.PurchaseOrder;
import com.example.sms.domain.model.procurement.purchase.PurchaseOrderLine;
import com.example.sms.domain.model.procurement.purchase.PurchaseOrderNumber;
import com.example.sms.domain.model.sales.order.OrderNumber;
import com.example.sms.domain.model.procurement.purchase.PurchaseOrderDate;
import com.example.sms.domain.model.procurement.purchase.DesignatedDeliveryDate;
import com.example.sms.domain.model.master.partner.supplier.SupplierCode;
import com.example.sms.domain.type.money.Money;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistory;
import com.example.sms.domain.model.system.user.User;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.model.system.audit.ApplicationExecutionProcessFlag;
import com.example.sms.domain.model.system.user.RoleName;
import com.example.sms.domain.model.inventory.Inventory;
import com.example.sms.service.inventory.InventoryRepository;
import com.example.sms.service.master.payment.PaymentAccountRepository;
import com.example.sms.service.master.region.RegionRepository;
import com.example.sms.service.master.department.DepartmentRepository;
import com.example.sms.service.master.employee.EmployeeRepository;
import com.example.sms.service.master.partner.PartnerCategoryRepository;
import com.example.sms.service.master.partner.PartnerGroupRepository;
import com.example.sms.service.master.partner.PartnerRepository;
import com.example.sms.service.master.product.ProductCategoryRepository;
import com.example.sms.service.master.product.ProductRepository;
import com.example.sms.service.sales.invoice.InvoiceRepository;
import com.example.sms.service.sales.payment.incoming.PaymentRepository;
import com.example.sms.service.sales.sales.SalesRepository;
import com.example.sms.service.sales.order.SalesOrderRepository;
import com.example.sms.service.procurement.purchase.PurchaseOrderRepository;
import com.example.sms.service.system.audit.AuditRepository;
import com.example.sms.service.system.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

@Component
public class TestDataFactoryImpl implements TestDataFactory {
    @Autowired
    UserRepository userRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductCategoryRepository productCategoryRepository;
    @Autowired
    AuditRepository auditRepository;
    @Autowired
    RegionRepository regionRepository;
    @Autowired
    PartnerGroupRepository partnerGroupRepository;
    @Autowired
    PartnerCategoryRepository partnerCategoryRepository;
    @Autowired
    PartnerRepository partnerRepository;
    @Autowired
    SalesOrderRepository salesOrderRepository;
    @Autowired
    SalesRepository salesRepository;
    @Autowired
    InvoiceRepository invoiceRepository;
    @Autowired
    PaymentAccountRepository paymentAccountRepository;
    @Autowired
    PaymentRepository paymentIncomingRepository;
    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;
    @Autowired
    InventoryRepository inventoryRepository;

    @Override
    public void setUpForAuthApiService() {
        setUpUser();
    }

    @Override
    public void setUpForUserManagementService() {
        setUpUser();
    }

    @Override
    public void setUpForDepartmentService() {
        departmentRepository.deleteAll();
        departmentRepository.save(getDepartment("10000", LocalDateTime.of(2021, 1, 1, 0, 0), "部門1"));
        departmentRepository.save(getDepartment("20000", LocalDateTime.of(2021, 1, 1, 0, 0), "部門2"));
        employeeRepository.deleteAll();
        employeeRepository.save(getEmployee("EMP001", "10000", LocalDateTime.of(2021, 1, 1, 0, 0)));
        employeeRepository.save(getEmployee("EMP002", "10000", LocalDateTime.of(2021, 1, 1, 0, 0)));
    }

    @Override
    public void setUpForEmployeeService() {
        userRepository.save(getUser());
        LocalDateTime startDate = LocalDateTime.of(2021, 1, 1, 0, 0);
        departmentRepository.deleteAll();
        departmentRepository.save(getDepartment("10000", startDate, "部門1"));
        employeeRepository.deleteAll();
        employeeRepository.save(getEmployee("EMP001", "10000", startDate));
        employeeRepository.save(getEmployee("EMP002", "10000", startDate));
    }

    @Override
    public void setUpForProductService() {
        productRepository.deleteAll();

        String productCode = "99999001";
        SubstituteProduct substituteProduct = getSubstituteProduct(productCode, "99999002", 1);
        Bom bom = getBom(productCode, "X99", 1);
        CustomerSpecificSellingPrice customerSpecificSellingPrice = getCustomerSpecificSellingPrice(productCode, "1", 1);
        Product product = Product.of(productCode, "商品1", "商品1", "しょうひん1", ProductType.その他, 100, 90, 10, TaxType.外税, "カテゴリ1", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "001", 1);

        productRepository.save(Product.of(product, List.of(substituteProduct), List.of(bom), List.of(customerSpecificSellingPrice)));
        productRepository.save(Product.of("99999002", "商品2", "商品2", "しょうひん2", ProductType.その他, 200, 180, 20, TaxType.内税, "カテゴリ1", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "002", 2));
        productRepository.save(Product.of("99999003", "商品3", "商品3", "しょうひん3", ProductType.その他, 300, 270, 30, TaxType.非課税, "カテゴリ2", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "003", 3));

        productCategoryRepository.deleteAll();
        productCategoryRepository.save(ProductCategory.of("カテゴリ1", "カテゴリ1", 1, "カテゴリ1", 1));
        productCategoryRepository.save(ProductCategory.of("カテゴリ2", "カテゴリ2", 2, "カテゴリ1~カテゴリ2", 2));
    }

    @Override
    public void setUpForAuditService() {
        User execUser = User.of("U777777", getUser().getPassword().Value(), getUser().getName().FirstName(), getUser().getName().LastName(), getUser().getRoleName());
        userRepository.save(execUser);

        auditRepository.deleteAll();
        ApplicationExecutionHistory applicationExecutionHistory = ApplicationExecutionHistory.of(null, "その他", "9999", ApplicationExecutionHistoryType.同期, LocalDateTime.of(2024,1,1,1,0), LocalDateTime.of(2024,1,1,2,0), ApplicationExecutionProcessFlag.未実行,  "processDetails", execUser);
        auditRepository.save(applicationExecutionHistory);
        auditRepository.save(applicationExecutionHistory);
    }

    @Override
    public void setUpForDownloadService() {
        departmentRepository.deleteAll();
        departmentRepository.save(getDepartment("30000", LocalDateTime.of(2021, 1, 1, 0, 0), "部門3"));
        departmentRepository.save(getDepartment("40000", LocalDateTime.of(2021, 1, 1, 0, 0), "部門4"));
        departmentRepository.save(getDepartment("50000", LocalDateTime.of(2021, 1, 1, 0, 0), "部門5"));
        employeeRepository.deleteAll();
        userRepository.save(getUser());
        employeeRepository.save(getEmployee("EMP003", "30000", LocalDateTime.of(2021, 1, 1, 0, 0)));
        employeeRepository.save(getEmployee("EMP004", "30000", LocalDateTime.of(2021, 1, 1, 0, 0)));
        employeeRepository.save(getEmployee("EMP005", "40000", LocalDateTime.of(2021, 1, 1, 0, 0)));
        productCategoryRepository.deleteAll();
        productCategoryRepository.save(ProductCategory.of("00000001", "カテゴリ3", 1, "2", 3));
        productCategoryRepository.save(ProductCategory.of("00000002", "カテゴリ4", 1, "2", 3));
        productCategoryRepository.save(ProductCategory.of("00000003", "カテゴリ5", 1, "2", 3));
        productRepository.deleteAll();
        productRepository.save(Product.of("99999999", "商品1", "商品1", "ショウヒンイチ", ProductType.その他, 900, 810, 90, TaxType.外税, "カテゴリ9", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "009", 9));
        productRepository.save(Product.of("99999998", "商品2", "商品2", "ショウヒン二", ProductType.その他, 800, 720, 80, TaxType.外税, "カテゴリ8", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "008", 8));
        productRepository.save(Product.of("99999997", "商品3", "商品3", "ショウヒンサン", ProductType.その他, 700, 630, 70, TaxType.外税, "カテゴリ7", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "007", 7));

        partnerGroupRepository.deleteAll();
        partnerGroupRepository.save(PartnerGroup.of("0001", "取引先グループ1"));
        partnerGroupRepository.save(PartnerGroup.of("0002", "取引先グループ2"));

        partnerCategoryRepository.deleteAll();
        partnerRepository.deleteAll();
        partnerRepository.save(getPartner("001"));
        partnerRepository.save(getPartner("002"));
        partnerRepository.save(getPartner("003"));
        partnerRepository.save(Partner.ofWithCustomers(getPartner("001"), List.of(getCustomer("001", 1), getCustomer("001", 2), getCustomer("001", 3))));
        partnerRepository.save(Partner.ofWithVendors(getPartner("002"), List.of(getVendor("002", 1), getVendor("002", 2), getVendor("002", 3))));

        salesOrderRepository.deleteAll();
        Order order1 = getSalesOrder("OD00000001");
        IntStream.rangeClosed(1, 3).forEach(i -> {
            OrderLine line = getSalesOrderLine(order1.getOrderNumber().getValue(), i);
            salesOrderRepository.save(Order.of(order1, List.of(line)));
        });
        Order order2 = getSalesOrder("OD00000002");
        IntStream.rangeClosed(1, 3).forEach(i -> {
            OrderLine line = getSalesOrderLine(order2.getOrderNumber().getValue(), i);
            salesOrderRepository.save(Order.of(order2, List.of(line)));
        });
        Order order3 = getSalesOrder("OD00000003");
        IntStream.rangeClosed(1, 3).forEach(i -> {
            OrderLine line = getSalesOrderLine(order3.getOrderNumber().getValue(), i);
            salesOrderRepository.save(Order.of(order3, List.of(line)));
        });
        // 商品データの準備
        productRepository.save(Product.of(
                "99999999", // 商品コード
                "商品1",    // 商品名
                "商品1",    // 商品名カナ
                "ショウヒンイチ", // 商品英語名
                ProductType.その他, // 商品種別
                900, // 商品標準価格
                810, // 売上単価
                90,  // 利益額
                TaxType.その他, // 税種別
                "カテゴリ9", // カテゴリ
                MiscellaneousType.適用外, // 雑費区分
                StockManagementTargetType.対象, // 在庫管理対象
                StockAllocationType.引当済, // 在庫引当区分
                "009", // 倉庫コード
                9    // 入荷リードタイム
        ));

        // 部門データの準備
        Department department = departmentRepository.findById(DepartmentId.of("30000", LocalDateTime.of(2021, 1, 1, 0, 0))).orElseThrow();

        // 取引先データの準備
        Customer customer = partnerRepository.findCustomerById(CustomerCode.of("001", 1)).orElseThrow();

        // 社員データの準備
        Employee employee = employeeRepository.findById(EmployeeCode.of("EMP003")).orElseThrow();

        // 出荷データの準備
        setUpForShippingService();

        // 請求データの準備
        invoiceRepository.deleteAll();

        // 売上データの削除
        salesRepository.deleteAll();

        // 売上データの準備
        IntStream.rangeClosed(1, 3).forEach(i -> {
            // 売上番号をフォーマット
            String salesNumber = String.format("SA%08d", i);
            // 受注番号をフォーマット
            String orderNumber = salesNumber.replace("SA", "OD");

            // 売上明細の準備
            List<SalesLine> salesLines = IntStream.range(1, 4)
                    .mapToObj((IntFunction<SalesLine>) lineNumber -> SalesLine.of(
                            salesNumber,
                            lineNumber,
                            orderNumber,
                            lineNumber,
                            "99999999", // 商品コード
                            "商品1",    // 商品名
                            800, // 売上単価
                            10, // 売上数量
                            10, // 出荷数量
                            0, // 値引金額
                            (LocalDateTime.of(2021, 1, 1, 0, 0)), // 請求日
                            "B001", // 請求番号
                            0, // 請求遅延区分
                            (LocalDateTime.of(2021, 1, 1, 0, 0)), // 自動仕訳日,
                            null,
                            TaxRateType.標準税率
                    ))
                    .toList();

            // 売上エンティティの作成
            Sales newSales = Sales.of(
                    salesNumber,
                    salesNumber.replace("SA", "OD"), // 仮登録用の受注番号
                    (LocalDateTime.of(2021, 1, 1, 0, 0)), // 売上日
                    1, // 売上区分
                    department.getDepartmentId().getDeptCode().getValue(), // 部門コード
                    department.getDepartmentId().getDepartmentStartDate().getValue(), // 部門開始日
                    customer.getCustomerCode().getCode().getValue(), // 取引先コード
                    customer.getCustomerCode().getBranchNumber(),
                    employee.getEmpCode().getValue(), // 社員コード
                    null, // 赤黒伝票番号
                    null, // 元伝票番号
                    "テスト備考", // 備考
                    salesLines // 売上明細
            );

            // 作成した売上データを保存
            salesRepository.save(newSales);

            // 請求データの準備
            setUpForInvoiceAcceptanceService();
            
            // 口座データの準備
            paymentAccountRepository.deleteAll();
            IntStream.range(1, 4).forEach(j -> {
                PaymentAccount paymentAccount = getPaymentAccount(String.format("ACC%03d", j));
                paymentAccountRepository.save(paymentAccount);
            });

            // 入金データの準備
            setUpForPaymentIncomingService();

            // 在庫データの準備
            setUpForInventoryService();
        });
    }

    private void setUpUser() {
        userRepository.deleteAll();
        userRepository.save(getUser());
        userRepository.save(getAdmin());
        productRepository.save(Product.of("99999999", "商品1", "商品1", "ショウヒンイチ", ProductType.その他, 900, 810, 90, TaxType.その他, "カテゴリ9", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "009", 9));
        productRepository.save(Product.of("99999998", "商品2", "商品2", "ショウヒン二", ProductType.その他, 800, 720, 80, TaxType.その他, "カテゴリ8", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "008", 8));
        productRepository.save(Product.of("99999997", "商品3", "商品3", "ショウヒンサン", ProductType.その他, 700, 630, 70, TaxType.その他, "カテゴリ7", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "007", 7));
    }

    @Override
    public void setUpForRegionService() {
        regionRepository.deleteAll();

        IntStream.rangeClosed(1, 3).forEach(i -> {
            Region region = getRegion("R00" + i);
            region = Region.of(region.getRegionCode().getValue(), "地域名" + i);
            regionRepository.save(region);
        });
    }

    @Override
    public void setUpForPartnerGroupService() {
        partnerGroupRepository.deleteAll();
        IntStream.rangeClosed(0, 9).forEach(i -> {
            PartnerGroup partnerGroup = partnerGroup("000" + i);
            partnerGroupRepository.save(PartnerGroup.of(partnerGroup.getPartnerGroupCode().getValue(), "取引先グループ" + i));
        });
    }

    @Override
    public void setUpForPartnerCategoryService() {
        partnerCategoryRepository.deleteAll();
        partnerRepository.deleteAll();

        IntStream.rangeClosed(1, 4).forEach(i -> {
            Partner partner = getPartner("00" + i);
            partnerRepository.save(partner);
        });

        PartnerCategoryAffiliation partnerCategoryAffiliation = getPartnerCategoryAffiliation("1", "001", "01");
        PartnerCategoryAffiliation partnerCategoryAffiliation2 = getPartnerCategoryAffiliation("1", "002", "01");
        List<PartnerCategoryAffiliation> partnerCategoryAffiliations = List.of(partnerCategoryAffiliation, partnerCategoryAffiliation2);
        PartnerCategoryAffiliation partnerCategoryAffiliation3 = getPartnerCategoryAffiliation("1", "003", "02");
        PartnerCategoryAffiliation partnerCategoryAffiliation4 = getPartnerCategoryAffiliation("1", "004", "02");
        List<PartnerCategoryAffiliation> partnerCategoryAffiliations2 = List.of(partnerCategoryAffiliation3, partnerCategoryAffiliation4);
        PartnerCategoryItem partnerCategoryItem = PartnerCategoryItem.of(getPartnerCategoryItem("1", "01"), partnerCategoryAffiliations);
        PartnerCategoryItem partnerCategoryItem2 = PartnerCategoryItem.of(getPartnerCategoryItem("1", "02"), partnerCategoryAffiliations2);
        List<PartnerCategoryItem> partnerCategoryItems = List.of(partnerCategoryItem, partnerCategoryItem2);
        PartnerCategoryType partnerCategoryType = PartnerCategoryType.of(getPartnerCategoryType("1"), partnerCategoryItems);
        partnerCategoryRepository.save(partnerCategoryType);
    }

    @Override
    public void setUpForPartnerService() {
        partnerCategoryRepository.deleteAll();
        partnerRepository.deleteAll();

        IntStream.rangeClosed(1, 4).forEach(i -> {
            Partner partner = getPartner("00" + i);
            partnerRepository.save(partner);
        });
    }

    @Override
    public void setUpForCustomerService() {
        partnerCategoryRepository.deleteAll();
        partnerRepository.deleteAll();

        IntStream.rangeClosed(1, 1).forEach(i -> {
            Partner partner = getPartner("00" + i);
            List<Customer> customers = IntStream.rangeClosed(1, 3)
                    .mapToObj(j -> getCustomer("00" + i, j))
                    .toList();
            partnerRepository.save(Partner.ofWithCustomers(partner,customers));
        });
    }

    @Override
    public void setUpForVendorService() {
        partnerCategoryRepository.deleteAll();
        partnerRepository.deleteAll();

        IntStream.rangeClosed(1, 1).forEach(i -> {
            Partner partner = getPartner("00" + i);
            List<Vendor> vendors = IntStream.rangeClosed(1, 3)
                    .mapToObj(j -> getVendor("00" + i, j))
                    .toList();
            partnerRepository.save(Partner.ofWithVendors(partner,vendors));
        });
    }

    @Override
    public void setUpForOrderService() {
        Department department = getDepartment("10000", LocalDateTime.of(2021, 1, 1, 0, 0), "部門1");
        departmentRepository.save(department);
        Employee employee = getEmployee("EMP001", "10000", LocalDateTime.of(2021, 1, 1, 0, 0));
        employeeRepository.save(employee);
        Partner partner = getPartner("001");
        Customer customer = getCustomer(partner.getPartnerCode().getValue(), 1);
        List<Shipping> shippingList = IntStream.rangeClosed(1, 3)
                .mapToObj(i -> getShipping(partner.getPartnerCode().getValue(), i, customer.getCustomerCode().getBranchNumber()))
                .toList();
        partnerRepository.save(Partner.ofWithCustomers(partner, List.of(Customer.of(customer, shippingList))));
        productRepository.save(Product.of("99999999", "商品1", "商品1", "ショウヒンイチ", ProductType.その他, 900, 810, 90, TaxType.外税, "カテゴリ9", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "009", 9));

        salesOrderRepository.deleteAll();

        IntStream.rangeClosed(1, 3).forEach(i -> {
            String orderNumber = String.format("OD%08d", i);
            Order order = getSalesOrder(orderNumber);
            List<OrderLine> lines = IntStream.range(1, 4)
                    .mapToObj(lineNumber -> getSalesOrderLine(order.getOrderNumber().getValue(), lineNumber))
                    .toList();
            Order newOrder = Order.of(
                    order.getOrderNumber().getValue(),
                    order.getOrderDate().getValue(),
                    department.getDepartmentId().getDeptCode().getValue(),
                    department.getDepartmentId().getDepartmentStartDate().getValue(),
                    customer.getCustomerCode().getCode().getValue(),
                    customer.getCustomerCode().getBranchNumber(),
                    employee.getEmpCode().getValue(),
                    order.getDesiredDeliveryDate().getValue(),
                    order.getCustomerOrderNumber(),
                    order.getWarehouseCode(),
                    order.getTotalOrderAmount().getAmount(),
                    order.getTotalConsumptionTax().getAmount(),
                    order.getRemarks(),
                    lines);
            salesOrderRepository.save(newOrder);
        });
    }

    @Override
    public void setUpForOrderUploadService() {
        salesOrderRepository.deleteAll();
        setUpForDepartmentService();
        setUpForProductService();
        setUpForPartnerService();
        setUpForEmployeeService();
    }

    @Override
    public void setUpForOrderRuleCheckService() {
        salesOrderRepository.deleteAll();
        setUpForDepartmentService();
        setUpForProductService();
        setUpForPartnerService();
        setUpForEmployeeService();
    }

    @Override
    public void setUpForShippingService() {
        setUpForOrderService();

        OrderList orderList = salesOrderRepository.selectAll();

        salesOrderRepository.deleteAll();

        orderList.asList().forEach(
                salesOrder -> {
                    List<OrderLine> lines = salesOrder.getOrderLines();
                    List<OrderLine> newLines = new ArrayList<>();
                    for (OrderLine line : lines) {
                        OrderLine newLine = OrderLine.of(
                                line.getOrderNumber().getValue(),  // orderNumber（受注番号）
                                line.getOrderLineNumber(),  // orderLineNumber（受注行番号）
                                "99999999",  // productCode（商品コード）
                                "商品1",  // productName（商品名）
                                1000,  // salesUnitPrice（販売単価）
                                10,  // orderQuantity（受注数量）
                                10,  // taxRate（消費税率）
                                10,  // allocationQuantity（引当数量）
                                10,  // shipmentInstructionQuantity（出荷指示数量）
                                10,  // shippedQuantity（出荷済数量）
                                1,  // completionFlag（完了フラグ）
                                10,  // discountAmount（値引金額）
                                LocalDateTime.of(2021, 1, 1, 0, 0),  // deliveryDate（納期）
                                LocalDateTime.of(2021, 1, 1, 0, 0)  // shippingDate（出荷日）
                        );
                        newLines.add(newLine);
                    }
                    Order newOrder = Order.of(salesOrder, newLines);
                    salesOrderRepository.save(newOrder);
                }
        );
    }

    @Override
    public void setUpForShippingRuleCheckService() {
        salesOrderRepository.deleteAll();
        setUpForDepartmentService();
        setUpForProductService();
        setUpForPartnerService();
        setUpForEmployeeService();
    }

    @Override
    public void setUpForSalesService() {
        // 部門データの準備
        Department department = getDepartment("10000", LocalDateTime.of(2021, 1, 1, 0, 0), "部門1");
        departmentRepository.save(department);

        // 社員データの準備
        Employee employee = getEmployee("EMP001", "10000", LocalDateTime.of(2021, 1, 1, 0, 0));
        employeeRepository.save(employee);

        // 取引先・顧客データの準備
        Partner partner = getPartner("001");
        Customer customer = getCustomer(partner.getPartnerCode().getValue(), 1);
        List<Shipping> shippingList = IntStream.rangeClosed(1, 3)
                .mapToObj(i -> getShipping(partner.getPartnerCode().getValue(), i, customer.getCustomerCode().getBranchNumber()))
                .toList();
        partnerRepository.save(Partner.ofWithCustomers(partner, List.of(Customer.of(customer, shippingList))));

        // 商品データの準備
        productRepository.save(Product.of(
                "99999999", // 商品コード
                "商品1",    // 商品名
                "商品1",    // 商品名カナ
                "ショウヒンイチ", // 商品英語名
                ProductType.その他, // 商品種別
                900, // 商品標準価格
                810, // 売上単価
                90,  // 利益額
                TaxType.その他, // 税種別
                "カテゴリ9", // カテゴリ
                MiscellaneousType.適用外, // 雑費区分
                StockManagementTargetType.対象, // 在庫管理対象
                StockAllocationType.引当済, // 在庫引当区分
                "009", // 倉庫コード
                9    // 入荷リードタイム
        ));

        // 出荷データの準備
        setUpForShippingService();

        // 請求データの準備
        invoiceRepository.deleteAll();

        // 売上データの削除
        salesRepository.deleteAll();

        // 売上データの準備
        IntStream.rangeClosed(1, 3).forEach(i -> {
            // 売上番号をフォーマット
            String salesNumber = String.format("SA%08d", i);
            // 受注番号をフォーマット
            String orderNumber = salesNumber.replace("SA", "OD");

            // 売上明細の準備
            List<SalesLine> salesLines = IntStream.range(1, 4)
                    .mapToObj((IntFunction<SalesLine>) lineNumber -> SalesLine.of(
                            salesNumber,
                            lineNumber,
                            orderNumber,
                            lineNumber,
                            "99999999", // 商品コード
                            "商品1",    // 商品名
                            800, // 売上単価
                            10, // 売上数量
                            10, // 出荷数量
                            0, // 値引金額
                            LocalDateTime.now(), // 請求日
                            "B001", // 請求番号
                            0, // 請求遅延区分
                            LocalDateTime.now(), // 自動仕訳日,
                            null,
                            TaxRateType.標準税率
                    ))
                    .toList();

            // 売上エンティティの作成
            Sales newSales = Sales.of(
                    salesNumber,
                    salesNumber.replace("SA", "OD"), // 仮登録用の受注番号
                    LocalDateTime.now(), // 売上日
                    1, // 売上区分
                    department.getDepartmentId().getDeptCode().getValue(), // 部門コード
                    department.getDepartmentId().getDepartmentStartDate().getValue(), // 部門開始日
                    customer.getCustomerCode().getCode().getValue(), // 取引先コード
                    customer.getCustomerCode().getBranchNumber(),
                    employee.getEmpCode().getValue(), // 社員コード
                    null, // 赤黒伝票番号
                    null, // 元伝票番号
                    "テスト備考", // 備考
                    salesLines // 売上明細
            );

            // 作成した売上データを保存
            salesRepository.save(newSales);
        });
    }

    @Override
    public void setUpForSalesServiceForAggregate() {
        // 請求データの削除
        invoiceRepository.deleteAll();

        // 売上データの削除
        salesRepository.deleteAll();

        // 受注データの削除
        salesOrderRepository.deleteAll();
    }

    @Override
    public void setUpForInvoiceService() {
        // 取引先・顧客データの準備
        Partner partner = getPartner("009");
        String partnerCode = partner.getPartnerCode().getValue();
        CustomerBillingCategory customerBillingCategory = CustomerBillingCategory.都度請求;
        ClosingBilling closingBilling = ClosingBilling.of(20, 1, 99, 1);
        Billing billing = new Billing(customerBillingCategory, closingBilling, closingBilling);
        Customer customer = TestDataFactoryImpl.getCustomer("009", 1).toBuilder()
                .billing(billing)
                .build();
        CustomerCode customerCode = customer.getCustomerCode();
        List<Shipping> shippingList = IntStream.rangeClosed(1, 3)
                .mapToObj(i -> getShipping(partnerCode, i, customerCode.getBranchNumber()))
                .toList();
        partnerRepository.save(Partner.ofWithCustomers(partner, List.of(Customer.of(customer, shippingList))));

        Partner partner2 = getPartner("010");
        String partnerCode2 = partner2.getPartnerCode().getValue();
        CustomerBillingCategory customerBillingCategory2 = CustomerBillingCategory.締請求;
        ClosingBilling closingBilling2_1 = ClosingBilling.of(10, 0, 10, 1);
        ClosingBilling closingBilling2_2 = ClosingBilling.of(10, 0, 10, 1);
        Billing billing2 = new Billing(customerBillingCategory2, closingBilling2_1, closingBilling2_2);
        Customer customer2 = TestDataFactoryImpl.getCustomer("010", 1).toBuilder()
                .billing(billing2)
                .build();
        CustomerCode customerCode2 = customer.getCustomerCode();
        List<Shipping> shippingList2 = IntStream.rangeClosed(1, 3)
                .mapToObj(i -> getShipping(partnerCode2, i, customerCode2.getBranchNumber()))
                .toList();
        partnerRepository.save(Partner.ofWithCustomers(partner2, List.of(Customer.of(customer2, shippingList2))));

        Partner partner3 = getPartner("011");
        String partnerCode3 = partner3.getPartnerCode().getValue();
        CustomerBillingCategory customerBillingCategory3 = CustomerBillingCategory.締請求;
        ClosingBilling closingBilling3_1 = ClosingBilling.of(20, 1, 20, 1);
        ClosingBilling closingBilling3_2 = ClosingBilling.of(20, 1, 20, 1);
        Billing billing3 = new Billing(customerBillingCategory3, closingBilling3_1, closingBilling3_2);
        Customer customer3 = TestDataFactoryImpl.getCustomer("011", 1).toBuilder()
                .billing(billing3)
                .build();
        CustomerCode customerCode3 = customer.getCustomerCode();
        List<Shipping> shippingList3 = IntStream.rangeClosed(1, 3)
                .mapToObj(i -> getShipping(partnerCode3, i, customerCode3.getBranchNumber()))
                .toList();
        partnerRepository.save(Partner.ofWithCustomers(partner3, List.of(Customer.of(customer3, shippingList3))));

        Partner partner4 = getPartner("012");
        String partnerCode4 = partner4.getPartnerCode().getValue();
        CustomerBillingCategory customerBillingCategory4 = CustomerBillingCategory.締請求;
        ClosingBilling closingBilling4_1 = ClosingBilling.of(99, 2, 99, 1);
        ClosingBilling closingBilling4_2 = ClosingBilling.of(99, 2, 99, 1);
        Billing billing4 = new Billing(customerBillingCategory4, closingBilling4_1, closingBilling4_2);
        Customer customer4 = TestDataFactoryImpl.getCustomer("012", 1).toBuilder()
                .billing(billing4)
                .build();
        CustomerCode customerCode4 = customer.getCustomerCode();
        List<Shipping> shippingList4 = IntStream.rangeClosed(1, 3)
                .mapToObj(i -> getShipping(partnerCode4, i, customerCode4.getBranchNumber()))
                .toList();
        partnerRepository.save(Partner.ofWithCustomers(partner4, List.of(Customer.of(customer4, shippingList4))));

        Partner partner5 = getPartner("013");
        String partnerCode5 = partner5.getPartnerCode().getValue();
        CustomerBillingCategory customerBillingCategory5 = CustomerBillingCategory.締請求;
        ClosingBilling closingBilling5_1 = ClosingBilling.of(10, 0, 10, 1);
        ClosingBilling closingBilling5_2 = ClosingBilling.of(20, 1, 20, 1);
        Billing billing5 = new Billing(customerBillingCategory5, closingBilling5_1, closingBilling5_2);
        Customer customer5 = TestDataFactoryImpl.getCustomer("013", 1).toBuilder()
                .billing(billing5)
                .build();
        CustomerCode customerCode5 = customer.getCustomerCode();
        List<Shipping> shippingList5 = IntStream.rangeClosed(1, 3)
                .mapToObj(i -> getShipping(partnerCode5, i, customerCode5.getBranchNumber()))
                .toList();
        partnerRepository.save(Partner.ofWithCustomers(partner5, List.of(Customer.of(customer5, shippingList5))));

        Partner partner6 = getPartner("014");
        String partnerCode6 = partner6.getPartnerCode().getValue();
        CustomerBillingCategory customerBillingCategory6 = CustomerBillingCategory.締請求;
        ClosingBilling closingBilling6_1 = ClosingBilling.of(20, 1, 20, 1);
        ClosingBilling closingBilling6_2 = ClosingBilling.of(99, 2, 99, 1);
        Billing billing6 = new Billing(customerBillingCategory6, closingBilling6_1, closingBilling6_2);
        Customer customer6 = TestDataFactoryImpl.getCustomer("014", 1).toBuilder()
                .billing(billing6)
                .build();
        CustomerCode customerCode6 = customer.getCustomerCode();
        List<Shipping> shippingList6 = IntStream.rangeClosed(1, 3)
                .mapToObj(i -> getShipping(partnerCode6, i, customerCode6.getBranchNumber()))
                .toList();
        partnerRepository.save(Partner.ofWithCustomers(partner6, List.of(Customer.of(customer6, shippingList6))));

        setUpForSalesServiceForAggregate();
    }

    @Override
    public void setUpForInvoiceAcceptanceService() {
        IntStream.range(0, 3).forEach(i -> {
            Invoice invoice = getInvoice(String.format("IV%08d", i));
            invoiceRepository.save(invoice);
        });

        Sales sales = getSales("SA12345678");
        SalesLine salesLine = getSalesLine(sales.getSalesNumber().getValue(), 1);
        salesRepository.save(sales.toBuilder().salesLines(List.of(salesLine)).build());
    }

    @Override
    public void setUpForPaymentAccountService() {
        paymentAccountRepository.deleteAll();
        IntStream.range(0, 3).forEach(i -> {
            PaymentAccount paymentAccount = getPaymentAccount(String.format("ACC%03d", i));
            paymentAccountRepository.save(paymentAccount);
        });
    }

    @Override
    public void setUpForPaymentIncomingService() {
        paymentIncomingRepository.deleteAll();
        IntStream.range(0, 3).forEach(i -> {
            Payment paymentIncoming = getPaymentData(String.format("PAY%03d", i));
            paymentIncomingRepository.save(paymentIncoming);
        });
    }

    @Override
    public MultipartFile createOrderFile() {
        InputStream is = getClass().getResourceAsStream("/csv/order/order_multiple.csv");
        try {
            return new MockMultipartFile(
                    "order_multiple.csv",
                    "order_multiple.csv",
                    "text/csv",
                    is
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MultipartFile createOrderInvalidFile() {
        InputStream is = getClass().getResourceAsStream("/csv/order/order_unregistered_code.csv");
        try {
            return new MockMultipartFile(
                    "order_multiple.csv",
                    "order_multiple.csv",
                    "text/csv",
                    is
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MultipartFile createOrderCheckRuleFile() {
        InputStream is = getClass().getResourceAsStream("/csv/order/order_check_rule.csv");
        try {
            return new MockMultipartFile(
                    "order_multiple.csv",
                    "order_multiple.csv",
                    "text/csv",
                    is
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static User getUser() {
        return User.of("U999999", "$2a$10$oxSJl.keBwxmsMLkcT9lPeAIxfNTPNQxpeywMrF7A3kVszwUTqfTK", "first", "last", RoleName.USER);
    }

    public static User getAdmin() {
        return User.of("U888888", "$2a$10$oxSJl.keBwxmsMLkcT9lPeAIxfNTPNQxpeywMrF7A3kVszwUTqfTK", "first", "last", RoleName.USER);
    }

    public static Department getDepartment(String departmentId, LocalDateTime startDate, String departmentName) {
        return Department.of(DepartmentId.of(departmentId, startDate), LocalDateTime.of(9999, 12, 31, 0, 0), departmentName, 0, departmentId + "~", 0, 1);
    }

    public static Employee getEmployee(String empCode, String departmentCode, LocalDateTime startDate) {
        return Employee.of(
                Employee.of(empCode, "firstName lastName", "firstNameKana lastNameKana", "090-1234-5678", "03-1234-5678", ""),
                getDepartment(departmentCode, startDate, "部門1"),
                getUser()
        );
    }

    public static Product getProduct(String productCode) {
        return Product.of(productCode, "商品正式名", "商品略称", "商品名カナ", ProductType.その他, 1000, 2000, 3000, TaxType.外税, "99999999", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "000", 5);
    }

    public static SubstituteProduct getSubstituteProduct(String productCode, String substituteProductCode, Integer priority) {
        return SubstituteProduct.of(productCode, substituteProductCode, priority);
    }

    public static Bom getBom(String productCode, String componentCode, int i) {
        return Bom.of(productCode, componentCode, i);
    }

    public static CustomerSpecificSellingPrice getCustomerSpecificSellingPrice(String productCode, String format, int i) {
        return CustomerSpecificSellingPrice.of(productCode, format, i);
    }

    public static ProductCategory getProductCategory(String productCategoryCode) {
        return ProductCategory.of(productCategoryCode, "カテゴリ9", 1, "2", 3);
    }

    public static Region getRegion(String regionCode) {
        return Region.of(regionCode, "地域名");
    }

    public static PartnerGroup partnerGroup(String groupCode) {
        return PartnerGroup.of(groupCode, "取引先グループ");
    }

    public static PartnerCategoryType getPartnerCategoryType(String categoryCode) {
        return PartnerCategoryType.of(categoryCode, "取引先分類種別名");
    }

    public static PartnerCategoryItem getPartnerCategoryItem(String categoryCode, String itemCode) {
        return PartnerCategoryItem.of(categoryCode, itemCode, "取引先分類名");
    }

    public static PartnerCategoryAffiliation getPartnerCategoryAffiliation(String categoryCode, String partnerCode, String itemCode) {
        return PartnerCategoryAffiliation.of(categoryCode, partnerCode, itemCode);
    }

    public static Partner getPartner(String partnerCode) {
        return  Partner.of(
                partnerCode,
                "取引先名A",
                "トリヒキサキメイエー",
                1,
                "1234567",
                "東京都",
                "中央区銀座1-1-1",
                "ビル名201",
                0,
                0,
                "PG01",
                1000000,
                50000
        );
    }

    public static Customer getCustomer(String customerCode, Integer customerBranchNumber) {
        return Customer.of(
                customerCode,  // customerCode（顧客コード）
                customerBranchNumber,  // customerBranchNumber（顧客枝番）
                1,  // customerCategory（顧客区分）
                "001",  // billingCode（請求先コード）
                1,  // billingBranchNumber（請求先枝番）
                "001",  // collectionCode（回収先コード）
                1,  // collectionBranchNumber（回収先枝番）
                "山田太郎",  // customerName（顧客名）
                "ヤマダタロウ",  // customerNameKana（顧客名カナ）
                "RE001",  // companyRepresentativeCode（自社担当者コード）
                "花子",  // customerRepresentativeName（顧客担当者名）
                "営業部",  // customerDepartmentName（顧客部門名）
                "123-4567",  // customerPostalCode（顧客郵便番号）
                "東京都",  // customerPrefecture（顧客都道府県）
                "新宿区1-1-1",  // customerAddress1（顧客住所１）
                "マンション101号室",  // customerAddress2（顧客住所２）
                "03-1234-5678",  // customerPhoneNumber（顧客電話番号）
                "03-1234-5679",  // customerFaxNumber（顧客FAX番号）
                "example@example.com",  // customerEmailAddress（顧客メールアドレス）
                2,  // customerBillingCategory（顧客請求区分）
                10,  // customerClosingDay1（顧客締日１）
                0,  // customerPaymentMonth1（顧客支払月１）
                10,  // customerPaymentDay1（顧客支払日１）
                1,  // customerPaymentMethod1（顧客支払方法１）
                20,  // customerClosingDay2（顧客締日２）
                1,  // customerPaymentMonth2（顧客支払月２）
                99,  // customerPaymentDay2（顧客支払日２）
                2   // customerPaymentMethod2（顧客支払方法２）
        );
    }

    public static Shipping getShipping(String customerCode, Integer destinationNumber, Integer customerBranchNumber) {
        return Shipping.of(
                customerCode,
                destinationNumber,
                customerBranchNumber,
                "出荷先名A",
                "R001",
                "123-4567",
                "東京都",
                "新宿区1-1-1"
        );
    }

    public static Vendor getVendor(String vendorCode, Integer vendorBranchCode) {
        return Vendor.of(
                vendorCode,
                vendorBranchCode,
                "仕入先名A",
                "シリヒキサキメイエー",
                "担当者名A",
                "部門名A",
                "123-4567",
                "東京都",
                "新宿区1-1-1",
                "マンション101号室",
                "03-1234-5678",
                "03-1234-5679",
                "test@example.com",
                10,
                1,
                20,
                2
        );
    }

    public static Order getSalesOrder(String orderNumber) {
        return Order.of(
                orderNumber,  // orderNumber（受注番号）
                LocalDateTime.of(2021, 1, 1, 0, 0),  // orderDate（受注日）
                "10009",  // departmentCode（部門コード）
                LocalDateTime.of(2021, 1, 1, 0, 0),  // departmentStartDate（部門開始日）
                "009",  // customerCode（顧客コード）
                1,  // customerBranchNumber（顧客枝番）
                "EMP009",  // employeeCode（社員コード）
                LocalDateTime.of(2021, 1, 1, 0, 0),  // desiredDeliveryDate（希望納期）
                "001",  // customerOrderNumber（客先注文番号）
                "001",  // warehouseCode（倉庫コード）
                1000,  // totalOrderAmount（受注金額合計）
                100,  // totalConsumptionTax（消費税合計）
                "備考",  // remarks（備考）
                List.of()
        );
    }

    public static OrderLine getSalesOrderLine(String orderNumber, int lineNumber) {
        return OrderLine.of(
                orderNumber,  // orderNumber（受注番号）
                lineNumber,  // orderLineNumber（受注行番号）
                "99999999",  // productCode（商品コード）
                "商品1",  // productName（商品名）
                1000,  // salesUnitPrice（販売単価）
                10,  // orderQuantity（受注数量）
                10,  // taxRate（消費税率）
                10,  // allocationQuantity（引当数量）
                10,  // shipmentInstructionQuantity（出荷指示数量）
                10,  // shippedQuantity（出荷済数量）
                0,  // completionFlag（完了フラグ）
                10,  // discountAmount（値引金額）
                LocalDateTime.of(2021, 1, 1, 0, 0),  // deliveryDate（納期）
                LocalDateTime.of(2021, 1, 1, 0, 0)  // shippingDate（出荷日）
        );
    }

    public static Sales getSales(String salesNumber) {
        return Sales.of(
                salesNumber,
                "OD00000009",
                LocalDateTime.of(2023, 10, 1, 0, 0),
                1,
                "10000",
                LocalDateTime.of(2023, 10, 1, 0, 0),
                "001",
                1,
                "EMP001",
                1,
                "V001",
                "テスト備考",
                List.of()
        );
    }

    public static SalesLine getSalesLine(String salesNumber, int lineNumber) {
        return SalesLine.of(
                salesNumber,
                lineNumber,
                salesNumber.replace("SA", "OD"),
                lineNumber,
                "99999999",
                "商品名",
                1000,
                10,
                10,
                10,
                LocalDateTime.of(2023, 10, 1, 0, 0),
                "001",
                0,
                LocalDateTime.of(2023, 10, 1, 0, 0),
                null,
                TaxRateType.標準税率
        );
    }

    public static Invoice getInvoice(String invoiceNumber) {
        return Invoice.of(
                invoiceNumber,
                LocalDateTime.now(),
                "001",
                1,
                10000,
                50000,
                20000,
                40000,
                4000,
                0,
                List.of()
        );
    }

    public static InvoiceLine getInvoiceLine(String invoiceNumber, String saleNumber, int lineNumber) {
        return  InvoiceLine.of(
                invoiceNumber,
                saleNumber,
                lineNumber
        );
    }

    public static PaymentAccount getPaymentAccount(String accountCode) {
        return PaymentAccount.of(
                accountCode,
                "テスト口座",
                LocalDateTime.of(2023, 10, 1, 0, 0), // 開始日
                LocalDateTime.of(2024, 10, 1, 0, 0), // 開始日
                "テスト口座（適用後）",
                "1", // 普通
                "1234567",
                "1", // 普通
                "テスト太郎",
                "10000",
                LocalDateTime.of(2023, 1, 1, 0, 0),
                "0001",
                "001"
        );
    }

    public static Payment getPaymentData(String paymentNumber) {
        return Payment.of(
                paymentNumber,
                LocalDateTime.of(2022, 1, 1, 0, 0),
                "10000",
                LocalDateTime.of(2021, 1, 1, 0, 0),
                "001",
                1,
                4, // 振込
                "ACC001",
                10000,
                0
        );
    }

    public static Payment getPaymentWithCustomer(String paymentNumber, String customerCode, Integer branchNumber) {
        return Payment.of(
                paymentNumber,
                LocalDateTime.now(),
                "10000",
                LocalDateTime.of(2021, 1, 1, 0, 0),
                customerCode,
                branchNumber,
                PaymentMethodType.振込.getCode(),
                "ACC001",
                10000,
                5000
        );
    }

    public static Payment getPaymentWithAccount(String paymentNumber, String accountCode) {
        return Payment.of(
                paymentNumber,
                LocalDateTime.now(),
                "10000",
                LocalDateTime.of(2021, 1, 1, 0, 0),
                "001",
                1,
                PaymentMethodType.振込.getCode(),
                accountCode,
                10000,
                5000
        );
    }

    @Override
    public void setUpForPurchaseOrderService() {
        setUpForUserManagementService();
        setUpForDepartmentService();
        setUpForEmployeeService();
        setUpForRegionService();
        setUpForPartnerGroupService();
        setUpForPartnerCategoryService();
        setUpForPartnerService();
        setUpForProductService();
        setUpPurchaseOrder();
    }

    @Override
    public void setUpForPurchaseOrderServiceWithErrors() {
        setUpForUserManagementService();
        setUpForDepartmentService();
        setUpForEmployeeService();
        setUpForRegionService();
        setUpForPartnerGroupService();
        setUpForPartnerCategoryService();
        setUpForPartnerService();
        setUpForProductService();
        setUpPurchaseOrderWithErrors();
    }

    private void setUpPurchaseOrder() {
        purchaseOrderRepository.deleteAll();
        List<PurchaseOrder> purchaseOrders = getPurchaseOrders();
        purchaseOrders.forEach(purchaseOrderRepository::save);
    }

    private void setUpPurchaseOrderWithErrors() {
        purchaseOrderRepository.deleteAll();
        List<PurchaseOrder> purchaseOrders = getPurchaseOrdersWithErrors();
        purchaseOrders.forEach(purchaseOrder -> {
            try {
                purchaseOrderRepository.save(purchaseOrder);
            } catch (Exception e) {
                // バリデーションエラーでもテスト用データとして保存する
                System.out.println("テスト用エラーデータ作成時のエラー: " + e.getMessage());
            }
        });
    }

    @Override
    public void setUpForInventoryService() {
        // 在庫データをクリア
        inventoryRepository.deleteAll();
        
        // 商品マスタデータの準備
        productRepository.deleteAll();
        productRepository.save(Product.of("10101001", "商品1", "商品1", "しょうひん1", ProductType.その他, 1000, 900, 100, TaxType.外税, "カテゴリ1", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "001", 1));
        productRepository.save(Product.of("10101002", "商品2", "商品2", "しょうひん2", ProductType.その他, 2000, 1800, 200, TaxType.内税, "カテゴリ1", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "002", 2));
        productRepository.save(Product.of("10103001", "商品3", "商品3", "しょうひん3", ProductType.その他, 3000, 2700, 300, TaxType.非課税, "カテゴリ2", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "003", 3));
        // InventoryUploadTest用の商品データ
        productRepository.save(Product.of("99999001", "テスト商品1", "テスト商品1", "てすとしょうひん1", ProductType.その他, 1000, 900, 100, TaxType.外税, "カテゴリ1", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "T01", 1));
        productRepository.save(Product.of("99999002", "テスト商品2", "テスト商品2", "てすとしょうひん2", ProductType.その他, 2000, 1800, 200, TaxType.内税, "カテゴリ1", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "T02", 2));
        productRepository.save(Product.of("99999003", "テスト商品3", "テスト商品3", "てすとしょうひん3", ProductType.その他, 3000, 2700, 300, TaxType.非課税, "カテゴリ1", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "T03", 3));
        
        // 既存の在庫データを作成（テスト用）
        // テストシナリオで使用される WH1/10101001/LOT001 とは異なるデータを作成
        inventoryRepository.save(getInventory("WH2", "10101001", "LOT002"));
        inventoryRepository.save(getInventory("WH3", "10101002", "LOT003"));
        inventoryRepository.save(getInventory("WH1", "10103001", "LOT004"));
    }

    private List<PurchaseOrder> getPurchaseOrders() {
        List<PurchaseOrder> purchaseOrders = new ArrayList<>();
        IntFunction<PurchaseOrder> getPurchaseOrder = i -> getPurchaseOrder("PO" + String.format("%08d", i));
        IntStream.range(1, 4).forEach(i -> purchaseOrders.add(getPurchaseOrder.apply(i)));
        return purchaseOrders;
    }

    private List<PurchaseOrder> getPurchaseOrdersWithErrors() {
        List<PurchaseOrder> purchaseOrders = new ArrayList<>();
        
        // 金額超過の発注（600万円）
        purchaseOrders.add(getPurchaseOrderWithAmountError("PE00000001"));
        
        // 納期が過去の発注
        purchaseOrders.add(getPurchaseOrderWithDeliveryError("PE00000002"));
        
        return purchaseOrders;
    }

    private PurchaseOrder getPurchaseOrderWithAmountError(String purchaseOrderNumber) {
        LocalDateTime now = LocalDateTime.now();
        
        List<PurchaseOrderLine> lines = List.of(
                getPurchaseOrderLineWithHighAmount(purchaseOrderNumber, 1)
        );

        // 金額超過の発注をbuilderで作成（バリデーションを回避）
        return PurchaseOrder.builder()
                .purchaseOrderNumber(PurchaseOrderNumber.of(purchaseOrderNumber))
                .purchaseOrderDate(PurchaseOrderDate.of(now))
                .salesOrderNumber(OrderNumber.of("OD25010001"))
                .supplierCode(SupplierCode.of("001", 0))
                .purchaseManagerCode(EmployeeCode.of("EMP001"))
                .designatedDeliveryDate(DesignatedDeliveryDate.of(now.plusDays(30)))
                .warehouseCode("001")
                .totalPurchaseAmount(Money.of(6000000)) // 600万円でルール違反
                .totalConsumptionTax(Money.of(600000))
                .remarks("金額超過テストデータ")
                .purchaseOrderLines(lines)
                .build();
    }

    private PurchaseOrder getPurchaseOrderWithDeliveryError(String purchaseOrderNumber) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime pastDeliveryDate = now.minusDays(1); // 納期が過去
        
        List<PurchaseOrderLine> lines = List.of(
                getPurchaseOrderLine(purchaseOrderNumber, 1)
        );

        // 納期エラーの発注をbuilderで作成（バリデーションを回避）
        return PurchaseOrder.builder()
                .purchaseOrderNumber(PurchaseOrderNumber.of(purchaseOrderNumber))
                .purchaseOrderDate(PurchaseOrderDate.of(now))
                .salesOrderNumber(OrderNumber.of("OD25010001"))
                .supplierCode(SupplierCode.of("001", 0))
                .purchaseManagerCode(EmployeeCode.of("EMP001"))
                .designatedDeliveryDate(DesignatedDeliveryDate.of(pastDeliveryDate)) // 過去の日付
                .warehouseCode("001")
                .totalPurchaseAmount(Money.of(100000))
                .totalConsumptionTax(Money.of(10000))
                .remarks("納期エラーテストデータ")
                .purchaseOrderLines(lines)
                .build();
    }

    private static PurchaseOrderLine getPurchaseOrderLineWithHighAmount(String purchaseOrderNumber, int lineNumber) {
        return PurchaseOrderLine.of(
                purchaseOrderNumber,
                lineNumber,
                lineNumber,
                "OD25010001",
                1,
                "99999001", // 商品コード
                "高額商品",
                6000000, // 600万円の単価
                1, // 数量1
                0,
                0
        );
    }

    public static PurchaseOrder getPurchaseOrder(String purchaseOrderNumber) {
        LocalDateTime now = LocalDateTime.now();
        
        List<PurchaseOrderLine> lines = List.of(
                getPurchaseOrderLine(purchaseOrderNumber, 1)
        );

        return PurchaseOrder.of(
                purchaseOrderNumber,
                now,
                "OD25010001",
                "001", // 仕入先コード
                0,
                "EMP001", // 社員コード
                now.plusDays(7),
                "001", // 倉庫コード
                10000,
                1000, // 消費税（10000円 × 10% = 1000円）
                "備考",
                lines
        );
    }

    public static PurchaseOrderLine getPurchaseOrderLine(String purchaseOrderNumber, int lineNumber) {
        return PurchaseOrderLine.of(
                purchaseOrderNumber,
                lineNumber,
                lineNumber,
                "OD25010001",
                1,
                "10101001", // 実際の商品コード
                "商品" + lineNumber,
                1000,
                10,
                0,
                0
        );
    }

    @Override
    public MultipartFile createPurchaseOrderFile() {
        InputStream is = getClass().getResourceAsStream("/csv/purchase/order/purchase_order_valid.csv");
        try {
            return new MockMultipartFile(
                    "purchase_order_valid.csv",
                    "purchase_order_valid.csv",
                    "text/csv",
                    is
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MultipartFile createPurchaseOrderInvalidFile() {
        InputStream is = getClass().getResourceAsStream("/csv/purchase/order/purchase_order_invalid.csv");
        try {
            return new MockMultipartFile(
                    "purchase_order_invalid.csv",
                    "purchase_order_invalid.csv",
                    "text/csv",
                    is
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MultipartFile createPurchaseOrderForCheckFile() {
        InputStream is = getClass().getResourceAsStream("/csv/purchase/order/purchase_order_valid_for_check.csv");
        try {
            return new MockMultipartFile(
                    "purchase_order_valid_for_check.csv",
                    "purchase_order_valid_for_check.csv",
                    "text/csv",
                    is
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MultipartFile createInventoryFile() {
        InputStream is = getClass().getResourceAsStream("/csv/inventory/valid_inventory.csv");
        try {
            return new MockMultipartFile(
                    "valid_inventory.csv",
                    "valid_inventory.csv",
                    "text/csv",
                    is
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MultipartFile createInventoryInvalidFile() {
        InputStream is = getClass().getResourceAsStream("/csv/inventory/invalid_inventory.csv");
        try {
            return new MockMultipartFile(
                    "invalid_inventory.csv",
                    "invalid_inventory.csv",
                    "text/csv",
                    is
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MultipartFile createInventoryForCheckFile() {
        InputStream is = getClass().getResourceAsStream("/csv/inventory/inventory_valid_for_check.csv");
        try {
            return new MockMultipartFile(
                    "inventory_valid_for_check.csv",
                    "inventory_valid_for_check.csv",
                    "text/csv",
                    is
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static Inventory getInventory(String warehouseCode, String productCode, String lotNumber) {
        return Inventory.of(
                warehouseCode,
                productCode,
                lotNumber,
                "1",  // 在庫区分
                "G",  // 良品区分
                100,  // 実在庫数
                95,   // 有効在庫数
                LocalDateTime.now().minusDays(1)  // 最終出荷日
        );
    }
}

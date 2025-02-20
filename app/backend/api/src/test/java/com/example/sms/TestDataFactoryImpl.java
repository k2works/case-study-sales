package com.example.sms;

import com.example.sms.domain.model.master.partner.customer.Customer;
import com.example.sms.domain.model.master.partner.customer.Shipping;
import com.example.sms.domain.model.master.partner.vendor.Vendor;
import com.example.sms.domain.model.master.product.MiscellaneousType;
import com.example.sms.domain.model.master.region.Region;
import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.department.DepartmentId;
import com.example.sms.domain.model.master.employee.Employee;
import com.example.sms.domain.model.master.partner.*;
import com.example.sms.domain.model.master.partner.Partner;
import com.example.sms.domain.model.master.product.*;
import com.example.sms.domain.model.sales_order.SalesOrder;
import com.example.sms.domain.model.sales_order.SalesOrderLine;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistory;
import com.example.sms.domain.model.system.user.User;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.model.system.audit.ApplicationExecutionProcessFlag;
import com.example.sms.domain.model.system.user.RoleName;
import com.example.sms.service.master.region.RegionRepository;
import com.example.sms.service.master.department.DepartmentRepository;
import com.example.sms.service.master.employee.EmployeeRepository;
import com.example.sms.service.master.partner.PartnerCategoryRepository;
import com.example.sms.service.master.partner.PartnerGroupRepository;
import com.example.sms.service.master.partner.PartnerRepository;
import com.example.sms.service.master.product.ProductCategoryRepository;
import com.example.sms.service.master.product.ProductRepository;
import com.example.sms.service.sales_order.SalesOrderRepository;
import com.example.sms.service.system.audit.AuditRepository;
import com.example.sms.service.system.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
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
        productRepository.save(Product.of("99999999", "商品1", "商品1", "ショウヒンイチ", ProductType.その他, 900, 810, 90, TaxType.その他, "カテゴリ9", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "009", 9));
        productRepository.save(Product.of("99999998", "商品2", "商品2", "ショウヒン二", ProductType.その他, 800, 720, 80, TaxType.その他, "カテゴリ8", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "008", 8));
        productRepository.save(Product.of("99999997", "商品3", "商品3", "ショウヒンサン", ProductType.その他, 700, 630, 70, TaxType.その他, "カテゴリ7", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "007", 7));

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
        SalesOrder order1 = getSalesOrder("S00001");
        IntStream.rangeClosed(1, 3).forEach(i -> {
            SalesOrderLine line = getSalesOrderLine(order1.getOrderNumber(), i);
            salesOrderRepository.save(SalesOrder.of(order1, List.of(line)));
        });
        SalesOrder order2 = getSalesOrder("S00002");
        IntStream.rangeClosed(1, 3).forEach(i -> {
            SalesOrderLine line = getSalesOrderLine(order2.getOrderNumber(), i);
            salesOrderRepository.save(SalesOrder.of(order2, List.of(line)));
        });
        SalesOrder order3 = getSalesOrder("S00003");
        IntStream.rangeClosed(1, 3).forEach(i -> {
            SalesOrderLine line = getSalesOrderLine(order3.getOrderNumber(), i);
            salesOrderRepository.save(SalesOrder.of(order3, List.of(line)));
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
    public void setUpForSalesOrderService() {
        salesOrderRepository.deleteAll();

        IntStream.rangeClosed(1, 3).forEach(i -> {
            String orderNumber = String.format("S%05d", i);
            SalesOrder order = getSalesOrder(orderNumber);
            List<SalesOrderLine> lines = IntStream.range(1, 4)
                    .mapToObj(lineNumber -> getSalesOrderLine(order.getOrderNumber(), lineNumber))
                    .toList();
            SalesOrder newOrder = SalesOrder.of(
                    order.getOrderNumber(),
                    order.getOrderDate(),
                    order.getDepartmentCode(),
                    order.getDepartmentStartDate(),
                    order.getCustomerCode(),
                    order.getCustomerBranchNumber(),
                    order.getEmployeeCode(),
                    order.getDesiredDeliveryDate(),
                    order.getCustomerOrderNumber(),
                    order.getWarehouseCode(),
                    order.getTotalOrderAmount(),
                    order.getTotalConsumptionTax(),
                    order.getRemarks(),
                    lines);
            salesOrderRepository.save(newOrder);
        });
    }

    @Override
    public void setUpForSalesOrderUploadService() {
        salesOrderRepository.deleteAll();
        setUpForDepartmentService();
        setUpForProductService();
        setUpForPartnerService();
        setUpForEmployeeService();
    }

    @Override
    public MultipartFile createSalesOrderFile() {
        InputStream is = getClass().getResourceAsStream("/csv/sales_order/sales_order_multiple.csv");
        try {
            return new MockMultipartFile(
                    "sales_order_multiple.csv",
                    "sales_order_multiple.csv",
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

    public static SalesOrder getSalesOrder(String orderNumber) {
        return SalesOrder.of(
                orderNumber,  // orderNumber（受注番号）
                LocalDateTime.of(2021, 1, 1, 0, 0),  // orderDate（受注日）
                "10000",  // departmentCode（部門コード）
                LocalDateTime.of(2021, 1, 1, 0, 0),  // departmentStartDate（部門開始日）
                "001",  // customerCode（顧客コード）
                1,  // customerBranchNumber（顧客枝番）
                "EMP001",  // employeeCode（社員コード）
                LocalDateTime.of(2021, 1, 1, 0, 0),  // desiredDeliveryDate（希望納期）
                "001",  // customerOrderNumber（客先注文番号）
                "001",  // warehouseCode（倉庫コード）
                1000,  // totalOrderAmount（受注金額合計）
                100,  // totalConsumptionTax（消費税合計）
                "備考",  // remarks（備考）
                List.of()
        );
    }

    public static SalesOrderLine getSalesOrderLine(String orderNumber, int lineNumber) {
        return SalesOrderLine.of(
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
                1,  // completionFlag（完了フラグ）
                10,  // discountAmount（値引金額）
                LocalDateTime.of(2021, 1, 1, 0, 0)  // deliveryDate（納期）
        );
    }

}
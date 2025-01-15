package com.example.sms;

import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.department.DepartmentId;
import com.example.sms.domain.model.master.employee.Employee;
import com.example.sms.domain.model.master.partner.*;
import com.example.sms.domain.model.master.product.*;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistory;
import com.example.sms.domain.model.system.user.User;
import com.example.sms.domain.type.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.type.audit.ApplicationExecutionProcessFlag;
import com.example.sms.domain.type.product.*;
import com.example.sms.domain.type.user.RoleName;
import com.example.sms.service.master.department.DepartmentRepository;
import com.example.sms.service.master.employee.EmployeeRepository;
import com.example.sms.service.master.partner.PartnerCategoryRepository;
import com.example.sms.service.master.partner.PartnerGroupRepository;
import com.example.sms.service.master.partner.PartnerRepository;
import com.example.sms.service.master.product.ProductCategoryRepository;
import com.example.sms.service.master.product.ProductRepository;
import com.example.sms.service.system.audit.AuditRepository;
import com.example.sms.service.system.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    PartnerGroupRepository partnerGroupRepository;
    @Autowired
    PartnerCategoryRepository partnerCategoryRepository;
    @Autowired
    PartnerRepository partnerRepository;

    @Override
    public void setUpForAuthApiService() {
        setUpUser();
    }

    @Override
    public User User() {
        return user();
    }

    @Override
    public Department Department() {
        return department("30000", LocalDateTime.of(2021, 1, 1, 0, 0), "部門3");
    }

    @Override
    public Employee Employee() {
        return employee("EMP999", "10000", LocalDateTime.of(2021, 1, 1, 0, 0));
    }

    @Override
    public Product Product() {
        return product("99999999", "商品9", "商品9", "しょうひん9", ProductType.その他, 900, 810, 90, TaxType.その他, "カテゴリ9", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "009", 9);
    }

    @Override
    public ProductCategory ProductCategory() {
        return getProductCategory("カテゴリ9", "カテゴリ9", 1, "2", 3);
    }

    @Override
    public void setUpForUserManagementService() {
        setUpUser();
    }

    @Override
    public void setUpForDepartmentService() {
        departmentRepository.deleteAll();
        departmentRepository.save(department("10000", LocalDateTime.of(2021, 1, 1, 0, 0), "部門1"));
        departmentRepository.save(department("20000", LocalDateTime.of(2021, 1, 1, 0, 0), "部門2"));
        employeeRepository.deleteAll();
        employeeRepository.save(employee("EMP001", "10000", LocalDateTime.of(2021, 1, 1, 0, 0)));
        employeeRepository.save(employee("EMP002", "10000", LocalDateTime.of(2021, 1, 1, 0, 0)));
    }

    @Override
    public void setUpForEmployeeService() {
        userRepository.save(user());
        LocalDateTime startDate = LocalDateTime.of(2021, 1, 1, 0, 0);
        departmentRepository.deleteAll();
        departmentRepository.save(department("10000", startDate, "部門1"));
        employeeRepository.deleteAll();
        employeeRepository.save(employee("EMP001", "10000", startDate));
        employeeRepository.save(employee("EMP002", "10000", startDate));
    }

    @Override
    public void setUpForProductService() {
        productRepository.deleteAll();

        String productCode = "99999001";
        SubstituteProduct substituteProduct = substituteProduct(productCode, "99999002", 1);
        Bom bom = bom(productCode, "X99", 1);
        CustomerSpecificSellingPrice customerSpecificSellingPrice = customerSpecificSellingPrice(productCode, "1", 1);
        Product product = product(productCode, "商品1", "商品1", "しょうひん1", ProductType.その他, 100, 90, 10, TaxType.外税, "カテゴリ1", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "001", 1);

        productRepository.save(Product.of(product, List.of(substituteProduct), List.of(bom), List.of(customerSpecificSellingPrice)));
        productRepository.save(product("99999002", "商品2", "商品2", "しょうひん2", ProductType.その他, 200, 180, 20, TaxType.内税, "カテゴリ1", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "002", 2));
        productRepository.save(product("99999003", "商品3", "商品3", "しょうひん3", ProductType.その他, 300, 270, 30, TaxType.非課税, "カテゴリ2", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "003", 3));

        productCategoryRepository.deleteAll();
        productCategoryRepository.save(getProductCategory("カテゴリ1", "カテゴリ1", 1, "カテゴリ1", 1));
        productCategoryRepository.save(getProductCategory("カテゴリ2", "カテゴリ2", 2, "カテゴリ1~カテゴリ2", 2));
    }

    private void setUpUser() {
        userRepository.deleteAll();
        userRepository.save(user());
        userRepository.save(admin());
    }

    @Override
    public void setUpForAuditService() {
        User execUser = User.of("U777777", user().getPassword().Value(), user().getName().FirstName(), user().getName().LastName(), user().getRoleName());
        userRepository.save(execUser);

        auditRepository.deleteAll();
        ApplicationExecutionHistory applicationExecutionHistory = ApplicationExecutionHistory.of(null, "その他", "9999", ApplicationExecutionHistoryType.同期, LocalDateTime.of(2024,1,1,1,0), LocalDateTime.of(2024,1,1,2,0), ApplicationExecutionProcessFlag.未実行,  "processDetails", execUser);
        auditRepository.save(applicationExecutionHistory);
        auditRepository.save(applicationExecutionHistory);
    }

    @Override
    public void setUpForDownloadService() {
        departmentRepository.deleteAll();
        departmentRepository.save(department("30000", LocalDateTime.of(2021, 1, 1, 0, 0), "部門3"));
        departmentRepository.save(department("40000", LocalDateTime.of(2021, 1, 1, 0, 0), "部門4"));
        departmentRepository.save(department("50000", LocalDateTime.of(2021, 1, 1, 0, 0), "部門5"));
        employeeRepository.deleteAll();
        userRepository.save(user());
        employeeRepository.save(employee("EMP003", "30000", LocalDateTime.of(2021, 1, 1, 0, 0)));
        employeeRepository.save(employee("EMP004", "30000", LocalDateTime.of(2021, 1, 1, 0, 0)));
        employeeRepository.save(employee("EMP005", "40000", LocalDateTime.of(2021, 1, 1, 0, 0)));
        productCategoryRepository.deleteAll();
        productCategoryRepository.save(getProductCategory("00000001", "カテゴリ3", 1, "2", 3));
        productCategoryRepository.save(getProductCategory("00000002", "カテゴリ4", 1, "2", 3));
        productCategoryRepository.save(getProductCategory("00000003", "カテゴリ5", 1, "2", 3));
        productRepository.deleteAll();
        productRepository.save(product("99999999", "商品1", "商品1", "ショウヒンイチ", ProductType.その他, 900, 810, 90, TaxType.その他, "カテゴリ9", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "009", 9));
        productRepository.save(product("99999998", "商品2", "商品2", "ショウヒン二", ProductType.その他, 800, 720, 80, TaxType.その他, "カテゴリ8", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "008", 8));
        productRepository.save(product("99999997", "商品3", "商品3", "ショウヒンサン", ProductType.その他, 700, 630, 70, TaxType.その他, "カテゴリ7", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "007", 7));
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

    private static User user() {
        return User.of("U999999", "$2a$10$oxSJl.keBwxmsMLkcT9lPeAIxfNTPNQxpeywMrF7A3kVszwUTqfTK", "first", "last", RoleName.USER);
    }

    private static User admin() {
        return User.of("U888888", "$2a$10$oxSJl.keBwxmsMLkcT9lPeAIxfNTPNQxpeywMrF7A3kVszwUTqfTK", "first", "last", RoleName.USER);
    }

    public static Department department(String departmentId, LocalDateTime startDate, String departmentName) {
        return Department.of(DepartmentId.of(departmentId, startDate), LocalDateTime.of(9999, 12, 31, 0, 0), departmentName, 0, departmentId + "~", 0, 1);
    }

    public static Employee employee(String empCode, String departmentCode, LocalDateTime startDate) {
        return Employee.of(
                Employee.of(empCode, "firstName lastName", "firstNameKana lastNameKana", "090-1234-5678", "03-1234-5678", ""),
                department(departmentCode, startDate, "部門1"),
                user()
        );
    }

    public static Product product(String productCode, String productFormalName, String productAbbreviation, String productNameKana, ProductType productType, Integer sellingPrice, Integer purchasePrice, Integer costOfSales, TaxType taxType, String productClassificationCode, MiscellaneousType miscellaneousType, StockManagementTargetType stockManagementTargetType, StockAllocationType stockAllocationType, String vendorCode, Integer supplierBranchNumber) {
        return Product.of(productCode, productFormalName, productAbbreviation, productNameKana, productType, sellingPrice, purchasePrice, costOfSales, taxType, productClassificationCode, miscellaneousType, stockManagementTargetType, stockAllocationType, vendorCode, supplierBranchNumber);
    }

    public static SubstituteProduct substituteProduct(String productCode, String substituteProductCode, Integer priority) {
        return SubstituteProduct.of(productCode, substituteProductCode, priority);
    }

    public static Bom bom(String productCode, String componentCode, int i) {
        return Bom.of(productCode, componentCode, i);
    }

    public static CustomerSpecificSellingPrice customerSpecificSellingPrice(String productCode, String format, int i) {
        return CustomerSpecificSellingPrice.of(productCode, format, i);
    }

    public static ProductCategory getProductCategory(String productCategoryCode, String productCategoryName, int productCategoryHierarchy, String productCategoryPath, int lowestLevelDivision) {
        return ProductCategory.of(productCategoryCode, productCategoryName, productCategoryHierarchy, productCategoryPath, lowestLevelDivision);
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
}
package com.example.sms;

import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.department.DepartmentId;
import com.example.sms.domain.model.master.employee.Employee;
import com.example.sms.domain.model.master.product.*;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistory;
import com.example.sms.domain.model.system.user.User;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.model.system.audit.ApplicationExecutionProcessFlag;
import com.example.sms.domain.model.system.user.RoleName;
import com.example.sms.service.master.department.DepartmentRepository;
import com.example.sms.service.master.employee.EmployeeRepository;
import com.example.sms.service.master.product.ProductCategoryRepository;
import com.example.sms.service.master.product.ProductRepository;
import com.example.sms.service.system.audit.AuditRepository;
import com.example.sms.service.system.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

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
        Product product = Product.of(productCode, "商品1", "商品1", "しょうひん1", ProductType.その他, 100, 90, 10, TaxType.外税, "カテゴリ1", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "サプライヤ1", 1);

        productRepository.save(Product.of(product, List.of(substituteProduct), List.of(bom), List.of(customerSpecificSellingPrice)));
        productRepository.save(Product.of("99999002", "商品2", "商品2", "しょうひん2", ProductType.その他, 200, 180, 20, TaxType.内税, "カテゴリ1", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "サプライヤ2", 2));
        productRepository.save(Product.of("99999003", "商品3", "商品3", "しょうひん3", ProductType.その他, 300, 270, 30, TaxType.非課税, "カテゴリ2", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "サプライヤ3", 3));

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
        productRepository.save(Product.of("99999999", "商品1", "商品1", "ショウヒンイチ", ProductType.その他, 900, 810, 90, TaxType.その他, "カテゴリ9", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "サプライヤ9", 9));
        productRepository.save(Product.of("99999998", "商品2", "商品2", "ショウヒン二", ProductType.その他, 800, 720, 80, TaxType.その他, "カテゴリ8", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "サプライヤ8", 8));
        productRepository.save(Product.of("99999997", "商品3", "商品3", "ショウヒンサン", ProductType.その他, 700, 630, 70, TaxType.その他, "カテゴリ7", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "サプライヤ7", 7));
    }

    private void setUpUser() {
        userRepository.deleteAll();
        userRepository.save(getUser());
        userRepository.save(getAdmin());
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
        return Product.of(productCode, "商品正式名", "商品略称", "商品名カナ", ProductType.その他, 1000, 2000, 3000, TaxType.外税, "99999999", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "00000000", 5);
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

}

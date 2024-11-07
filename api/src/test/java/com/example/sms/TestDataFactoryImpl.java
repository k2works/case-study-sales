package com.example.sms;

import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.department.DepartmentId;
import com.example.sms.domain.model.master.employee.Employee;
import com.example.sms.domain.model.master.product.*;
import com.example.sms.domain.model.system.user.RoleName;
import com.example.sms.domain.model.system.user.User;
import com.example.sms.service.master.department.DepartmentRepository;
import com.example.sms.service.master.employee.EmployeeRepository;
import com.example.sms.service.master.product.ProductCategoryRepository;
import com.example.sms.service.master.product.ProductRepository;
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

    @Override
    public void setUpForAuthApiService() {
        userRepository.deleteAll();
        userRepository.save(user());
        userRepository.save(admin());
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
        return product("99999999", "商品9", "商品9", "しょうひん9", "9", 900, 810, 90, 9, "カテゴリ9", 9, 9, 9, "サプライヤ9", 9);
    }

    @Override
    public ProductCategory ProductCategory() {
        return getProductCategory("カテゴリ9", "カテゴリ9", 1, "2", 3);
    }

    @Override
    public void setUpForUserManagementService() {
        userRepository.deleteAll();
        userRepository.save(user());
        userRepository.save(admin());
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
        SubstituteProduct substituteProduct = substituteProduct(productCode, "000001", 1);
        Bom bom = bom(productCode, "000001", 1);
        CustomerSpecificSellingPrice customerSpecificSellingPrice = customerSpecificSellingPrice(productCode, "1", 1);
        Product product = product(productCode, "商品1", "商品1", "しょうひん1", "1", 100, 90, 10, 1, "カテゴリ1", 1, 1, 1, "サプライヤ1", 1);

        productRepository.save(Product.of(product, List.of(substituteProduct), List.of(bom), List.of(customerSpecificSellingPrice)));
        productRepository.save(product("99999002", "商品2", "商品2", "しょうひん2", "2", 200, 180, 20, 2, "カテゴリ1", 2, 2, 2, "サプライヤ2", 2));
        productRepository.save(product("99999003", "商品3", "商品3", "しょうひん3", "3", 300, 270, 30, 3, "カテゴリ2", 3, 3, 3, "サプライヤ3", 3));

        productCategoryRepository.deleteAll();
        productCategoryRepository.save(getProductCategory("カテゴリ1", "カテゴリ1", 1, "カテゴリ1", 1));
        productCategoryRepository.save(getProductCategory("カテゴリ2", "カテゴリ2", 2, "カテゴリ2", 2));
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

    public static Product product(String productCode, String productFormalName, String productAbbreviation, String productNameKana, String productCategory, Integer sellingPrice, Integer purchasePrice, Integer costOfSales, Integer taxCategory, String productClassificationCode, Integer miscellaneousCategory, Integer stockManagementTargetCategory, Integer stockAllocationCategory, String supplierCode, Integer supplierBranchNumber) {
        return Product.of(productCode, productFormalName, productAbbreviation, productNameKana, productCategory, sellingPrice, purchasePrice, costOfSales, taxCategory, productClassificationCode, miscellaneousCategory, stockManagementTargetCategory, stockAllocationCategory, supplierCode, supplierBranchNumber);
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

}

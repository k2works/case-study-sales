package com.example.sms.service.system.download;

import com.example.sms.domain.model.master.department.DepartmentList;
import com.example.sms.domain.model.master.employee.EmployeeList;
import com.example.sms.domain.model.master.product.ProductCategoryList;
import com.example.sms.domain.model.master.product.ProductList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.system.download.DepartmentDownloadCSV;
import com.example.sms.infrastructure.datasource.system.download.EmployeeDownloadCSV;
import com.example.sms.infrastructure.datasource.system.download.ProductCategoryDownloadCSV;
import com.example.sms.infrastructure.datasource.system.download.ProductDownloadCSV;
import org.springframework.stereotype.Service;

import java.io.OutputStreamWriter;
import java.util.List;

import static com.example.sms.infrastructure.Pattern2WriteCSVUtil.writeCsv;
import static com.example.sms.service.system.auth.AuthApiService.checkPermission;

/**
 * データダウンロードサービス
 */
@Service
public class DownloadService {
    private final DepartmentCSVRepository departmentCSVRepository;
    private final EmployeeCSVRepository employeeCSVRepository;
    private final ProductCategoryCSVRepository productCategoryCSVRepository;
    private final ProductCSVRepository productCSVRepository;

    public DownloadService(DepartmentCSVRepository departmentCSVRepository, EmployeeCSVRepository employeeCSVRepository, ProductCategoryCSVRepository productCategoryCSVRepository, ProductCSVRepository productCSVRepository) {
        this.departmentCSVRepository = departmentCSVRepository;
        this.employeeCSVRepository = employeeCSVRepository;
        this.productCategoryCSVRepository = productCategoryCSVRepository;
        this.productCSVRepository = productCSVRepository;
    }

    /**
     * ダウンロード件数取得
     */
    public int count(DownloadCriteria condition) {
        return switch (condition.getTarget()) {
            case 部門 -> {
                checkPermission("ROLE_ADMIN");
                yield countDepartments(condition);
            }
            case 社員 -> {
                checkPermission("ROLE_ADMIN");
                yield countEmployees(condition);
            }
            case 商品分類 -> {
                checkPermission("ROLE_ADMIN");
                yield countProductCategories(condition);
            }
            case 商品 ->  {
                checkPermission("ROLE_ADMIN");
                yield countProducts(condition);
            }
        };
    }

    /**
     * ダウンロード
     */
    public void download(OutputStreamWriter streamWriter, DownloadCriteria condition) throws Exception {
        switch (condition.getTarget()) {
            case 部門 -> writeCsv(DepartmentDownloadCSV.class).accept(streamWriter, convert(condition));
            case 社員 -> writeCsv(EmployeeDownloadCSV.class).accept(streamWriter, convert(condition));
            case 商品分類 -> writeCsv(ProductCategoryDownloadCSV.class).accept(streamWriter, convert(condition));
            case 商品 -> writeCsv(ProductDownloadCSV.class).accept(streamWriter, convert(condition));
        }
    }

    /**
     * CSV変換
     */
    public <T> List<T> convert(DownloadCriteria condition) {
        return switch (condition.getTarget()) {
            case 部門 -> (List<T>) convertDepartments(condition);
            case 社員 -> (List<T>) convertEmployees(condition);
            case 商品分類 -> (List<T>) convertProductCategories(condition);
            case 商品 -> (List<T>) convertProducts(condition);
        };
    }

    /**
     * 部門ダウンロード件数取得
     */
    private int countDepartments(DownloadCriteria condition) {
        return departmentCSVRepository.countBy(condition);
    }

    /**
     * 社員ダウンロード件数取得
     */
    private int countEmployees(DownloadCriteria condition) {
        return employeeCSVRepository.countBy(condition);
    }

    /**
     * 商品カテゴリダウンロード件数取得
     */
    private int countProductCategories(DownloadCriteria condition) {
        return productCategoryCSVRepository.countBy(condition);
    }

    /**
     * 商品ダウンロード件数取得
     */
    private int countProducts(DownloadCriteria condition) {
        return productCSVRepository.countBy(condition);
    }

    /**
     * 部門CSV変換
     */
    private List<DepartmentDownloadCSV> convertDepartments(DownloadCriteria condition) {
        DepartmentList departmentList = departmentCSVRepository.selectBy(condition);
        return departmentCSVRepository.convert(departmentList);
    }

    /**
     * 社員CSV変換
     */
    private List<EmployeeDownloadCSV> convertEmployees(DownloadCriteria condition) {
        EmployeeList employeeList = employeeCSVRepository.selectBy(condition);
        return employeeCSVRepository.convert(employeeList);
    }

    /**
     * 商品分類CSV変換
     */
    private List<ProductCategoryDownloadCSV> convertProductCategories(DownloadCriteria condition) {
        ProductCategoryList productCategoryList = productCategoryCSVRepository.selectBy(condition);
        return productCategoryCSVRepository.convert(productCategoryList);
    }

    /**
     * 商品CSV変換
     */
    private List<ProductDownloadCSV> convertProducts(DownloadCriteria condition) {
        ProductList productList = productCSVRepository.selectBy(condition);
        return productCSVRepository.convert(productList);
    }
}

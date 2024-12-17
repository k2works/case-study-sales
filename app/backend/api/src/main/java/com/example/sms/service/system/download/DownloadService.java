package com.example.sms.service.system.download;

import com.example.sms.domain.model.master.department.DepartmentList;
import com.example.sms.domain.model.master.employee.EmployeeList;
import com.example.sms.domain.model.master.product.ProductCategoryList;
import com.example.sms.domain.model.master.product.ProductList;
import com.example.sms.domain.model.system.download.DownloadCondition;
import com.example.sms.infrastructure.datasource.master.department.DepartmentDownloadCSV;
import com.example.sms.infrastructure.datasource.master.employee.EmployeeDownloadCSV;
import com.example.sms.infrastructure.datasource.master.product.ProductCategoryDownloadCSV;
import com.example.sms.infrastructure.datasource.master.product.ProductDownloadCSV;
import org.springframework.stereotype.Service;

import java.io.OutputStreamWriter;
import java.util.List;

import static com.example.sms.infrastructure.Pattern2WriteCSVUtil.writeCsv;

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
    public int count(DownloadCondition condition) {
        return switch (condition.getTarget()) {
            case DEPARTMENT -> countDepartments(condition);
            case EMPLOYEE -> countEmployees(condition);
            case PRODUCT_CATEGORY -> countProductCategories(condition);
            case PRODUCT -> countProducts(condition);
        };
    }

    /**
     * ダウンロード変換
     */
    public <T> List<T> convert(DownloadCondition condition) {
        return switch (condition.getTarget()) {
            case DEPARTMENT -> (List<T>) downloadDepartments(condition);
            case EMPLOYEE -> (List<T>) downloadEmployees(condition);
            case PRODUCT_CATEGORY -> (List<T>) downloadProductCategories(condition);
            case PRODUCT -> (List<T>) downloadProducts(condition);
        };
    }

    /**
     * ダウンロード
     */
    public void download(OutputStreamWriter streamWriter, DownloadCondition condition) throws Exception {
        switch (condition.getTarget()) {
            case DEPARTMENT -> writeCsv(DepartmentDownloadCSV.class).accept(streamWriter, convert(condition));
            case EMPLOYEE -> writeCsv(EmployeeDownloadCSV.class).accept(streamWriter, convert(condition));
            case PRODUCT_CATEGORY -> writeCsv(ProductCategoryDownloadCSV.class).accept(streamWriter, convert(condition));
            case PRODUCT -> writeCsv(ProductDownloadCSV.class).accept(streamWriter, convert(condition));
        };
    }

    /**
     * 部門ダウンロード件数取得
     */
    private int countDepartments(DownloadCondition condition) {
        return departmentCSVRepository.countBy(condition);
    }

    /**
     * 社員ダウンロード件数取得
     */
    private int countEmployees(DownloadCondition condition) {
        return employeeCSVRepository.countBy(condition);
    }

    /**
     * 商品カテゴリダウンロード件数取得
     */
    private int countProductCategories(DownloadCondition condition) {
        return productCategoryCSVRepository.countBy(condition);
    }

    /**
     * 商品ダウンロード件数取得
     */
    private int countProducts(DownloadCondition condition) {
        return productCSVRepository.countBy(condition);
    }

    /**
     * 部門ダウンロード
     */
    private List<DepartmentDownloadCSV> downloadDepartments(DownloadCondition condition) {
        DepartmentList departmentList = departmentCSVRepository.selectBy(condition);
        return departmentCSVRepository.convert(departmentList);
    }

    /**
     * 社員ダウンロード
     */
    private List<EmployeeDownloadCSV> downloadEmployees(DownloadCondition condition) {
        EmployeeList employeeList = employeeCSVRepository.selectBy(condition);
        return employeeCSVRepository.convert(employeeList);
    }

    /**
     * 商品カテゴリダウンロード
     */
    private List<ProductCategoryDownloadCSV> downloadProductCategories(DownloadCondition condition) {
        ProductCategoryList productCategoryList = productCategoryCSVRepository.selectBy(condition);
        return productCategoryCSVRepository.convert(productCategoryList);
    }

    /**
     * 商品ダウンロード
     */
    private List<ProductDownloadCSV> downloadProducts(DownloadCondition condition) {
        ProductList productList = productCSVRepository.selectBy(condition);
        return productCSVRepository.convert(productList);
    }
}

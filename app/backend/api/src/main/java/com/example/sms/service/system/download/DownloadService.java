package com.example.sms.service.system.download;

import com.example.sms.domain.model.master.department.DepartmentList;
import com.example.sms.domain.model.master.employee.EmployeeList;
import com.example.sms.domain.model.master.partner.PartnerGroupList;
import com.example.sms.domain.model.master.partner.PartnerList;
import com.example.sms.domain.model.master.partner.customer.CustomerList;
import com.example.sms.domain.model.master.partner.vendor.VendorList;
import com.example.sms.domain.model.master.product.ProductCategoryList;
import com.example.sms.domain.model.master.product.ProductList;
import com.example.sms.domain.model.sales.order.OrderList;
import com.example.sms.domain.model.sales.sales.SalesList;
import com.example.sms.domain.model.sales.shipping.ShippingList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.system.download.*;
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
    private final PartnerGroupCSVRepository partnerGroupCSVRepository;
    private final PartnerCSVRepository partnerCSVRepository;
    private final CustomerCSVRepository customerCSVRepository;
    private final VendorCSVRepository vendorCSVRepository;
    private final OrderCSVRepository orderCSVRepository;
    private final ShippingCSVRepository shippingCSVRepository;
    private final SalesCSVRepository salesCSVRepository;

    public DownloadService(DepartmentCSVRepository departmentCSVRepository, EmployeeCSVRepository employeeCSVRepository, ProductCategoryCSVRepository productCategoryCSVRepository, ProductCSVRepository productCSVRepository, PartnerGroupCSVRepository partnerGroupCSVRepository, PartnerCSVRepository partnerCSVRepository, CustomerCSVRepository customerCSVRepository, VendorCSVRepository vendorCSVRepository, OrderCSVRepository orderCSVRepository, ShippingCSVRepository shippingCSVRepository, SalesCSVRepository salesCSVRepository) {
        this.departmentCSVRepository = departmentCSVRepository;
        this.employeeCSVRepository = employeeCSVRepository;
        this.productCategoryCSVRepository = productCategoryCSVRepository;
        this.productCSVRepository = productCSVRepository;
        this.partnerGroupCSVRepository = partnerGroupCSVRepository;
        this.partnerCSVRepository = partnerCSVRepository;
        this.customerCSVRepository = customerCSVRepository;
        this.vendorCSVRepository = vendorCSVRepository;
        this.orderCSVRepository = orderCSVRepository;
        this.shippingCSVRepository = shippingCSVRepository;
        this.salesCSVRepository = salesCSVRepository;
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
            case 取引先グループ -> {
                checkPermission("ROLE_ADMIN");
                yield countPartnerGroups(condition);
            }
            case 取引先 -> {
                checkPermission("ROLE_ADMIN");
                yield countPartner(condition);
            }
            case 顧客 -> {
                checkPermission("ROLE_ADMIN");
                yield countCustomer(condition);
            }
            case 仕入先 -> {
                checkPermission("ROLE_ADMIN");
                yield countVendor(condition);
            }
            case 受注 -> {
                yield countOrder(condition);
            }
            case 出荷 -> {
                yield countShipping(condition);
            }
            case 売上 -> {
                yield countSales(condition);
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
            case 取引先グループ -> writeCsv(PartnerGroupDownloadCSV.class).accept(streamWriter, convert(condition));
            case 取引先 -> writeCsv(PartnerDownloadCSV.class).accept(streamWriter, convert(condition));
            case 顧客 -> writeCsv(CustomerDownloadCSV.class).accept(streamWriter, convert(condition));
            case 仕入先 -> writeCsv(VendorDownloadCSV.class).accept(streamWriter, convert(condition));
            case 受注 -> writeCsv(OrderDownloadCSV.class).accept(streamWriter, convert(condition));
            case 出荷 -> writeCsv(ShippingDownloadCSV.class).accept(streamWriter, convert(condition));
            case 売上 -> writeCsv(SalesDownloadCSV.class).accept(streamWriter, convert(condition));
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
            case 取引先グループ -> (List<T>) convertPartnerGroup(condition);
            case 取引先 -> (List<T>) convertPartner(condition);
            case 顧客 -> (List<T>) convertCustomer(condition);
            case 仕入先 -> (List<T>) convertVendor(condition);
            case 受注 -> (List<T>) convertOrder(condition);
            case 出荷 -> (List<T>) convertShipping(condition);
            case 売上 -> (List<T>) convertSales(condition);
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
     * 取引先グループダウンロード件数取得
     */
    private int countPartnerGroups(DownloadCriteria condition) {
        return partnerGroupCSVRepository.countBy(condition);
    }

    /**
     * 取引先ダウンロード件数取得
     */
    private int countPartner(DownloadCriteria condition) {
        return partnerCSVRepository.countBy(condition);
    }

    /**
     * 顧客ダウンロード件数取得
     */
    private int countCustomer(DownloadCriteria condition) {
        return customerCSVRepository.countBy(condition);
    }

    /**
     * 仕入先ダウンロード件数取得
     */
    private int countVendor(DownloadCriteria condition) {
        return vendorCSVRepository.countBy(condition);
    }
    /**
     * 受注ダウンロード件数取得
     */
    private int countOrder(DownloadCriteria condition) {
        return orderCSVRepository.countBy(condition);
    }
    /**
     * 出荷ダウンロード件数取得
     */
    private int countShipping(DownloadCriteria condition) {
        return shippingCSVRepository.countBy(condition);
    }

    /**
     * 売上ダウンロード件数取得
     */
    private int countSales(DownloadCriteria condition) {
        return salesCSVRepository.countBy(condition);
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

    /**
     * 取引先グループCSV変換
     */
    private List<PartnerGroupDownloadCSV> convertPartnerGroup(DownloadCriteria condition) {
        PartnerGroupList partnerGroupList = partnerGroupCSVRepository.selectBy(condition);
        return partnerGroupCSVRepository.convert(partnerGroupList);
    }

    /**
     * 取引先CSV変換
     */
    private List<PartnerDownloadCSV> convertPartner(DownloadCriteria condition) {
        PartnerList partnerList = partnerCSVRepository.selectBy(condition);
        return partnerCSVRepository.convert(partnerList);
    }

    /**
     * 顧客CSV変換
     */
    private List<CustomerDownloadCSV> convertCustomer(DownloadCriteria condition) {
        CustomerList customerList = customerCSVRepository.selectBy(condition);
        return customerCSVRepository.convert(customerList);
    }

    /**
     * 仕入先CSV変換
     */
    private List<VendorDownloadCSV> convertVendor(DownloadCriteria condition) {
        VendorList vendorList = vendorCSVRepository.selectBy(condition);
        return vendorCSVRepository.convert(vendorList);
    }

    /**
     * 受注CSV変換
     */
    private List<OrderDownloadCSV> convertOrder(DownloadCriteria condition) {
        OrderList orderList = orderCSVRepository.selectBy(condition);
        return orderCSVRepository.convert(orderList);
    }
    /**
     * 出荷CSV変換
     */
    private List<ShippingDownloadCSV> convertShipping(DownloadCriteria condition) {
        ShippingList shippingList = shippingCSVRepository.selectBy(condition);
        return shippingCSVRepository.convert(shippingList);
    }
    /**
     * 売上CSV変換
     */
    private List<SalesDownloadCSV> convertSales(DownloadCriteria condition) {
        SalesList salesList = salesCSVRepository.selectBy(condition);
        return salesCSVRepository.convert(salesList);
    }
}

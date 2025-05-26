package com.example.sms;

import org.springframework.web.multipart.MultipartFile;

public interface TestDataFactory {
    void setUpForAuthApiService();

    void setUpForUserManagementService();

    void setUpForDepartmentService();

    void setUpForEmployeeService();

    void setUpForProductService();

    void setUpForAuditService();

    void setUpForDownloadService();

    void setUpForRegionService();

    void setUpForPartnerGroupService();

    void setUpForPartnerCategoryService();

    void setUpForPartnerService();

    void setUpForCustomerService();

    void setUpForVendorService();

    void setUpForOrderService();

    void setUpForOrderUploadService();

    void setUpForOrderRuleCheckService();

    void setUpForShippingService();

    void setUpForShippingRuleCheckService();

    void setUpForSalesService();

    void setUpForSalesServiceForAggregate();

    void setUpForInvoiceService();

    MultipartFile createOrderFile();

    MultipartFile createOrderInvalidFile();

    MultipartFile createOrderCheckRuleFile();
}

package com.example.sms.presentation.api.system.download;

import com.example.sms.domain.model.system.download.DownloadTarget;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ダウンロード条件リソース")
class DownloadCriteriaResourceTest {

    @Test
    @DisplayName("ダウンロード条件リソースを作成できる")
    void shouldCreateDownloadCriteriaResource() {
        DownloadCriteriaResource resource = new DownloadCriteriaResource();
        resource.setTarget(DownloadTarget.部門);
        resource.setFileName("test_file.csv");
        
        assertAll(
                () -> assertEquals(DownloadTarget.部門, resource.getTarget()),
                () -> assertEquals("test_file.csv", resource.getFileName())
        );
    }

    @ParameterizedTest
    @EnumSource(DownloadTarget.class)
    @DisplayName("すべてのダウンロード対象でダウンロード条件を作成できる")
    void shouldCreateDownloadCriteriaForAllTargets(DownloadTarget target) {
        DownloadCriteria criteria = DownloadCriteriaResource.of(target);
        
        assertNotNull(criteria);
    }

    @Test
    @DisplayName("部門ダウンロード条件を作成できる")
    void shouldCreateDepartmentDownloadCriteria() {
        DownloadCriteria criteria = DownloadCriteriaResource.of(DownloadTarget.部門);
        assertNotNull(criteria);
    }

    @Test
    @DisplayName("社員ダウンロード条件を作成できる")
    void shouldCreateEmployeeDownloadCriteria() {
        DownloadCriteria criteria = DownloadCriteriaResource.of(DownloadTarget.社員);
        assertNotNull(criteria);
    }

    @Test
    @DisplayName("商品分類ダウンロード条件を作成できる")
    void shouldCreateProductCategoryDownloadCriteria() {
        DownloadCriteria criteria = DownloadCriteriaResource.of(DownloadTarget.商品分類);
        assertNotNull(criteria);
    }

    @Test
    @DisplayName("商品ダウンロード条件を作成できる")
    void shouldCreateProductDownloadCriteria() {
        DownloadCriteria criteria = DownloadCriteriaResource.of(DownloadTarget.商品);
        assertNotNull(criteria);
    }

    @Test
    @DisplayName("取引先グループダウンロード条件を作成できる")
    void shouldCreatePartnerGroupDownloadCriteria() {
        DownloadCriteria criteria = DownloadCriteriaResource.of(DownloadTarget.取引先グループ);
        assertNotNull(criteria);
    }

    @Test
    @DisplayName("取引先ダウンロード条件を作成できる")
    void shouldCreatePartnerDownloadCriteria() {
        DownloadCriteria criteria = DownloadCriteriaResource.of(DownloadTarget.取引先);
        assertNotNull(criteria);
    }

    @Test
    @DisplayName("顧客ダウンロード条件を作成できる")
    void shouldCreateCustomerDownloadCriteria() {
        DownloadCriteria criteria = DownloadCriteriaResource.of(DownloadTarget.顧客);
        assertNotNull(criteria);
    }

    @Test
    @DisplayName("仕入先ダウンロード条件を作成できる")
    void shouldCreateVendorDownloadCriteria() {
        DownloadCriteria criteria = DownloadCriteriaResource.of(DownloadTarget.仕入先);
        assertNotNull(criteria);
    }

    @Test
    @DisplayName("受注ダウンロード条件を作成できる")
    void shouldCreateOrderDownloadCriteria() {
        DownloadCriteria criteria = DownloadCriteriaResource.of(DownloadTarget.受注);
        assertNotNull(criteria);
    }

    @Test
    @DisplayName("出荷ダウンロード条件を作成できる")
    void shouldCreateShipmentDownloadCriteria() {
        DownloadCriteria criteria = DownloadCriteriaResource.of(DownloadTarget.出荷);
        assertNotNull(criteria);
    }

    @Test
    @DisplayName("売上ダウンロード条件を作成できる")
    void shouldCreateSalesDownloadCriteria() {
        DownloadCriteria criteria = DownloadCriteriaResource.of(DownloadTarget.売上);
        assertNotNull(criteria);
    }

    @Test
    @DisplayName("発注ダウンロード条件を作成できる")
    void shouldCreatePurchaseOrderDownloadCriteria() {
        DownloadCriteria criteria = DownloadCriteriaResource.of(DownloadTarget.発注);
        assertNotNull(criteria);
    }

    @Test
    @DisplayName("請求ダウンロード条件を作成できる")
    void shouldCreateInvoiceDownloadCriteria() {
        DownloadCriteria criteria = DownloadCriteriaResource.of(DownloadTarget.請求);
        assertNotNull(criteria);
    }

    @Test
    @DisplayName("入金ダウンロード条件を作成できる")
    void shouldCreatePaymentDownloadCriteria() {
        DownloadCriteria criteria = DownloadCriteriaResource.of(DownloadTarget.入金);
        assertNotNull(criteria);
    }

    @Test
    @DisplayName("口座ダウンロード条件を作成できる")
    void shouldCreatePaymentAccountDownloadCriteria() {
        DownloadCriteria criteria = DownloadCriteriaResource.of(DownloadTarget.口座);
        assertNotNull(criteria);
    }
}
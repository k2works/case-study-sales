package com.example.sms.service.shipping;

import com.example.sms.IntegrationTest;
import com.example.sms.TestDataFactory;
import com.example.sms.domain.model.shipping.Shipping;
import com.example.sms.domain.model.shipping.ShippingList;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
@DisplayName("出荷サービス")
class ShippingServiceTest {

    @Autowired
    private ShippingService shippingService;

    @Autowired
    private TestDataFactory testDataFactory;

    @Nested
    @DisplayName("出荷")
    class ShippingTest {
        
        @BeforeEach
        void setUp() {
            testDataFactory.setUpForSalesOrderService();
        }

        @Test
        @DisplayName("出荷一覧を取得できる")
        void shouldRetrieveAllShippings() {
            ShippingList result = shippingService.selectAll();
            assertNotNull(result);
            assertFalse(result.asList().isEmpty());
            assertNotNull(result.asList().getFirst().getDepartment());
            assertNotNull(result.asList().getFirst().getCustomer());
            assertNotNull(result.asList().getFirst().getEmployee());
            assertNotNull(result.asList().getFirst().getProduct());
        }

        @Test
        @DisplayName("注文番号で検索できる")
        void shouldFindShippingById() {
            ShippingList allShippings = shippingService.selectAll();
            if (!allShippings.asList().isEmpty()) {
                Shipping shipping = allShippings.asList().getFirst();
                
                Shipping foundShipping = shippingService.findById(shipping.getOrderNumber().getValue()).orElse(null);
                
                assertNotNull(foundShipping);
                assertEquals(shipping.getOrderNumber().getValue(), foundShipping.getOrderNumber().getValue());
                assertEquals(shipping.getProductCode().getValue(), foundShipping.getProductCode().getValue());
                assertEquals(shipping.getProductName(), foundShipping.getProductName());
            }
        }

        @Test
        @DisplayName("出荷一覧をページング付きで取得できる")
        void shouldRetrieveAllShippingsWithPageInfo() {
            PageInfo<Shipping> result = shippingService.selectAllWithPageInfo();
            
            assertNotNull(result);
            assertNotNull(result.getList());
            assertFalse(result.getList().isEmpty());
        }

        @Test
        @DisplayName("条件付きで出荷を検索できる (ページング)")
        void shouldSearchShippingsWithPageInfo() {
            ShippingList allShippings = shippingService.selectAll();
            if (!allShippings.asList().isEmpty()) {
                Shipping shipping = allShippings.asList().getFirst();
                
                ShippingCriteria criteria = ShippingCriteria.builder()
                        .productCode(shipping.getProductCode().getValue())
                        .build();
                
                PageInfo<Shipping> result = shippingService.searchWithPageInfo(criteria);
                
                assertNotNull(result);
                assertNotNull(result.getList());
                assertFalse(result.getList().isEmpty());
                assertTrue(result.getList().stream().anyMatch(s -> 
                        s.getProductCode().getValue().equals(shipping.getProductCode().getValue())));
            }
        }

        @Test
        @DisplayName("出荷情報を保存できる")
        void shouldSaveShipping() {
            ShippingList allShippings = shippingService.selectAll();
            if (!allShippings.asList().isEmpty()) {
                Shipping shipping = allShippings.asList().getFirst();
                
                // 出荷データを保存
                shippingService.save(shipping);
                
                // 再取得して確認
                Shipping savedShipping = shippingService.findById(shipping.getOrderNumber().getValue()).orElse(null);
                
                assertNotNull(savedShipping);
                assertEquals(shipping.getOrderNumber().getValue(), savedShipping.getOrderNumber().getValue());
                assertEquals(shipping.getProductCode().getValue(), savedShipping.getProductCode().getValue());
            }
        }
    }
}
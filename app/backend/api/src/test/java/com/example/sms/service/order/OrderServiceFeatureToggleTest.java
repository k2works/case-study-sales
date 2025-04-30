package com.example.sms.service.order;

import com.example.sms.FeatureToggleProperties;
import com.example.sms.domain.service.sales.order.OrderDomainService;
import com.example.sms.service.master.department.DepartmentRepository;
import com.example.sms.service.master.employee.EmployeeRepository;
import com.example.sms.service.master.partner.PartnerRepository;
import com.example.sms.service.master.product.ProductRepository;
import com.example.sms.service.sales.order.SalesOrderRepository;
import com.example.sms.service.sales.order.SalesOrderService;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("受注サービス")
@ExtendWith(MockitoExtension.class) // モック初期化
class OrderServiceFeatureToggleTest {

    @Mock
    private SalesOrderRepository salesOrderRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private PartnerRepository partnerRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private OrderDomainService orderDomainService;
    @Mock
    private FeatureToggleProperties featureToggleProperties;
    @InjectMocks
    private SalesOrderService salesOrderService;

    private LogCaptor logCaptor;

    @BeforeEach
    void setUp() {
        logCaptor = LogCaptor.forClass(SalesOrderService.class); // ログキャプチャ設定
    }

    @Test
    @DisplayName("新機能が有効でベータ機能が無効の場合")
    void testExecuteFeature_NewFeatureEnabled() {
        // モック振る舞いを設定
        when(featureToggleProperties.isNewFeature()).thenReturn(true);
        when(featureToggleProperties.isBetaFeature()).thenReturn(false);

        // メソッドを実行
        salesOrderService.executeFeature();

        // モックの呼び出し確認: 呼び出されたことを確認
        verify(featureToggleProperties, times(1)).isNewFeature(); // 1度呼び出し
        verify(featureToggleProperties, times(1)).isBetaFeature(); // 1度呼び出し

        // ログ出力の検証 (任意)
        assertThat(logCaptor.getInfoLogs())
                .contains("新機能が有効です: 新しい処理を実行します！")
                .contains("ベータ機能は無効です: ロックされています。");
    }

    @Test
    @DisplayName("ベータ機能が有効で新機能が無効の場合")
    void testExecuteFeature_BetaFeatureEnabled() {
        // モック振る舞いを設定
        when(featureToggleProperties.isNewFeature()).thenReturn(false);
        when(featureToggleProperties.isBetaFeature()).thenReturn(true);

        // メソッドを実行
        salesOrderService.executeFeature();

        // モックの呼び出し確認: 呼び出されたことを確認
        verify(featureToggleProperties, times(1)).isNewFeature(); // 1度呼び出し
        verify(featureToggleProperties, times(1)).isBetaFeature(); // 1度呼び出し

        // ログ出力の検証 (任意)
        assertThat(logCaptor.getInfoLogs())
                .contains("新機能は無効です: 従来の処理を実行します。")
                .contains("ベータ機能が有効です: 特定の機能にアクセス可能です。");
    }

    @Test
    @DisplayName("新機能とベータ機能が無効の場合")
    void testExecuteFeature_NewAndBetaFeatureDisabled() {
        // モック振る舞いを設定
        when(featureToggleProperties.isNewFeature()).thenReturn(false);
        when(featureToggleProperties.isBetaFeature()).thenReturn(false);

        // メソッドを実行
        salesOrderService.executeFeature();

        // モックの呼び出し確認: 呼び出されたことを確認
        verify(featureToggleProperties, times(1)).isNewFeature(); // 1度呼び出し
        verify(featureToggleProperties, times(1)).isBetaFeature(); // 1度呼び出し

        // ログ出力の検証 (任意)
        assertThat(logCaptor.getInfoLogs())
                .contains("新機能は無効です: 従来の処理を実行します。")
                .contains("ベータ機能は無効です: ロックされています。");
    }
}

package com.example.sms.service.procurement.payment;

import com.example.sms.domain.model.master.partner.vendor.Vendor;
import com.example.sms.domain.model.master.partner.vendor.VendorCode;
import com.example.sms.domain.model.procurement.purchase.Purchase;
import com.example.sms.domain.model.procurement.purchase.PurchaseList;
import com.example.sms.domain.model.procurement.payment.PurchasePayment;
import com.example.sms.service.master.partner.PartnerRepository;
import com.example.sms.service.procurement.purchase.PurchaseRepository;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 支払データサービス
 */
@Service
@Transactional
public class PurchasePaymentService {

    final PurchasePaymentRepository purchasePaymentRepository;

    final PurchaseRepository purchaseRepository;

    final PartnerRepository partnerRepository;

    public PurchasePaymentService(PurchasePaymentRepository purchasePaymentRepository, PurchaseRepository purchaseRepository, PartnerRepository partnerRepository) {
        this.purchasePaymentRepository = purchasePaymentRepository;
        this.purchaseRepository = purchaseRepository;
        this.partnerRepository = partnerRepository;
    }

    /**
     * 支払データを保存する
     *
     * @param payment 支払データ
     */
    public void save(PurchasePayment payment) {
        purchasePaymentRepository.save(payment);
    }

    /**
     * 支払データを新規登録する
     *
     * @param payment 支払データ
     */
    public void register(PurchasePayment payment) {
        purchasePaymentRepository.save(payment);
    }

    /**
     * 全ての支払データを取得する
     *
     * @return 支払データのリスト
     */
    public List<PurchasePayment> selectAll() {
        return purchasePaymentRepository.selectAll().asList();
    }

    /**
     * 支払番号で支払データを検索する
     *
     * @param paymentNumber 支払番号
     * @return 支払データ（存在しない場合はEmpty）
     */
    public Optional<PurchasePayment> findByPaymentNumber(String paymentNumber) {
        return purchasePaymentRepository.findByPaymentNumber(paymentNumber);
    }

    /**
     * 支払データを削除する
     *
     * @param paymentNumber 支払番号
     */
    public void delete(String paymentNumber) {
        purchasePaymentRepository.delete(paymentNumber);
    }

    /**
     * 全ての支払データを削除する
     */
    public void deleteAll() {
        purchasePaymentRepository.deleteAll();
    }

    /**
     * ページング情報付きで全ての支払データを取得する
     *
     * @return ページング情報付きの支払データ
     */
    public PageInfo<PurchasePayment> selectAllWithPageInfo() {
        return purchasePaymentRepository.selectAllWithPageInfo();
    }

    /**
     * 検索条件に基づいて支払データを検索する（ページング付き）
     *
     * @param criteria 検索条件
     * @return ページング情報付きの支払データ
     */
    public PageInfo<PurchasePayment> searchWithPageInfo(PurchasePaymentCriteria criteria) {
        return purchasePaymentRepository.searchWithPageInfo(criteria);
    }

    /**
     * 仕入データから支払データを集計する
     * 仕入先ごとに当月の仕入金額を合計して支払データを作成する
     */
    public void aggregate() {
        PurchaseList purchaseList = purchaseRepository.selectAll();

        // 仕入先コードでグループ化
        Map<String, List<Purchase>> purchasesBySupplier = purchaseList.asList().stream()
                .collect(Collectors.groupingBy(purchase ->
                    purchase.getSupplierCode().getCode().getValue() + "-" +
                    purchase.getSupplierCode().getBranchNumber()
                ));

        // 仕入先ごとに支払データを作成（カウンターを使用してユニーク性を確保）
        final int[] counter = {1};
        purchasesBySupplier.forEach((supplierKey, purchases) -> {
            registerPurchasePaymentApplication(purchases, counter[0]++);
        });
    }

    /**
     * 仕入データリストから支払データを登録する
     * 同じ仕入先の仕入データを集計して1つの支払データを作成
     *
     * @param purchases 仕入データのリスト
     * @param counter カウンター（支払番号のユニーク性を確保するため）
     */
    public void registerPurchasePaymentApplication(List<Purchase> purchases, int counter) {
        if (purchases.isEmpty()) {
            return;
        }

        // 最初の仕入データから仕入先情報を取得
        Purchase firstPurchase = purchases.get(0);

        // 仕入金額と消費税を合計
        int totalAmount = purchases.stream()
                .mapToInt(p -> p.getTotalPurchaseAmount().getAmount())
                .sum();

        int totalTax = purchases.stream()
                .mapToInt(p -> p.getTotalConsumptionTax().getAmount())
                .sum();

        // 支払番号を生成（カウンターを使用してユニーク性を確保）
        String paymentNumber = String.format("PAY%07d", counter);

        // 支払日を現在日時から生成
        Integer paymentDate = Integer.parseInt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));

        // 仕入先マスタから支払方法を取得
        VendorCode vendorCode = VendorCode.of(
                firstPurchase.getSupplierCode().getCode().getValue(),
                firstPurchase.getSupplierCode().getBranchNumber()
        );
        Optional<Vendor> vendorOpt = partnerRepository.findVendorById(vendorCode);
        int paymentMethod = vendorOpt
                .map(vendor -> vendor.getVendorClosingBilling().getPaymentMethod().getValue())
                .orElse(1); // デフォルトは振込

        // 支払データを作成
        // 部門コードと部門開始日は仕入データから取得
        PurchasePayment payment = PurchasePayment.of(
                paymentNumber,
                paymentDate,
                firstPurchase.getDepartmentCode().getValue(),
                firstPurchase.getStartDate(),
                firstPurchase.getSupplierCode().getCode().getValue(),
                firstPurchase.getSupplierCode().getBranchNumber(),
                paymentMethod, // 仕入先マスタの支払方法を使用
                totalAmount,
                totalTax,
                false // 支払未完了
        );

        purchasePaymentRepository.save(payment);
    }
}

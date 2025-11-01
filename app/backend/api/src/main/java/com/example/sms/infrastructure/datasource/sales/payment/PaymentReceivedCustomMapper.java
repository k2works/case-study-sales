package com.example.sms.infrastructure.datasource.sales.payment;

import com.example.sms.service.sales.payment.PaymentReceivedCriteria;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 入金データのカスタムマッパー
 */
@Mapper
public interface PaymentReceivedCustomMapper {

    /**
     * 主キーで入金データを検索する
     *
     * @param paymentReceivedNumber 入金番号
     * @return 入金データのカスタムエンティティ
     */
    PaymentReceivedCustomEntity selectByPrimaryKey(String paymentReceivedNumber);

    /**
     * 全ての入金データを取得する
     *
     * @return 入金データのカスタムエンティティのリスト
     */
    List<PaymentReceivedCustomEntity> selectAll();

    /**
     * 全ての入金データを削除する
     */
    @Delete("DELETE FROM public.入金データ")
    void deleteAll();

    /**
     * 入金データを挿入する
     *
     * @param entity 入金データのカスタムエンティティ
     */
    void insert(PaymentReceivedCustomEntity entity);

    /**
     * 楽観的ロックを使用して入金データを更新する
     *
     * @param entity 入金データのエンティティ
     * @return 更新された行数
     */
    int updateByPrimaryKeyForOptimisticLock(PaymentReceivedCustomEntity entity);

    /**
     * 顧客コードで入金データを検索する
     *
     * @param customerCode 顧客コード
     * @param branchNumber 顧客枝番
     * @return 入金データのカスタムエンティティのリスト
     */
    List<PaymentReceivedCustomEntity> selectByCustomer(@Param("customerCode") String customerCode, @Param("branchNumber") Integer branchNumber);

    /**
     * 入金口座コードで入金データを検索する
     *
     * @param accountCode 入金口座コード
     * @return 入金データのカスタムエンティティのリスト
     */
    List<PaymentReceivedCustomEntity> selectByAccount(String accountCode);

    List<PaymentReceivedCustomEntity> selectByCriteria(PaymentReceivedCriteria criteria);
}

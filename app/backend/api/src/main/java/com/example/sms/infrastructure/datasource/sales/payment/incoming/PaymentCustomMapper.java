package com.example.sms.infrastructure.datasource.sales.payment.incoming;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 入金データのカスタムマッパー
 */
@Mapper
public interface PaymentCustomMapper {

    /**
     * 主キーで入金データを検索する
     *
     * @param paymentNumber 入金番号
     * @return 入金データのカスタムエンティティ
     */
    PaymentCustomEntity selectByPrimaryKey(String paymentNumber);

    /**
     * 全ての入金データを取得する
     *
     * @return 入金データのカスタムエンティティのリスト
     */
    List<PaymentCustomEntity> selectAll();

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
    void insert(PaymentCustomEntity entity);

    /**
     * 楽観的ロックを使用して入金データを更新する
     *
     * @param entity 入金データのエンティティ
     * @return 更新された行数
     */
    int updateByPrimaryKeyForOptimisticLock(PaymentCustomEntity entity);

    /**
     * 顧客コードで入金データを検索する
     *
     * @param customerCode 顧客コード
     * @param branchNumber 顧客枝番
     * @return 入金データのカスタムエンティティのリスト
     */
    List<PaymentCustomEntity> selectByCustomer(@Param("customerCode") String customerCode, @Param("branchNumber") Integer branchNumber);

    /**
     * 入金口座コードで入金データを検索する
     *
     * @param accountCode 入金口座コード
     * @return 入金データのカスタムエンティティのリスト
     */
    List<PaymentCustomEntity> selectByAccount(String accountCode);
}
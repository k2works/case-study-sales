package com.example.sms.infrastructure.datasource.master.payment.account;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 入金口座マスタのカスタムマッパー
 */
@Mapper
public interface PaymentAccountCustomMapper {

    /**
     * 主キーで入金口座マスタを検索する
     *
     * @param accountCode 入金口座コード
     * @return 入金口座マスタのカスタムエンティティ
     */
    PaymentAccountCustomEntity selectByPrimaryKey(String accountCode);

    /**
     * 全ての入金口座マスタを取得する
     *
     * @return 入金口座マスタのカスタムエンティティのリスト
     */
    List<PaymentAccountCustomEntity> selectAll();

    /**
     * 全ての入金口座マスタを削除する
     */
    @Delete("DELETE FROM public.入金口座マスタ")
    void deleteAll();

    /**
     * 入金口座マスタを挿入する
     *
     * @param entity 入金口座マスタのカスタムエンティティ
     */
    void insert(PaymentAccountCustomEntity entity);

    /**
     * 楽観的ロックを使用して入金口座マスタを更新する
     *
     * @param entity 入金口座マスタのエンティティ
     * @return 更新された行数
     */
    int updateByPrimaryKeyForOptimisticLock(PaymentAccountCustomEntity entity);
}
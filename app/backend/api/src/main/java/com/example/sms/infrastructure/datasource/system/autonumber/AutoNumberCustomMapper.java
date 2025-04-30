package com.example.sms.infrastructure.datasource.system.autonumber;

import com.example.sms.infrastructure.datasource.autogen.model.自動採番マスタ;
import com.example.sms.infrastructure.datasource.autogen.model.自動採番マスタKey;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface AutoNumberCustomMapper {

    AutoNumberCustomEntity selectByPrimaryKey(自動採番マスタKey key);

    AutoNumberCustomEntity selectByDocumentTypeAndYearMonth(String documentTypeCode, LocalDateTime yearMonth);

    List<AutoNumberCustomEntity> selectAll();

    @Delete("DELETE FROM public.自動採番マスタ")
    void deleteAll();

    void insert(AutoNumberCustomEntity entity);

    void insertForOptimisticLock(自動採番マスタ entity);

    int updateByPrimaryKeyForOptimisticLock(自動採番マスタ entity);
}
package com.example.sms.infrastructure.datasource.autonumber;

import com.example.sms.domain.model.autonumber.AutoNumber;
import com.example.sms.infrastructure.datasource.autogen.mapper.自動採番マスタMapper;
import com.example.sms.infrastructure.datasource.autogen.model.自動採番マスタ;
import com.example.sms.infrastructure.datasource.autogen.model.自動採番マスタKey;
import com.example.sms.service.autonumber.AutoNumberRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class AutoNumberDataSource implements AutoNumberRepository {
    private final 自動採番マスタMapper autoNumberMapper;
    private final AutoNumberCustomMapper autoNumberCustomMapper;
    private final AutoNumberEntityMapper autoNumberEntityMapper;

    public AutoNumberDataSource(
            自動採番マスタMapper autoNumberMapper,
            AutoNumberCustomMapper autoNumberCustomMapper,
            AutoNumberEntityMapper autoNumberEntityMapper) {
        this.autoNumberMapper = autoNumberMapper;
        this.autoNumberCustomMapper = autoNumberCustomMapper;
        this.autoNumberEntityMapper = autoNumberEntityMapper;
    }

    @Override
    public void deleteAll() {
        autoNumberCustomMapper.deleteAll();
    }

    @Override
    public void save(AutoNumber autoNumber) {
        自動採番マスタKey key = autoNumberEntityMapper.mapToKey(autoNumber);
        Optional<AutoNumberCustomEntity> autoNumberEntity = Optional.ofNullable(autoNumberCustomMapper.selectByPrimaryKey(key));

        if (autoNumberEntity.isEmpty()) {
            createAutoNumber(autoNumber);
        } else {
            updateAutoNumber(autoNumber);
        }
    }

    private void updateAutoNumber(AutoNumber autoNumber) {
        自動採番マスタ autoNumberData = autoNumberEntityMapper.mapToEntity(autoNumber);
        autoNumberMapper.updateByPrimaryKey(autoNumberData);
    }

    private void createAutoNumber(AutoNumber autoNumber) {
        自動採番マスタ autoNumberData = autoNumberEntityMapper.mapToEntity(autoNumber);
        autoNumberMapper.insert(autoNumberData);
    }

    @Override
    public List<AutoNumber> selectAll() {
        List<AutoNumberCustomEntity> autoNumberCustomEntities = autoNumberCustomMapper.selectAll();

        return autoNumberCustomEntities.stream()
                .map(autoNumberEntityMapper::mapToDomainModel)
                .toList();
    }

    @Override
    public Optional<AutoNumber> findByDocumentTypeAndYearMonth(String documentTypeCode, LocalDateTime yearMonth) {
        AutoNumberCustomEntity autoNumberCustomEntity = autoNumberCustomMapper.selectByDocumentTypeAndYearMonth(documentTypeCode, yearMonth);
        return autoNumberCustomEntity != null ? 
                Optional.of(autoNumberEntityMapper.mapToDomainModel(autoNumberCustomEntity)) : 
                Optional.empty();
    }

    @Override
    public void delete(AutoNumber autoNumber) {
        自動採番マスタKey key = autoNumberEntityMapper.mapToKey(autoNumber);
        autoNumberMapper.deleteByPrimaryKey(key);
    }

    @Override
    public Integer getNextDocumentNumber(String documentTypeCode, LocalDateTime yearMonth) {
        Optional<AutoNumber> autoNumber = findByDocumentTypeAndYearMonth(documentTypeCode, yearMonth);
        return autoNumber.map(AutoNumber::getNextDocumentNumber).orElse(1);
    }

    @Override
    public AutoNumber incrementDocumentNumber(String documentTypeCode, LocalDateTime yearMonth) {
        Optional<AutoNumber> autoNumber = findByDocumentTypeAndYearMonth(documentTypeCode, yearMonth);
        
        if (autoNumber.isPresent()) {
            AutoNumber updatedAutoNumber = autoNumber.get().incrementDocumentNumber();
            save(updatedAutoNumber);
            return updatedAutoNumber;
        } else {
            // 存在しない場合は新規作成
            AutoNumber newAutoNumber = AutoNumber.of(documentTypeCode, yearMonth, 1);
            save(newAutoNumber);
            return newAutoNumber;
        }
    }
}
package com.example.sms.service.system.autonumber;

import com.example.sms.domain.model.system.autonumber.AutoNumber;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AutoNumberRepository {

    void deleteAll();

    void save(AutoNumber autoNumber);

    List<AutoNumber> selectAll();

    Optional<AutoNumber> findByDocumentTypeAndYearMonth(String documentTypeCode, LocalDateTime yearMonth);

    void delete(AutoNumber autoNumber);

    Integer getNextDocumentNumber(String documentTypeCode, LocalDateTime yearMonth);

    AutoNumber incrementDocumentNumber(String documentTypeCode, LocalDateTime yearMonth);
}
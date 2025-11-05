package com.example.sms.service.system.autonumber;

import com.example.sms.domain.model.system.autonumber.AutoNumber;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 自動採番サービス
 */
@Service
@Transactional
public class AutoNumberService {
    private final AutoNumberRepository autoNumberRepository;

    public AutoNumberService(AutoNumberRepository autoNumberRepository) {
        this.autoNumberRepository = autoNumberRepository;
    }

    public List<AutoNumber> selectAll() {
        return autoNumberRepository.selectAll();
    }

    public Optional<AutoNumber> findByDocumentTypeAndYearMonth(String documentTypeCode, LocalDateTime yearMonth) {
        return autoNumberRepository.findByDocumentTypeAndYearMonth(documentTypeCode, yearMonth);
    }

    public void save(AutoNumber autoNumber) {
        autoNumberRepository.save(autoNumber);
    }

    public void delete(AutoNumber autoNumber) {
        autoNumberRepository.delete(autoNumber);
    }

    public Integer getNextDocumentNumber(String documentTypeCode, LocalDateTime yearMonth) {
        return autoNumberRepository.getNextDocumentNumber(documentTypeCode, yearMonth);
    }

    public AutoNumber incrementDocumentNumber(String documentTypeCode, LocalDateTime yearMonth) {
        return autoNumberRepository.incrementDocumentNumber(documentTypeCode, yearMonth);
    }

    public void deleteAll() {
        autoNumberRepository.deleteAll();
    }
}
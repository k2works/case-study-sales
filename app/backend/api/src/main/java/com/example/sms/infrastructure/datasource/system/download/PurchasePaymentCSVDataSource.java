package com.example.sms.infrastructure.datasource.system.download;

import com.example.sms.domain.model.procurement.payment.PurchasePaymentList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.procurement.payment.PurchasePaymentCustomEntity;
import com.example.sms.infrastructure.datasource.procurement.payment.PurchasePaymentCustomMapper;
import com.example.sms.infrastructure.datasource.procurement.payment.PurchasePaymentEntityMapper;
import com.example.sms.service.system.download.PurchasePaymentCSVRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * 支払CSVデータソース
 */
@Repository
@RequiredArgsConstructor
public class PurchasePaymentCSVDataSource implements PurchasePaymentCSVRepository {

    private final PurchasePaymentCustomMapper purchasePaymentCustomMapper;
    private final PurchasePaymentEntityMapper purchasePaymentEntityMapper;

    @Override
    public List<PurchasePaymentDownloadCSV> convert(PurchasePaymentList purchasePaymentList) {
        if (purchasePaymentList != null) {
            return purchasePaymentList.asList().stream()
                    .map(purchasePaymentEntityMapper::mapToCsvModel)
                    .toList();
        }
        return List.of();
    }

    @Override
    public int countBy(DownloadCriteria condition) {
        List<PurchasePaymentCustomEntity> entities = purchasePaymentCustomMapper.selectByCriteria(new HashMap<>());
        return entities.size();
    }

    @Override
    public PurchasePaymentList selectBy(DownloadCriteria condition) {
        List<PurchasePaymentCustomEntity> entities = purchasePaymentCustomMapper.selectByCriteria(new HashMap<>());
        return PurchasePaymentList.of(purchasePaymentEntityMapper.toModelList(entities));
    }
}

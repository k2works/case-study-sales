package com.example.sms.infrastructure.datasource.system.download;

import com.example.sms.domain.model.procurement.purchase.PurchaseList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.procurement.purchase.PurchaseCustomEntity;
import com.example.sms.infrastructure.datasource.procurement.purchase.PurchaseCustomMapper;
import com.example.sms.infrastructure.datasource.procurement.purchase.PurchaseEntityMapper;
import com.example.sms.service.system.download.PurchaseCSVRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PurchaseCSVDataSource implements PurchaseCSVRepository {
    private final PurchaseCustomMapper purchaseCustomMapper;
    private final PurchaseEntityMapper purchaseEntityMapper;

    // コンストラクタ
    public PurchaseCSVDataSource(PurchaseCustomMapper purchaseCustomMapper, PurchaseEntityMapper purchaseEntityMapper) {
        this.purchaseCustomMapper = purchaseCustomMapper;
        this.purchaseEntityMapper = purchaseEntityMapper;
    }

    @Override
    public List<PurchaseDownloadCSV> convert(PurchaseList purchaseList) {
        if (purchaseList != null) {
            return purchaseList.asList().stream()
                    .flatMap(purchase ->
                            purchase.getPurchaseLines().stream()
                                    .map(purchaseLine -> purchaseEntityMapper.mapToCsvModel(purchase, purchaseLine))
                    ).toList();
        }
        return List.of();
    }

    @Override
    public int countBy(DownloadCriteria condition) {
        List<PurchaseCustomEntity> purchaseEntities = purchaseCustomMapper.selectAll();
        return purchaseEntities.size();
    }

    @Override
    public PurchaseList selectBy(DownloadCriteria condition) {
        List<PurchaseCustomEntity> purchaseEntities = purchaseCustomMapper.selectAll();
        return new PurchaseList(purchaseEntities.stream()
                .map(purchaseEntityMapper::toModel)
                .toList());
    }
}

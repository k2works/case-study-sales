package com.example.sms.infrastructure.datasource.system.download;

import com.example.sms.domain.model.procurement.purchase.order.PurchaseOrderList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.procurement.purchase.order.PurchaseOrderCustomEntity;
import com.example.sms.infrastructure.datasource.procurement.purchase.order.PurchaseOrderCustomMapper;
import com.example.sms.infrastructure.datasource.procurement.purchase.order.PurchaseOrderEntityMapper;
import com.example.sms.service.system.download.PurchaseOrderCSVRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PurchaseOrderCSVDataSource implements PurchaseOrderCSVRepository {
    private final PurchaseOrderCustomMapper purchaseOrderCustomMapper;
    private final PurchaseOrderEntityMapper purchaseOrderEntityMapper;

    // コンストラクタ
    public PurchaseOrderCSVDataSource(PurchaseOrderCustomMapper purchaseOrderCustomMapper, PurchaseOrderEntityMapper purchaseOrderEntityMapper) {
        this.purchaseOrderCustomMapper = purchaseOrderCustomMapper;
        this.purchaseOrderEntityMapper = purchaseOrderEntityMapper;
    }

    @Override
    public List<PurchaseOrderDownloadCSV> convert(PurchaseOrderList purchaseOrderList) {
        if (purchaseOrderList != null) {
            return purchaseOrderList.asList().stream()
                    .flatMap(purchaseOrder ->
                            purchaseOrder.getPurchaseOrderLines().stream()
                                    .map(purchaseOrderLine -> purchaseOrderEntityMapper.mapToCsvModel(purchaseOrder, purchaseOrderLine))
                    ).toList();
        }
        return List.of();
    }

    @Override
    public int countBy(DownloadCriteria condition) {
        List<PurchaseOrderCustomEntity> purchaseOrderEntities = purchaseOrderCustomMapper.selectAll();
        return purchaseOrderEntities.size();
    }

    @Override
    public PurchaseOrderList selectBy(DownloadCriteria condition) {
        List<PurchaseOrderCustomEntity> purchaseOrderEntities = purchaseOrderCustomMapper.selectAll();
        return new PurchaseOrderList(purchaseOrderEntities.stream()
                .map(purchaseOrderEntityMapper::toModel)
                .toList());
    }
}
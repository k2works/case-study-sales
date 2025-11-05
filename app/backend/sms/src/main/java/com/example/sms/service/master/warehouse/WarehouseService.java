package com.example.sms.service.master.warehouse;

import com.example.sms.domain.model.master.warehouse.Warehouse;
import com.example.sms.domain.model.master.warehouse.WarehouseCode;
import com.example.sms.domain.model.master.warehouse.WarehouseList;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 倉庫サービス
 */
@Service
@Transactional
public class WarehouseService {
    final WarehouseRepository warehouseRepository;

    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    /**
     * 倉庫一覧
     */
    public WarehouseList selectAll() {
        return warehouseRepository.selectAll();
    }

    /**
     * 倉庫一覧（ページング）
     */
    public PageInfo<Warehouse> selectAllWithPageInfo() {
        return warehouseRepository.selectAllWithPageInfo();
    }

    /**
     * 倉庫新規登録
     */
    public void register(Warehouse warehouse) {
        warehouseRepository.save(warehouse);
    }

    /**
     * 倉庫情報編集
     */
    public void save(Warehouse warehouse) {
        warehouseRepository.save(warehouse);
    }

    /**
     * 倉庫削除
     */
    public void delete(WarehouseCode warehouseCode) {
        warehouseRepository.deleteById(warehouseCode);
    }

    /**
     * 倉庫検索 (倉庫コード)
     */
    public Warehouse find(WarehouseCode warehouseCode) {
        return warehouseRepository.findById(warehouseCode).orElse(null);
    }

    /**
     * 倉庫検索（倉庫コード）
     */
    public WarehouseList findByCode(String warehouseCode) {
        return warehouseRepository.findByCode(warehouseCode);
    }

    /**
     * 倉庫検索
     */
    public PageInfo<Warehouse> searchWithPageInfo(WarehouseCriteria criteria) {
        return warehouseRepository.searchWithPageInfo(criteria);
    }
}
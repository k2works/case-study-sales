package com.example.sms.service.master.locationnumber;

import com.example.sms.domain.model.master.locationnumber.LocationNumber;
import com.example.sms.domain.model.master.locationnumber.LocationNumberKey;
import com.example.sms.domain.model.master.locationnumber.LocationNumberList;
import com.example.sms.infrastructure.datasource.autogen.model.棚番マスタKey;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 棚番サービス
 */
@Service
@Transactional
public class LocationNumberService {
    final LocationNumberRepository locationNumberRepository;

    public LocationNumberService(LocationNumberRepository locationNumberRepository) {
        this.locationNumberRepository = locationNumberRepository;
    }

    /**
     * 棚番一覧
     */
    public LocationNumberList selectAll() {
        return locationNumberRepository.selectAll();
    }

    /**
     * 棚番一覧（ページング）
     */
    public PageInfo<LocationNumber> selectAllWithPageInfo() {
        return locationNumberRepository.selectAllWithPageInfo();
    }

    /**
     * 棚番新規登録
     */
    public void register(LocationNumber locationNumber) {
        locationNumberRepository.save(locationNumber);
    }

    /**
     * 棚番情報編集
     */
    public void save(LocationNumber locationNumber) {
        locationNumberRepository.save(locationNumber);
    }

    /**
     * 棚番削除
     */
    public void delete(棚番マスタKey key) {
        locationNumberRepository.deleteById(key);
    }

    /**
     * 棚番削除 (ドメインキー)
     */
    public void delete(LocationNumberKey key) {
        棚番マスタKey infraKey = new 棚番マスタKey();
        infraKey.set倉庫コード(key.getWarehouseCode().getValue());
        infraKey.set棚番コード(key.getLocationNumberCode().getValue());
        infraKey.set商品コード(key.getProductCode().getValue());
        locationNumberRepository.deleteById(infraKey);
    }

    /**
     * 棚番検索 (複合キー)
     */
    public LocationNumber find(棚番マスタKey key) {
        return locationNumberRepository.findById(key).orElse(null);
    }

    /**
     * 棚番検索 (ドメインキー)
     */
    public LocationNumber find(LocationNumberKey key) {
        棚番マスタKey infraKey = new 棚番マスタKey();
        infraKey.set倉庫コード(key.getWarehouseCode().getValue());
        infraKey.set棚番コード(key.getLocationNumberCode().getValue());
        infraKey.set商品コード(key.getProductCode().getValue());
        return locationNumberRepository.findById(infraKey).orElse(null);
    }

    /**
     * 棚番検索（倉庫コード）
     */
    public LocationNumberList findByWarehouseCode(String warehouseCode) {
        return locationNumberRepository.findByWarehouseCode(warehouseCode);
    }

    /**
     * 棚番検索（棚番コード）
     */
    public LocationNumberList findByLocationNumberCode(String locationNumberCode) {
        return locationNumberRepository.findByLocationNumberCode(locationNumberCode);
    }

    /**
     * 棚番検索
     */
    public PageInfo<LocationNumber> searchWithPageInfo(LocationNumberCriteria criteria) {
        return locationNumberRepository.searchWithPageInfo(criteria);
    }
}
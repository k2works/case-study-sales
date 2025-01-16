package com.example.sms.service.master.common;

import com.example.sms.domain.model.common.region.Region;
import com.example.sms.domain.model.common.region.RegionList;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 地域サービス
 */
@Service
@Transactional
public class RegionService {
    final RegionRepository regionRepository;

    public RegionService(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    /**
     * 地域一覧を取得
     */
    public RegionList selectAll() {
        return regionRepository.selectAll();
    }

    /**
     * 地域新規登録
     */
    public void register(Region region) {
        regionRepository.save(region);
    }

    /**
     * 地域情報を編集
     */
    public void save(Region region) {
        regionRepository.save(region);
    }

    /**
     * 地域を削除
     */
    public void delete(Region region) {
        regionRepository.deleteById(region);
    }

    /**
     * 地域検索
     */
    public Region find(String regionCode) {
        return regionRepository.findById(regionCode).orElse(null);
    }

    /**
     * 地域検索 (ページング)
     */
    public PageInfo<Region> searchWithPageInfo(RegionCriteria criteria) {
        return regionRepository.searchWithPageInfo(criteria);
    }

    /**
     * 地域一覧をページングで取得
     */
    public PageInfo<Region> selectAllWithPageInfo() {
        return regionRepository.selectAllWithPageInfo();
    }
}
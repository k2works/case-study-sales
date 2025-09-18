package com.example.sms.infrastructure.datasource.system.download;

import com.example.sms.domain.model.master.locationnumber.LocationNumberList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.autogen.mapper.棚番マスタMapper;
import com.example.sms.infrastructure.datasource.master.locationnumber.LocationNumberCustomEntity;
import com.example.sms.infrastructure.datasource.master.locationnumber.LocationNumberCustomMapper;
import com.example.sms.infrastructure.datasource.master.locationnumber.LocationNumberEntityMapper;
import com.example.sms.service.system.download.LocationNumberCSVRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LocationNumberCSVDataSource implements LocationNumberCSVRepository {
    final 棚番マスタMapper locationNumberMapper;
    final LocationNumberCustomMapper locationNumberCustomMapper;
    final LocationNumberEntityMapper locationNumberEntityMapper;

    public LocationNumberCSVDataSource(棚番マスタMapper locationNumberMapper, LocationNumberCustomMapper locationNumberCustomMapper, LocationNumberEntityMapper locationNumberEntityMapper) {
        this.locationNumberMapper = locationNumberMapper;
        this.locationNumberCustomMapper = locationNumberCustomMapper;
        this.locationNumberEntityMapper = locationNumberEntityMapper;
    }

    @Override
    public List<LocationNumberDownloadCSV> convert(LocationNumberList locationNumberList) {
        if (locationNumberList != null) {
            return locationNumberList.asList().stream()
                    .map(locationNumberEntityMapper::mapToCsvModel)
                    .toList();
        }
        return List.of();
    }

    @Override
    public int countBy(DownloadCriteria condition) {
        List<LocationNumberCustomEntity> locationNumberEntities = locationNumberCustomMapper.selectAll();
        return locationNumberEntities.size();
    }

    @Override
    public LocationNumberList selectBy(DownloadCriteria condition) {
        List<LocationNumberCustomEntity> locationNumberEntities = locationNumberCustomMapper.selectAll();
        return new LocationNumberList(locationNumberEntities.stream()
                .map(locationNumberEntityMapper::mapToDomainModel)
                .toList());
    }
}
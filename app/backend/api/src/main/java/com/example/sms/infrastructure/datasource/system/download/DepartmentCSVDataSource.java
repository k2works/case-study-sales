package com.example.sms.infrastructure.datasource.system.download;

import com.example.sms.domain.model.master.department.DepartmentList;
import com.example.sms.domain.model.system.download.DownloadCondition;
import com.example.sms.infrastructure.datasource.master.department.DepartmentEntityMapper;
import com.example.sms.infrastructure.datasource.master.department.部門マスタ;
import com.example.sms.infrastructure.datasource.master.department.部門マスタMapper;
import com.example.sms.service.system.download.DepartmentCSVRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DepartmentCSVDataSource implements DepartmentCSVRepository {
    final 部門マスタMapper departmentMapper;
    final DepartmentEntityMapper departmentEntityMapper;

    public DepartmentCSVDataSource(部門マスタMapper departmentMapper, DepartmentEntityMapper departmentEntityMapper) {
        this.departmentMapper = departmentMapper;
        this.departmentEntityMapper = departmentEntityMapper;
    }

    @Override
    public List<DepartmentDownloadCSV> convert(DepartmentList departmentList) {
        if (departmentList != null) {
            return departmentList.asList().stream()
                    .map(departmentEntityMapper::mapToCsvModel)
                    .toList();
        }
        return List.of();
    }

    @Override
    public int countBy(DownloadCondition condition) {
        List<部門マスタ> departmentEntities = departmentMapper.selectAll();
        return departmentEntities.size();
    }

    @Override
    public DepartmentList selectBy(DownloadCondition condition) {
        List<部門マスタ> departmentEntities = departmentMapper.selectAll();
        return new DepartmentList(departmentEntities.stream()
                .map(departmentEntityMapper::mapToDomainModel)
                .toList());
    }
}

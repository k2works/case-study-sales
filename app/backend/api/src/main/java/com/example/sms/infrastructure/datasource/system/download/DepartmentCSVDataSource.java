package com.example.sms.infrastructure.datasource.system.download;

import com.example.sms.domain.model.master.department.DepartmentList;
import com.example.sms.domain.model.system.download.DownloadCondition;
import com.example.sms.infrastructure.datasource.autogen.mapper.部門マスタMapper;
import com.example.sms.infrastructure.datasource.autogen.model.部門マスタ;
import com.example.sms.infrastructure.datasource.master.department.DepartmentCustomEntity;
import com.example.sms.infrastructure.datasource.master.department.DepartmentCustomMapper;
import com.example.sms.infrastructure.datasource.master.department.DepartmentEntityMapper;
import com.example.sms.service.system.download.DepartmentCSVRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DepartmentCSVDataSource implements DepartmentCSVRepository {
    final 部門マスタMapper departmentMapper;
    final DepartmentCustomMapper departmentCustomMapper;
    final DepartmentEntityMapper departmentEntityMapper;

    public DepartmentCSVDataSource(部門マスタMapper departmentMapper, DepartmentCustomMapper departmentCustomMapper, DepartmentEntityMapper departmentEntityMapper) {
        this.departmentMapper = departmentMapper;
        this.departmentCustomMapper = departmentCustomMapper;
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
        List<DepartmentCustomEntity> departmentEntities = departmentCustomMapper.selectAll();
        return departmentEntities.size();
    }

    @Override
    public DepartmentList selectBy(DownloadCondition condition) {
        List<DepartmentCustomEntity> departmentEntities = departmentCustomMapper.selectAll();
        return new DepartmentList(departmentEntities.stream()
                .map(departmentEntityMapper::mapToDomainModel)
                .toList());
    }
}

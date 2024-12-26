package com.example.sms.infrastructure.datasource.system.download;

import com.example.sms.domain.model.master.employee.EmployeeList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.master.employee.EmployeeCustomEntity;
import com.example.sms.infrastructure.datasource.master.employee.EmployeeCustomMapper;
import com.example.sms.infrastructure.datasource.master.employee.EmployeeEntityMapper;
import com.example.sms.service.system.download.EmployeeCSVRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class EmployeeCSVDataSource implements EmployeeCSVRepository {
    private final EmployeeCustomMapper employeeCustomMapper;
    private final EmployeeEntityMapper employeeEntityMapper;

    public EmployeeCSVDataSource(EmployeeCustomMapper employeeCustomMapper, EmployeeEntityMapper employeeEntityMapper) {
        this.employeeCustomMapper = employeeCustomMapper;
        this.employeeEntityMapper = employeeEntityMapper;
    }

    @Override
    public List<EmployeeDownloadCSV> convert(EmployeeList employeeList) {
        if (employeeList != null) {
            return employeeList.asList().stream()
                    .map(employeeEntityMapper::mapToCsvModel)
                    .toList();
        }
        return List.of();
    }

    @Override
    public int countBy(DownloadCriteria condition) {
        List<EmployeeCustomEntity> employeeEntities = employeeCustomMapper.selectAll();
        return employeeEntities.size();
    }

    @Override
    public EmployeeList selectBy(DownloadCriteria condition) {
        List<EmployeeCustomEntity> employeeEntities = employeeCustomMapper.selectAll();
        return new EmployeeList(employeeEntities.stream()
                .map(employeeEntityMapper::mapToDomainModel)
                .toList());
    }
}

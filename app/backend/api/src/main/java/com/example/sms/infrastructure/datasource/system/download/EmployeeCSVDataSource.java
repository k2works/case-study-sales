package com.example.sms.infrastructure.datasource.system.download;

import com.example.sms.domain.model.master.employee.EmployeeList;
import com.example.sms.domain.model.system.download.DownloadCondition;
import com.example.sms.infrastructure.datasource.master.employee.EmployeeEntityMapper;
import com.example.sms.infrastructure.datasource.master.employee.社員マスタ;
import com.example.sms.infrastructure.datasource.master.employee.社員マスタMapper;
import com.example.sms.service.system.download.EmployeeCSVRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class EmployeeCSVDataSource implements EmployeeCSVRepository {
    private final 社員マスタMapper employeeMapper;
    private final EmployeeEntityMapper employeeEntityMapper;

    public EmployeeCSVDataSource(社員マスタMapper employeeMapper, EmployeeEntityMapper employeeEntityMapper) {
        this.employeeMapper = employeeMapper;
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
    public int countBy(DownloadCondition condition) {
        List<社員マスタ> employeeEntities = employeeMapper.selectAll();
        return employeeEntities.size();
    }

    @Override
    public EmployeeList selectBy(DownloadCondition condition) {
        List<社員マスタ> employeeEntities = employeeMapper.selectAll();
        return new EmployeeList(employeeEntities.stream()
                .map(employeeEntityMapper::mapToDomainModel)
                .toList());
    }
}

package com.example.sms.infrastructure.datasource.master.employee;

import com.example.sms.domain.model.master.employee.Employee;
import com.example.sms.domain.model.master.employee.EmployeeCode;
import com.example.sms.domain.model.master.employee.EmployeeList;
import com.example.sms.infrastructure.PageInfoHelper;
import com.example.sms.service.master.employee.EmployeeRepository;
import com.github.pagehelper.PageInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeDataSource implements EmployeeRepository {
    final 社員マスタMapper employeeMapper;
    final EmployeeEntityMapper employeeEntityMapper;

    public EmployeeDataSource(社員マスタMapper employeeMapper, EmployeeEntityMapper employeeEntityMapper) {
        this.employeeMapper = employeeMapper;
        this.employeeEntityMapper = employeeEntityMapper;
    }

    @Override
    public Optional<Employee> findById(EmployeeCode empCode) {
        社員マスタ employeeEntity = employeeMapper.selectByPrimaryKey(empCode.getValue());
        if (employeeEntity != null) {
            return Optional.of(employeeEntityMapper.mapToDomainModel(employeeEntity));
        }
        return Optional.empty();
    }

    @Override
    public EmployeeList selectAll() {
        List<社員マスタ> employeeEntities = employeeMapper.selectAll();
        return new EmployeeList(employeeEntities.stream()
                .map(employeeEntityMapper::mapToDomainModel)
                .toList());
    }

    @Override
    public PageInfo<Employee> selectAllWithPageInfo() {
        List<社員マスタ> employeeEntities = employeeMapper.selectAll();
        PageInfo<社員マスタ> pageInfo = new PageInfo<>(employeeEntities);

        return PageInfoHelper.of(pageInfo, employeeEntityMapper::mapToDomainModel);
    }

    @Override
    public void save(Employee employee) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null && authentication.getName() != null ? authentication.getName() : "system";

        Optional<社員マスタ> employeeEntity = Optional.ofNullable(employeeMapper.selectByPrimaryKey(employee.getEmpCode().getValue()));
        if (employeeEntity.isEmpty()) {
            社員マスタ newEmployeeEntity = employeeEntityMapper.mapToEntity(employee);

            newEmployeeEntity.set作成日時(LocalDateTime.now());
            newEmployeeEntity.set作成者名(username);
            newEmployeeEntity.set更新日時(LocalDateTime.now());
            newEmployeeEntity.set更新者名(username);
            employeeMapper.insert(newEmployeeEntity);
        } else {
            社員マスタ updateEmployeeEntity = employeeEntityMapper.mapToEntity(employee);

            updateEmployeeEntity.set作成日時(employeeEntity.get().get作成日時());
            updateEmployeeEntity.set作成者名(employeeEntity.get().get作成者名());
            updateEmployeeEntity.set更新日時(LocalDateTime.now());
            updateEmployeeEntity.set更新者名(username);
            employeeMapper.updateByPrimaryKey(updateEmployeeEntity);
        }
    }

    @Override
    public void deleteById(EmployeeCode empCode) {
        employeeMapper.deleteByPrimaryKey(empCode.getValue());
    }

    @Override
    public void deleteAll() {
        employeeMapper.deleteAll();
    }
}

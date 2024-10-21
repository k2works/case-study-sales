package com.example.sms.infrastructure.datasource.master.department;

import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.department.DepartmentId;
import com.example.sms.domain.model.master.department.DepartmentList;
import com.example.sms.service.master.department.DepartmentRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class DepartmentDataSource implements DepartmentRepository {
    final 部門マスタMapper departmentMapper;
    final DepartmentEntityMapper departmentEntityMapper;

    public DepartmentDataSource(部門マスタMapper departmentMapper, DepartmentEntityMapper departmentEntityMapper) {
        this.departmentMapper = departmentMapper;
        this.departmentEntityMapper = departmentEntityMapper;
    }

    @Override
    public Optional<Department> findById(DepartmentId departmentId) {
        部門マスタKey departmentKey = new 部門マスタKey();
        departmentKey.set部門コード(departmentId.getDeptCode().getValue());
        departmentKey.set開始日(departmentId.getDepartmentStartDate().getValue());
        部門マスタ departmentEntity = departmentMapper.selectByPrimaryKey(departmentKey);
        if (departmentEntity != null) {
            return Optional.of(departmentEntityMapper.mapToDomainModel(departmentEntity));
        }
        return Optional.empty();
    }

    @Override
    public DepartmentList selectAll() {
        List<部門マスタ> departmentEntities = departmentMapper.selectAll();
        return new DepartmentList(departmentEntities.stream()
                .map(departmentEntityMapper::mapToDomainModel)
                .toList());
    }

    @Override
    public void save(Department department) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null && authentication.getName() != null ? authentication.getName() : "system";

        部門マスタKey departmentKey = new 部門マスタKey();
        departmentKey.set部門コード(department.getDepartmentId().getDeptCode().getValue());
        departmentKey.set開始日(department.getDepartmentId().getDepartmentStartDate().getValue());
        Optional<部門マスタ> departmentEntity = Optional.ofNullable(departmentMapper.selectByPrimaryKey(departmentKey));
        if (departmentEntity.isEmpty()) {
            部門マスタ newDepartmentEntity = departmentEntityMapper.mapToEntity(department);

            newDepartmentEntity.set作成日時(LocalDateTime.now());
            newDepartmentEntity.set作成者名(username);
            newDepartmentEntity.set更新日時(LocalDateTime.now());
            newDepartmentEntity.set更新者名(username);
            departmentMapper.insert(newDepartmentEntity);
        } else {
            部門マスタ updateDepartmentEntity = departmentEntityMapper.mapToEntity(department);
            updateDepartmentEntity.set作成日時(departmentEntity.get().get作成日時());
            updateDepartmentEntity.set作成者名(departmentEntity.get().get作成者名());
            updateDepartmentEntity.set更新日時(LocalDateTime.now());
            updateDepartmentEntity.set更新者名(username);
            departmentMapper.updateByPrimaryKey(updateDepartmentEntity);
        }
    }

    @Override
    public void deleteById(DepartmentId departmentId) {
        部門マスタKey departmentKey = new 部門マスタKey();
        departmentKey.set部門コード(departmentId.getDeptCode().getValue());
        departmentKey.set開始日(departmentId.getDepartmentStartDate().getValue());
        departmentMapper.deleteByPrimaryKey(departmentKey);
    }

    @Override
    public void deleteAll() {
        departmentMapper.deleteAll();
    }
}

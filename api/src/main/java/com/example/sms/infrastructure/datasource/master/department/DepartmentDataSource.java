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
        departmentKey.set部門コード(departmentId.getDeptCode());
        departmentKey.set開始日(departmentId.getStartDate());
        部門マスタ departmentEntity = departmentMapper.selectByPrimaryKey(departmentKey);
        if (departmentEntity != null) {
            return Optional.of(departmentEntityMapper.mapToDomainEntity(departmentEntity));
        }
        return Optional.empty();
    }

    @Override
    public DepartmentList selectAll() {
        List<部門マスタ> departmentEntities = departmentMapper.selectAll();
        return new DepartmentList(departmentEntities.stream()
                .map(departmentEntityMapper::mapToDomainEntity)
                .toList());
    }

    @Override
    public void save(Department department) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null && authentication.getName() != null ? authentication.getName() : "system";

        部門マスタKey departmentKey = new 部門マスタKey();
        departmentKey.set部門コード(department.getDepartmentId().getDeptCode());
        departmentKey.set開始日(department.getDepartmentId().getStartDate());
        部門マスタ departmentEntity = departmentEntityMapper.mapToEntity(department);

        departmentEntity.set作成日時(LocalDateTime.now());
        departmentEntity.set作成者名(username);
        departmentEntity.set更新日時(LocalDateTime.now());
        departmentEntity.set更新者名(username);
        departmentMapper.insert(departmentEntity);
    }

    @Override
    public void update(Department department) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null && authentication.getName() != null ? authentication.getName() : "system";

        部門マスタKey departmentKey = new 部門マスタKey();
        departmentKey.set部門コード(department.getDepartmentId().getDeptCode());
        departmentKey.set開始日(department.getDepartmentId().getStartDate());
        Optional<部門マスタ> departmentEntity = Optional.ofNullable(departmentMapper.selectByPrimaryKey(departmentKey));
        if (departmentEntity.isEmpty()) {
            return;
        }

        部門マスタ updateDepartmentEntity = departmentEntityMapper.mapToEntity(department);
        updateDepartmentEntity.set作成日時(departmentEntity.get().get作成日時());
        updateDepartmentEntity.set作成者名(departmentEntity.get().get作成者名());
        updateDepartmentEntity.set更新日時(LocalDateTime.now());
        updateDepartmentEntity.set更新者名(username);
        departmentMapper.updateByPrimaryKey(updateDepartmentEntity);
    }

    @Override
    public void deleteById(DepartmentId departmentId) {
        部門マスタKey departmentKey = new 部門マスタKey();
        departmentKey.set部門コード(departmentId.getDeptCode());
        departmentKey.set開始日(departmentId.getStartDate());
        departmentMapper.deleteByPrimaryKey(departmentKey);
    }

    @Override
    public void deleteAll() {
        departmentMapper.deleteAll();
    }
}

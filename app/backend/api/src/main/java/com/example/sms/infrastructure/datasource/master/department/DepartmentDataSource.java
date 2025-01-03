package com.example.sms.infrastructure.datasource.master.department;

import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.department.DepartmentId;
import com.example.sms.domain.model.master.department.DepartmentList;
import com.example.sms.infrastructure.PageInfoHelper;
import com.example.sms.infrastructure.datasource.autogen.mapper.部門マスタMapper;
import com.example.sms.infrastructure.datasource.autogen.model.部門マスタ;
import com.example.sms.infrastructure.datasource.autogen.model.部門マスタKey;
import com.example.sms.service.master.department.DepartmentCriteria;
import com.example.sms.service.master.department.DepartmentRepository;
import com.github.pagehelper.PageInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class DepartmentDataSource implements DepartmentRepository {
    final 部門マスタMapper departmentMapper;
    final DepartmentCustomMapper departmentCustomMapper;
    final DepartmentEntityMapper departmentEntityMapper;

    public DepartmentDataSource(部門マスタMapper departmentMapper, DepartmentCustomMapper departmentCustomMapper, DepartmentEntityMapper departmentEntityMapper) {
        this.departmentMapper = departmentMapper;
        this.departmentCustomMapper = departmentCustomMapper;
        this.departmentEntityMapper = departmentEntityMapper;
    }

    @Override
    public Optional<Department> findById(DepartmentId departmentId) {
        部門マスタKey departmentKey = new 部門マスタKey();
        departmentKey.set部門コード(departmentId.getDeptCode().getValue());
        departmentKey.set開始日(departmentId.getDepartmentStartDate().getValue());
        DepartmentCustomEntity departmentEntity = departmentCustomMapper.selectByPrimaryKey(departmentKey);
        if (departmentEntity != null) {
            return Optional.of(departmentEntityMapper.mapToDomainModel(departmentEntity));
        }
        return Optional.empty();
    }

    @Override
    public DepartmentList findByCode(String departmentCode) {
        List<DepartmentCustomEntity> departmentEntities = departmentCustomMapper.selectByDepartmentCode(departmentCode);
        return new DepartmentList(departmentEntities.stream()
                .map(departmentEntityMapper::mapToDomainModel)
                .toList());
    }

    @Override
    public DepartmentList selectAll() {
        List<DepartmentCustomEntity> departmentEntities = departmentCustomMapper.selectAll();
        return new DepartmentList(departmentEntities.stream()
                .map(departmentEntityMapper::mapToDomainModel)
                .toList());
    }

    @Override
    public PageInfo<Department> selectAllWithPageInfo() {
        List<DepartmentCustomEntity> departmentEntities = departmentCustomMapper.selectAll();
        PageInfo<DepartmentCustomEntity> pageInfo = new PageInfo<>(departmentEntities);

        return PageInfoHelper.of(pageInfo, departmentEntityMapper::mapToDomainModel);
    }

    @Override
    public void save(Department department) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null && authentication.getName() != null ? authentication.getName() : "system";

        部門マスタKey departmentKey = new 部門マスタKey();
        departmentKey.set部門コード(department.getDepartmentId().getDeptCode().getValue());
        departmentKey.set開始日(department.getDepartmentId().getDepartmentStartDate().getValue());
        Optional<DepartmentCustomEntity> departmentEntity = Optional.ofNullable(departmentCustomMapper.selectByPrimaryKey(departmentKey));
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
        departmentCustomMapper.deleteAll();
    }

    @Override
    public PageInfo<Department> searchWithPageInfo(DepartmentCriteria criteria) {
        List<DepartmentCustomEntity> departmentEntities = departmentCustomMapper.selectByCriteria(criteria);
        PageInfo<DepartmentCustomEntity> pageInfo = new PageInfo<>(departmentEntities);
        return PageInfoHelper.of(pageInfo, departmentEntityMapper::mapToDomainModel);
    }
}

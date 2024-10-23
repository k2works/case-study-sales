package com.example.sms.service.master.department;

import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.department.DepartmentId;
import com.example.sms.domain.model.master.department.DepartmentList;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 部門サービス
 */
@Service
@Transactional
public class DepartmentService {
    final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    /**
     * 部門一覧
     */
    public DepartmentList selectAll() {
        return departmentRepository.selectAll();
    }

    public PageInfo<Department> selectAllWithPageInfo() {
        return departmentRepository.selectAllWithPageInfo();
    }
    /**
     * 部門新規登録
     */
    public void register(Department department) {
        departmentRepository.save(department);
    }

    /**
     * 部門情報編集
     */
    public void save(Department department) {
        departmentRepository.save(department);
    }

    /**
     * 部門削除
     */
    public void delete(DepartmentId departmentId) {
        departmentRepository.deleteById(departmentId);
    }

    /**
     * 部門検索
     */
    public Department find(DepartmentId departmentId) {
        return departmentRepository.findById(departmentId).orElse(null);
    }

}

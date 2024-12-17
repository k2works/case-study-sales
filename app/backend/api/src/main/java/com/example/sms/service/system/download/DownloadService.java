package com.example.sms.service.system.download;

import com.example.sms.domain.model.master.department.DepartmentList;
import com.example.sms.domain.model.master.employee.EmployeeList;
import com.example.sms.domain.model.system.download.DownloadCondition;
import com.example.sms.infrastructure.datasource.master.department.DepartmentDownloadCSV;
import com.example.sms.infrastructure.datasource.master.employee.EmployeeDownloadCSV;
import org.springframework.stereotype.Service;

import java.io.OutputStreamWriter;
import java.util.List;

import static com.example.sms.infrastructure.Pattern2WriteCSVUtil.writeCsv;

/**
 * データダウンロードサービス
 */
@Service
public class DownloadService {
    private final DepartmentCSVRepository departmentCSVRepository;
    private final EmployeeCSVRepository employeeCSVRepository;

    public DownloadService(DepartmentCSVRepository departmentCSVRepository, EmployeeCSVRepository employeeCSVRepository) {
        this.departmentCSVRepository = departmentCSVRepository;
        this.employeeCSVRepository = employeeCSVRepository;
    }

    /**
     * ダウンロード件数取得
     */
    public int count(DownloadCondition condition) {
        return switch (condition.getTarget()) {
            case DEPARTMENT -> countDepartments(condition);
            case EMPLOYEE -> countEmployees(condition);
        };
    }

    /**
     * ダウンロード変換
     */
    public <T> List<T> convert(DownloadCondition condition) {
        return switch (condition.getTarget()) {
            case DEPARTMENT -> (List<T>) downloadDepartments(condition);
            case EMPLOYEE -> (List<T>) downloadEmployees(condition);
        };
    }

    /**
     * ダウンロード
     */
    public void download(OutputStreamWriter streamWriter, DownloadCondition condition) throws Exception {
        switch (condition.getTarget()) {
            case DEPARTMENT -> writeCsv(DepartmentDownloadCSV.class).accept(streamWriter, convert(condition));
            case EMPLOYEE -> writeCsv(EmployeeDownloadCSV.class).accept(streamWriter, convert(condition));
        };
    }

    /**
     * 部門ダウンロード件数取得
     */
    private int countDepartments(DownloadCondition condition) {
        return departmentCSVRepository.countBy(condition);
    }

    /**
     * 社員ダウンロード件数取得
     */
    private int countEmployees(DownloadCondition condition) {
        return employeeCSVRepository.countBy(condition);
    }

    /**
     * 部門ダウンロード
     */
    private List<DepartmentDownloadCSV> downloadDepartments(DownloadCondition condition) {
        DepartmentList departmentList = departmentCSVRepository.selectBy(condition);
        return departmentCSVRepository.convert(departmentList);
    }

    /**
     * 社員ダウンロード
     */
    private List<EmployeeDownloadCSV> downloadEmployees(DownloadCondition condition) {
        EmployeeList employeeList = employeeCSVRepository.selectBy(condition);
        return employeeCSVRepository.convert(employeeList);
    }

}

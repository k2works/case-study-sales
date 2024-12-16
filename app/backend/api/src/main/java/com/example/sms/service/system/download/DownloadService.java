package com.example.sms.service.system.download;

import com.example.sms.domain.model.master.department.DepartmentList;
import com.example.sms.domain.model.system.download.DownloadCondition;
import com.example.sms.infrastructure.datasource.master.department.DepartmentDownloadCSV;
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

    public DownloadService(DepartmentCSVRepository departmentCSVRepository) {
        this.departmentCSVRepository = departmentCSVRepository;
    }

    /**
     * ダウンロード件数取得
     */
    public int count(DownloadCondition condition) {
        return switch (condition.getTarget()) {
            case DEPARTMENT -> countDepartments(condition);
        };
    }

    /**
     * ダウンロード変換
     */
    public <T> List<T> convert(DownloadCondition condition) {
        return switch (condition.getTarget()) {
            case DEPARTMENT -> (List<T>) downloadDepartments(condition);
        };
    }

    /**
     * ダウンロード
     */
    public void download(OutputStreamWriter streamWriter, DownloadCondition condition) throws Exception {
        switch (condition.getTarget()) {
            case DEPARTMENT -> writeCsv(DepartmentDownloadCSV.class).accept(streamWriter, convert(condition));
        };
    }

    /**
     * 部門ダウンロード件数取得
     */
    private int countDepartments(DownloadCondition condition) {
        return departmentCSVRepository.countBy(condition);
    }

    /**
     * 部門ダウンロード
     */
    private List<DepartmentDownloadCSV> downloadDepartments(DownloadCondition condition) {
        DepartmentList departmentList = departmentCSVRepository.selectBy(condition);
        return departmentCSVRepository.convert(departmentList);
    }
}

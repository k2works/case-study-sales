package com.example.sms.infrastructure.datasource.system.audit;

import com.example.sms.service.system.audit.AuditSearchCondition;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import java.util.List;

@Mapper
public interface ApplicationExecutionHistoryCustomMapper {
    ApplicationExecutionHistoryCustomEntity selectByPrimaryKey(Integer id);

    @Delete("DELETE FROM system.application_execution_history")
    void deleteAll();

    List<ApplicationExecutionHistoryCustomEntity> selectAll();

    @Insert({
            "insert into system.application_execution_history (process_name, process_code,",
            "process_type, process_start, process_end,",
            "process_flag, process_details, version,",
            "user_id)",
            "values (#{processName,jdbcType=VARCHAR}, #{processCode,jdbcType=VARCHAR},",
            "#{processType,jdbcType=VARCHAR}, #{processStart,jdbcType=TIMESTAMP}, #{processEnd,jdbcType=TIMESTAMP},",
            "#{processFlag,jdbcType=INTEGER}, #{processDetails,jdbcType=VARCHAR}, #{version,jdbcType=INTEGER},",
            "#{userId,jdbcType=VARCHAR})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertForStart(ApplicationExecutionHistoryCustomEntity record);

    List<ApplicationExecutionHistoryCustomEntity> selectByCondition(AuditSearchCondition condition);
}

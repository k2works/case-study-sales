package com.example.sms.infrastructure.datasource.system.audit;

import com.example.sms.service.system.audit.AuditSearchCondition;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import java.util.List;

@Mapper
public interface ApplicationExecutionHistoryMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system.application_execution_history
     *
     * @mbg.generated Sat Dec 07 15:41:15 JST 2024
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system.application_execution_history
     *
     * @mbg.generated Sat Dec 07 15:41:15 JST 2024
     */
    int insert(ApplicationExecutionHistoryEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system.application_execution_history
     *
     * @mbg.generated Sat Dec 07 15:41:15 JST 2024
     */
    int insertSelective(ApplicationExecutionHistoryEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system.application_execution_history
     *
     * @mbg.generated Sat Dec 07 15:41:15 JST 2024
     */
    ApplicationExecutionHistoryEntity selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system.application_execution_history
     *
     * @mbg.generated Sat Dec 07 15:41:15 JST 2024
     */
    int updateByPrimaryKeySelective(ApplicationExecutionHistoryEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system.application_execution_history
     *
     * @mbg.generated Sat Dec 07 15:41:15 JST 2024
     */
    int updateByPrimaryKey(ApplicationExecutionHistoryEntity record);

    @Delete("DELETE FROM system.application_execution_history")
    void deleteAll();

    List<ApplicationExecutionHistoryEntity> selectAll();

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
    void insertForStart(ApplicationExecutionHistoryEntity record);

    List<ApplicationExecutionHistoryEntity> selectByCondition(AuditSearchCondition condition);
}
package com.example.sms.infrastructure.datasource.autogen.mapper;

import com.example.sms.infrastructure.datasource.autogen.model.ApplicationExecutionHistory;

public interface ApplicationExecutionHistoryMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system.application_execution_history
     *
     * @mbg.generated Thu Dec 19 20:11:17 JST 2024
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system.application_execution_history
     *
     * @mbg.generated Thu Dec 19 20:11:17 JST 2024
     */
    int insert(ApplicationExecutionHistory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system.application_execution_history
     *
     * @mbg.generated Thu Dec 19 20:11:17 JST 2024
     */
    int insertSelective(ApplicationExecutionHistory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system.application_execution_history
     *
     * @mbg.generated Thu Dec 19 20:11:17 JST 2024
     */
    ApplicationExecutionHistory selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system.application_execution_history
     *
     * @mbg.generated Thu Dec 19 20:11:17 JST 2024
     */
    int updateByPrimaryKeySelective(ApplicationExecutionHistory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system.application_execution_history
     *
     * @mbg.generated Thu Dec 19 20:11:17 JST 2024
     */
    int updateByPrimaryKey(ApplicationExecutionHistory record);
}
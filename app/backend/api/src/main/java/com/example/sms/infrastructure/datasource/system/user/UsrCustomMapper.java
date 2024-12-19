package com.example.sms.infrastructure.datasource.system.user;

import com.example.sms.infrastructure.datasource.autogen.model.Usr;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UsrCustomMapper {
    List<Usr> selectAll();

    @Select("DELETE FROM system.usr")
    void deleteAll();
}

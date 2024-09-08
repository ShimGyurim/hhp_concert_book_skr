package com.helloworld.quickstart.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;

@Mapper
public interface QuickMapper {
//    @Select("SELECT ID, NAME FROM ITEM WHERE id = #{id}")
    HashMap<String, Object> findById(HashMap<String, Object> paramMap);
}

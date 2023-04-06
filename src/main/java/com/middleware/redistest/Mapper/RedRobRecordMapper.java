package com.middleware.redistest.Mapper;

import com.middleware.redistest.entity.RedRobRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface RedRobRecordMapper {
    int deleteByPrimaryKey(Integer id);
    //根据主键id删除
    int insert(RedRobRecord record);                          //插入数据记录
    int insertSelective(RedRobRecord record);                 //插入数据记录
    RedRobRecord selectByPrimaryKey(Integer id);              //根据主键id查询记录
    int updateByPrimaryKeySelective(RedRobRecord record);     //更新数据记录
    int updateByPrimaryKey(RedRobRecord record);              //更新数据记录
}

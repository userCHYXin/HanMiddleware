package com.middleware.redistest.Mapper;

import com.middleware.redistest.entity.RedRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface RedRecordMapper {
    int deleteByPrimaryKey(Integer id);                       //根据主键id删除
    int insert(RedRecord record);                             //插入数据记录
    int insertSelective(RedRecord record);                    //插入数据记录
    RedRecord selectByPrimaryKey(Integer id);                 //根据主键id查询记录
    int updateByPrimaryKeySelective(RedRecord record);        //更新数据记录
    int updateByPrimaryKey(RedRecord record);                 //更新数据记录
}

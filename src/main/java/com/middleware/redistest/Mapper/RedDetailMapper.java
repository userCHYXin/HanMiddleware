package com.middleware.redistest.Mapper;

import com.middleware.redistest.entity.RedDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface RedDetailMapper {
    int deleteByPrimaryKey(Integer id);                         //根据主键id删除
    int insert(RedDetail record);                               //插入数据记录
    int insertSelective(RedDetail record);                      //插入数据记录
    RedDetail selectByPrimaryKey(Integer id);                   //根据主键id查询记录
    int updateByPrimaryKeySelective(RedDetail record);          //更新数据记录
    int updateByPrimaryKey(RedDetail record);                   //更新数据记录”
}

package com.gmy.sky2.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gmy.sky2.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * @Author guomaoyang
 * @Date 2020/8/18
 */
public interface UserMapper extends BaseMapper<User> {
    //User findOne(@Param("id") Long id);
}

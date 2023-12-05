package com.example.springsecurity.mapper;

import com.example.springsecurity.model.MyUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("""
    select * from my_users where username=#{username}
""")
    public MyUser findByUsername(String username);


    @Select("""
    select roles from my_users where username=#{username}
""")
    public String findRoleByUsername(String username);
}

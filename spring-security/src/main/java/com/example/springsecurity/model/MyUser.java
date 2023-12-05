package com.example.springsecurity.model;

import com.example.springsecurity.mapper.UserMapper;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @TableName my_users
 */
@Data
public class MyUser implements Serializable {
    private Integer id;

    private String username;

    private String password;

    private Integer enabled;

    private String roles;


    @Serial
    private static final long serialVersionUID = 1L;


}
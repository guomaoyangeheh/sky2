package com.gmy.sky2.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @Author guomaoyang
 * @Date 2020/8/18
 */
@Data
@ToString
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String email;
    private Integer account;
}
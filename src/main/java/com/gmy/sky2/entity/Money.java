package com.gmy.sky2.entity;

import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.ToString;

/**
 * @Author guomaoyang
 * @Date 2020/8/18
 */
@Data
@ToString
public class Money {
    private Long id;
    private Long userId;
    private Integer money;
    @Version
    private Integer version;
}
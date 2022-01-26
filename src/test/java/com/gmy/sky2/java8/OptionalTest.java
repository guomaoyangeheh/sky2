package com.gmy.sky2.java8;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

/**
 * @Author guomaoyang
 * @Date 2021/8/24
 */
public class OptionalTest {

    /**
     * optional.isPresent() 判断是否存在值
     * optional.orElse 如果存在则返回值，负责返回给定值
     */
    @Test
    public void test1(){
        Optional<String> optional = Optional.ofNullable("123");
       /* System.out.println("是否存在："+optional.isPresent());
        System.out.println(optional.orElse("1345"));*/
        optional.ifPresent(value -> System.out.println("value非空："+ value));
        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        Collections.reverse(integers);
        System.out.println(integers);


    }
}

package com.gmy.sky2.map;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author guomaoyang
 * @Date 2021/4/23
 */
public class MapTest {

    @Test
    void test2(){
        List<Integer> integers = new ArrayList<>();
        for (int i = 1; i <= 1500; i++) {
            integers.add(i);
        }
        int throld = 30;
        for (int i = 0; i < 50; i++) {
            if(i < 50-1){

                List<Integer> list = integers.subList(i * throld, (i + 1) * throld);
                System.out.println(list);
            }else {
                List<Integer> list = integers.subList(i * throld, integers.size());
                System.out.println(list);
            }
        }
    }

    @Test
    void test1(){
        LinkedHashMap<String, Integer> linkedHashMap = new LinkedHashMap<>(4);
        ConcurrentHashMap<Object, Object> concurrentHashMap = new ConcurrentHashMap<>();
        concurrentHashMap.put("key","value");
        linkedHashMap.put("gmy",12);
        linkedHashMap.put("lilei",13);
        linkedHashMap.put("xiaoming",14);

    }
}

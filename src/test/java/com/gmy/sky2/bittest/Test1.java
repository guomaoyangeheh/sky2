package com.gmy.sky2.bittest;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;

/**
 * @Author guomaoyang
 * @Date 2021/12/3
 */
public class Test1 {

    @Test
    public void test5(){
        System.out.println(convertDayToUserMonth(0));
        System.out.println(convertDayToUserMonth(11));
        System.out.println(convertDayToUserMonth(30));
        System.out.println(convertDayToUserMonth(31));

    }

    private Integer convertDayToUserMonth(Integer days){
        int i = days / 30;
        if(days % 30 != 0){
            return i+1;
        }
        return i;
    }
    @Test
    public void test4(){
        System.out.println(DateUtil.between(DateUtil.parseDateTime("2021-04-28 20:13:50"), DateUtil.parseDateTime("2022-04-27 23:59:59"), DateUnit.DAY));

    }

    @Test
    public void test3(){
        System.out.println(convertDayToValidMonth(11));
        System.out.println(convertDayToValidMonth(77));
        System.out.println(convertDayToValidMonth(93));
        System.out.println(convertDayToValidMonth(340));

    }
    private static Integer convertDayToValidMonth(Integer days){
        // 四舍五入
        BigDecimal bigDecimal = new BigDecimal(days).divide(new BigDecimal(30),0, RoundingMode.HALF_UP);
        return bigDecimal.intValue();
    }
    @Test
    public void test2(){
        HashSet<String> set = new HashSet<>();
        String account1 = "账户1";
        String account2 = "账户1";
        set.add(account1);
        set.add(new String("账户1"));
        System.out.println(set);

    }


    @Test
    public void test1(){
        int i1 = 1;
        int i2 = 2;
        int i3 = 4;
        System.out.println(3 & i1);
        System.out.println(3 & i2);
        System.out.println(3 & i3);

        System.out.println("-----");

        System.out.println(3 ^ i1);
        System.out.println(3 ^ i2);
        System.out.println(3 ^ i3);

        System.out.println("-----");

        System.out.println(1 | i2);
        System.out.println(3 | i3);


    }
}

package com.gmy.sky2.mytest;

/**
 * @Author guomaoyang
 * @Date 2021/4/23
 */
public class Son extends Father{

    private SonSupport sonSupport = new SonSupport();

    static {
        System.out.println("执行子类静态代码块");
    }
}

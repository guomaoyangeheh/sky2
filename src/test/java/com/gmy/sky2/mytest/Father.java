package com.gmy.sky2.mytest;

/**
 * @Author guomaoyang
 * @Date 2021/4/23
 */
public class Father {
    private FatherSupport fatherSupport = new FatherSupport();
    static {
        System.out.println("执行父类静态代码块");
    }
}

package com.gmy.sky2.mytest;

/**
 * @Author guomaoyang
 * @Date 2021/6/29
 */
public class TestVO {
    private int num ;

    public TestVO(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "TestVO{" +
                "num=" + num +
                '}';
    }
}

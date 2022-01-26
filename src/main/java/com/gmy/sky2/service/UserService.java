package com.gmy.sky2.service;

/**
 * @Author guomaoyang
 * @Date 2021/4/8
 */
public interface UserService {
    void deductBalanceDefault(Long id ,Integer money);
    void deductBalance(Long id ,Integer money);

    void deductBalanceNest(Long id ,Integer money);

    void queryCycle();
}

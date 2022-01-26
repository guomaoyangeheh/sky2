package com.gmy.sky2.service;

/**
 * @Author guomaoyang
 * @Date 2021/4/8
 */
public interface MoneyService {
    void deductMoneyDefault(Long moneyId,Integer money);
    void deductMoney(Long moneyId,Integer money);

    void deductMoneyNest(Long moneyId,Integer money);
    void queryCycle();
    String optUpdate();
}

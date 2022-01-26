package com.gmy.sky2.service.impl;

import cn.hutool.core.date.DateUtil;
import com.gmy.sky2.entity.Money;
import com.gmy.sky2.entity.User;
import com.gmy.sky2.helper.TransactionHelper;
import com.gmy.sky2.mapper.MoneyMapper;
import com.gmy.sky2.mapper.UserMapper;
import com.gmy.sky2.service.MoneyService;
import com.gmy.sky2.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Wrapper;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @Author guomaoyang
 * @Date 2021/4/8
 */
@Service
@Slf4j
public class MoneyServiceImpl implements MoneyService {
    @Autowired
    private MoneyMapper moneyMapper;
    @Autowired
    private RedissonClient redisson;
    @Autowired
    private TransactionHelper transactionHelper;


    @Override
    @Transactional
    public void deductMoneyDefault(Long moneyId, Integer money) {
        Money money1 = moneyMapper.selectById(moneyId);
        money1.setMoney(money1.getMoney()-money);
        moneyMapper.updateById(money1);
        throw new RuntimeException();
    }

    /**
     * 外部如果有事务则挂起外部事务，自己新开一个事务，执行完毕会直接提交
     * 自己有异常如果不抛出到外层事务则不会影响外部事务。
     * @param moneyId
     * @param money
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deductMoney(Long moneyId, Integer money) {
        Money money1 = moneyMapper.selectById(moneyId);
        money1.setMoney(money1.getMoney()-money);
        moneyMapper.updateById(money1);
    }

    /**
     * 嵌套事务规则
     * 其实在mysql层面就是开启了一个事物
     * 1.外部事务异常回滚，子事务会一起回滚
     * 2.子事务异常回滚，如果异常没有到外部事务，则外部事务会提交，子事务会回滚
     */
    @Override
    @Transactional(propagation = Propagation.NESTED)
    public void deductMoneyNest(Long moneyId, Integer money) {
        Money money1 = moneyMapper.selectById(moneyId);
        money1.setMoney(money1.getMoney()-money);
        moneyMapper.updateById(money1);
        throw new RuntimeException();
    }

    @Override
    public void queryCycle() {
        RLock myLock = redisson.getLock("myLock2");
        //myLock.lock(10, TimeUnit.SECONDS);
        boolean lockFlag = false;
        //myLock.lock(10,TimeUnit.SECONDS);
        try {
            log.info("线程:{}尝试获取锁，当前时间:{}",Thread.currentThread().getId(), DateUtil.format(LocalDateTime.now(),"yyyy-MM-dd HH:mm:ss.SSS"));
            lockFlag = myLock.tryLock(50,3, TimeUnit.SECONDS);
        }catch (Exception e){
            log.error("获取分布式锁出错....");
        }
        if(lockFlag){
            try{
                Thread.sleep(2000);
            }catch (Exception e){
                log.error("执行出错",e);
            }finally {
                myLock.unlock();
                log.info("线程:{}解锁成功，当前时间:{}",Thread.currentThread().getId(), DateUtil.format(LocalDateTime.now(),"yyyy-MM-dd HH:mm:ss.SSS"));
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("1".substring(0, 3));
    }
    @Override
    public String optUpdate() {
        return loopCall(()->{
            Money money = moneyMapper.selectById(1L);
            money.setMoney(money.getMoney()+10);
            int i = moneyMapper.updateById(money);
            if(i == 0){
                throw new RuntimeException("数据被占用");
            }
            return "处理成功！";
        });
    }

    private  String loopCall(Supplier<String> supplier) {
        int cycles = 3;
        for (int i = 0; i < cycles; i++) {
            try {
                return transactionHelper.propagationWithNew(supplier);
            }catch (Exception e){
                log.warn("乐观锁更新失败 执行批次:第{}次",i+1,e);
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.warn("重试{}次后，方法仍未调用成功!",cycles);
        return "数据被占用，请稍后重试";
    }
}

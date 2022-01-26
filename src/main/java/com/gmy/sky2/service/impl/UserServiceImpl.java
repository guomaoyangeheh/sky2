package com.gmy.sky2.service.impl;

import cn.hutool.core.date.DateUtil;
import com.gmy.sky2.entity.User;
import com.gmy.sky2.mapper.UserMapper;
import com.gmy.sky2.service.MoneyService;
import com.gmy.sky2.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @Author guomaoyang
 * @Date 2021/4/8
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MoneyService moneyService;
    private ThreadLocal<String> threadLocal = new ThreadLocal<>();
    @Autowired
    private RedissonClient redisson;

    @Override
    @Transactional
    public void deductBalanceDefault(Long id, Integer money) {
        User user = userMapper.selectById(id);
        user.setAccount(user.getAccount()-money);
        userMapper.updateById(user);
        try {
            moneyService.deductMoneyDefault(1L,10);
        }catch (Exception e){
            log.error("",e);
        }
    }

    @Override
    @Transactional
    public void deductBalance(Long id, Integer money) {
        User user = userMapper.selectById(id);
        user.setAccount(user.getAccount()-money);
        userMapper.updateById(user);
        moneyService.deductMoney(1L,10);

    }


    @Override
    @Transactional
    public void deductBalanceNest(Long id, Integer money) {
        User user = userMapper.selectById(id);
        user.setAccount(user.getAccount()-money);
        userMapper.updateById(user);
        try {
            moneyService.deductMoneyNest(1L,10);
        }catch (Exception e){
            log.error("",e);
        }
    }

    @Override
    public void queryCycle() {
        RLock myLock = redisson.getLock("myLock");
        //myLock.lock(10, TimeUnit.SECONDS);
        //myLock.lockInterruptibly();
        boolean lockFlag = false;
        //myLock.lock(10,TimeUnit.SECONDS);
        try {
            log.info("线程:{}尝试获取锁，当前时间:{}",Thread.currentThread().getId(), DateUtil.format(LocalDateTime.now(),"yyyy-MM-dd HH:mm:ss.SSS"));
           lockFlag = myLock.tryLock(5000,3000,TimeUnit.MILLISECONDS);
        }catch (Exception e){
            log.error("获取分布式锁出错....");
        }
        if(lockFlag){
            log.info("线程:{}获取锁成功获取锁，当前时间:{}",Thread.currentThread().getId(), DateUtil.format(LocalDateTime.now(),"yyyy-MM-dd HH:mm:ss.SSS"));
            try {
                //Thread.sleep(2000);
                moneyService.queryCycle();
            }catch (Exception e){
                log.error("出错",e);
            }finally {
                try {
                    myLock.unlock();
                    log.info("线程:{}释放锁，当前时间:{}",Thread.currentThread().getId(), DateUtil.format(LocalDateTime.now(),"yyyy-MM-dd HH:mm:ss.SSS"));
                }catch (Exception e){
                    log.error("线程:{}释放锁失败",Thread.currentThread().getId(),e);

                }
            }
        }else {
            log.info("线程:{}获取锁成功失败，当前时间:{}",Thread.currentThread().getId(), DateUtil.format(LocalDateTime.now(),"yyyy-MM-dd HH:mm:ss.SSS"));
            log.warn("数据被占用");
        }
    }
}

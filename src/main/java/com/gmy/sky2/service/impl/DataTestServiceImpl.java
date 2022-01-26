package com.gmy.sky2.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gmy.sky2.entity.DataTest;
import com.gmy.sky2.mapper.DataTestMapper;
import com.gmy.sky2.service.DataTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * @Author guomaoyang
 * @Date 2021/5/27
 */
@Service
@Slf4j
public class DataTestServiceImpl extends ServiceImpl<DataTestMapper,DataTest> implements DataTestService {

    @Override
    public Long insertData(Integer num) {
        log.info("开启插入数据{}条",num);
        long l = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            DataTest dataTest = new DataTest();
            dataTest.setName("test" + num);
            this.save(dataTest);
        }
        log.info("插入数据成功");
        return System.currentTimeMillis() - l;


    }

    @Override
    public Long batchInsertData(Integer num) {
        log.info("开启批量插入数据{}条",num);
        long l = System.currentTimeMillis();
        ArrayList<DataTest> dataTests = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            DataTest dataTest = new DataTest();
            dataTest.setName("test" + num);
            dataTests.add(dataTest);
        }
        saveBatch(dataTests);
        log.info("批量插入数据成功");
        return System.currentTimeMillis() - l;
    }
}

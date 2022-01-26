package com.gmy.sky2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gmy.sky2.entity.DataTest;
import org.springframework.stereotype.Service;

/**
 * @Author guomaoyang
 * @Date 2021/5/27
 */
public interface DataTestService extends IService<DataTest>{
    Long insertData(Integer num);
    Long batchInsertData(Integer num);
}

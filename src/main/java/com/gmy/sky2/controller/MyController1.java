package com.gmy.sky2.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gmy.sky2.entity.User;
import com.gmy.sky2.mapper.UserMapper;
import com.gmy.sky2.service.DataTestService;
import com.gmy.sky2.service.UserService;
import com.gmy.sky2.testdata.DemoDataListener;
import com.gmy.sky2.testdata.ExcelData;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @Author guomaoyang
 * @Date 2021/4/7
 */
@Component
@Slf4j
@RestController
public class MyController1 {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private DataTestService dataTestService;

    private ThreadLocal<String> threadLocal = new ThreadLocal<>();

    @GetMapping("/test1")
    public String test1(){
        return "123";
    }

    @GetMapping("/test2")
    @ResponseBody
    public Object test2(){
        User user = userMapper.selectById(1L);
        return user;
    }

    @GetMapping("/default")
    public String default1(){
        userService.deductBalanceDefault(1L,10);
        return "123";
    }
    @GetMapping("/new")
    public String new1(){
        userService.deductBalance(1L,10);
        return "123";
    }

    @GetMapping("/nested")
    public String nested(){
        userService.deductBalanceNest(1L,10);
        return "123";
    }

    @GetMapping("/queryCycle")
    public String queryCycle(){
        threadLocal.set("123");
        userService.queryCycle();
        log.info("threadLocal1=========={}",threadLocal.get());
        return "123";
    }

    @GetMapping("/insertData/{num}")
    public String insertData(@PathVariable Integer num){
        Long aLong = dataTestService.insertData(num);
        String str = "执行插入数据%s条，耗时%sms";
        return String.format(str,num,aLong);
    }

    @GetMapping("/batchInsertData/{num}")
    public String batchInsertData(@PathVariable Integer num){
        Long aLong = dataTestService.batchInsertData(num);
        String str = "执行批量插入数据%s条，耗时%sms";
        return String.format(str,num,aLong);
    }

    @PostMapping("upload")
    @ResponseBody
    public String upload(MultipartFile file) throws IOException {
       /* try {
            EasyExcel.read(file.getInputStream(), ExcelData.class, new DemoDataListener()).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        List<ExcelData> excelData = reader.readAll(ExcelData.class);
        log.info(new Gson().toJson(excelData));
        return "success";
    }

}

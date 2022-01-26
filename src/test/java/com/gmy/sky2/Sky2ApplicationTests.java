package com.gmy.sky2;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.excel.EasyExcel;
import com.gmy.sky2.service.MoneyService;
import com.gmy.sky2.testdata.DemoDataListener;
import com.gmy.sky2.testdata.ExcelData;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.List;

@SpringBootTest
class Sky2ApplicationTests {
    @Autowired
    private MoneyService moneyService;

    @Test
    void contextLoads() {
        File file = new File("/Users/mac/Documents/测试Excel.xlsx");
        //EasyExcel.read("/Users/mac/Documents/测试Excel.xlsx", ExcelData.class,new DemoDataListener()).sheet().doRead();
        ExcelReader reader = ExcelUtil.getReader("/Users/mac/Documents/测试Excel.xlsx");
        List<ExcelData> excelData = reader.readAll(ExcelData.class);
        System.out.println(new Gson().toJson(excelData));
    }
    @Test
    void optTest(){
        System.out.println(moneyService.optUpdate());
    }

}

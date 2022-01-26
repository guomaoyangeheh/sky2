package com.gmy.sky2.testdata;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 模板的读取类
 *
 * @author Jiaju Zhuang
 */
@Slf4j
// 有个很重要的点 ExcelDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
public class DemoDataListener extends AnalysisEventListener<ExcelData> {

    private static final Gson gson = new Gson();

    @Override
    public void invoke(ExcelData excelData, AnalysisContext analysisContext) {
        log.info("读取到一行数据:{}",gson.toJson(excelData));


    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("所有数据读取完成...");
    }
}
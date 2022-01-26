package com.gmy.sky2.sqlscript.vo;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @Author guomaoyang
 * @Date 2021/11/12
 */
public class DeadLineVO {

    private String grantNo;
    private String deadline;

    public String getGrantNo() {
        return trim(grantNo);
    }

    public void setGrantNo(String grantNo) {
        this.grantNo = grantNo;
    }

    public String getDeadline() {
        DateTime dateTime = DateUtil.parseDate(deadline);
        DateTime endOfDay = DateUtil.endOfDay(dateTime);

        return DateUtil.format(endOfDay, DatePattern.NORM_DATETIME_PATTERN);
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    private String trim(String value){
        return StringUtils.trim(value);
    }

}

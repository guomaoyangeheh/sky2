package com.gmy.sky2.sqlscript.vo;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author guomaoyang
 * @Date 2021/12/13
 */
public class VipVO1 {
    private String partnerName;
    private String endTime;

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = trim(partnerName);
    }

    public String getEndTime() {
        DateTime dateTime = DateUtil.parseDate(endTime);
        DateTime endOfDay = DateUtil.endOfDay(dateTime);

        return DateUtil.format(endOfDay, DatePattern.NORM_DATETIME_PATTERN);
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    private String trim(String value){
        return StringUtils.trim(value);
    }
}

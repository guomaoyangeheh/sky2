package com.gmy.sky2.sqlscript.vo;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author guomaoyang
 * @Date 2021/11/24
 */
public class VipGrantVO3 {
    private String grantNO;
    private String feeCode;


    public String getGrantNO() {
        return grantNO;
    }

    public void setGrantNO(String grantNO) {
        this.grantNO = trim(grantNO);
    }

    public String getFeeCode() {
        return feeCode;
    }

    public void setFeeCode(String feeCode) {
        this.feeCode = trim(feeCode);
    }

    private String trim(String value){
        return StringUtils.trim(value);
    }
}

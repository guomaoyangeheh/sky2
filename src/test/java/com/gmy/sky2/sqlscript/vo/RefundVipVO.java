package com.gmy.sky2.sqlscript.vo;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * @Author guomaoyang
 * @Date 2021/11/19
 */
public class RefundVipVO {

    private String grantNO;
    private String partnerName;
    private String refundTime;
    private String refundAmount;

    public String getGrantNO() {
        return grantNO;
    }

    public void setGrantNO(String grantNO) {
        this.grantNO = trim(grantNO);
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = trim(partnerName);
    }

    public String getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(String refundTime) {
        this.refundTime = trim(refundTime);
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount.multiply(new BigDecimal(100)).stripTrailingZeros().toPlainString();
    }

    private String trim(String value){
        return StringUtils.trim(value);
    }
}

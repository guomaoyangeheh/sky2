package com.gmy.sky2.sqlscript.vo;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author guomaoyang
 * @Date 2021/10/31
 */
public class VipGrantVO {
    private String grantNO;
    private String refundOperator;
    private String refund_amount;
    private String refund_time;
    private String refund_remark;
    private String associate_operator;
    private String associate_time;
    private String associate_partner_name;

    public String getGrantNO() {
        return grantNO;
    }

    public void setGrantNO(String grantNO) {
        this.grantNO = trim(grantNO);
    }

    public String getRefundOperator() {
        return refundOperator;
    }

    public void setRefundOperator(String refundOperator) {
        this.refundOperator = trim(refundOperator);
    }

    public String getRefund_amount() {
        return refund_amount;
    }

    public void setRefund_amount(String refund_amount) {
        this.refund_amount = trim(refund_amount);
    }

    public String getRefund_time() {
        return refund_time;
    }

    public void setRefund_time(String refund_time) {
        this.refund_time = trim(refund_time);
    }

    public String getRefund_remark() {
        return refund_remark;
    }

    public void setRefund_remark(String refund_remark) {
        this.refund_remark = trim(refund_remark);
    }

    public String getAssociate_operator() {
        return associate_operator;
    }

    public void setAssociate_operator(String associate_operator) {
        this.associate_operator = trim(associate_operator);
    }

    public String getAssociate_time() {
        return associate_time;
    }

    public void setAssociate_time(String associate_time) {
        this.associate_time = trim(associate_time);
    }

    public String getAssociate_partner_name() {
        return associate_partner_name;
    }

    public void setAssociate_partner_name(String associate_partner_name) {
        this.associate_partner_name = trim(associate_partner_name);
    }

    private String trim(String value){
        return StringUtils.trim(value);
    }

}

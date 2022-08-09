package com.gmy.sky2.sqlscript.vo;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author guomaoyang
 * @Date 2021/12/6
 */
public class CreateVipForm {

    private String partnerName;
    private Long partnerId;
    private Long gradeTpId;
    private String startTime;
    private String endTime;

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = trim(partnerName);
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public Long getGradeTpId() {
        return gradeTpId;
    }

    public void setGradeTpId(Long gradeTpId) {
        this.gradeTpId = gradeTpId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = trim(startTime);
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = trim(endTime);
    }

    private String trim(String value){
        return StringUtils.trim(value);
    }


}

package com.gmy.sky2.sqlscript.vo;

import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author guomaoyang
 * @Date 2021/4/30
 */
@ToString
public class ContractVO {
    private String contractId;
    private String contractNo;
    private String contractDate;
    private String partnerName;
    private String oldContractId;

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = trim(contractId);
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = trim(contractNo);
    }

    public String getContractDate() {
        return contractDate;
    }

    public void setContractDate(String contractDate) {
        this.contractDate = trim(contractDate);
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = trim(partnerName);
    }

    public String getOldContractId() {
        return oldContractId;
    }

    public void setOldContractId(String oldContractId) {
        this.oldContractId = trim(oldContractId);
    }

    private String trim(String value){
        return StringUtils.trim(value);
    }
}

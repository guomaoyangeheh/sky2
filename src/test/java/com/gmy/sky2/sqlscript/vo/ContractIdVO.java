package com.gmy.sky2.sqlscript.vo;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author guomaoyang
 * @Date 2021/4/30
 */
public class ContractIdVO {
    private String taskId;
    private String contractId;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = trim(taskId);
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = trim(contractId);
    }

    private String trim(String value){
        return StringUtils.trim(value);
    }
}

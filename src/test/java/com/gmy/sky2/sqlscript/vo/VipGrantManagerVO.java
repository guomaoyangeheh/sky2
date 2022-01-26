package com.gmy.sky2.sqlscript.vo;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author guomaoyang
 * @Date 2021/10/31
 */
public class VipGrantManagerVO {
    private String managerId;
    private String managerName;
    private String detpName;
    private String grantNO;

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = trim(managerId);
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = trim(managerName);
    }

    public String getDetpName() {
        return detpName;
    }

    public void setDetpName(String detpName) {
        this.detpName = trim(detpName);
    }

    public String getGrantNO() {
        return grantNO;
    }

    public void setGrantNO(String grantNO) {
        this.grantNO = trim(grantNO);
    }

    private String trim(String value){
        return StringUtils.trim(value);
    }
}

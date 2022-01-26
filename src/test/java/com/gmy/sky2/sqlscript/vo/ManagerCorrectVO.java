package com.gmy.sky2.sqlscript.vo;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author guomaoyang
 * @Date 2021/8/17
 */
public class ManagerCorrectVO {
    private String id;
    private String name;
    private String nickName;
    private String deptName;
    private String corpName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = trim(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = trim(name);
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = trim(nickName);
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = trim(deptName);
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = trim(corpName);
    }

    private String trim(String value){
        return StringUtils.trim(value);
    }
}

package com.cdkj.huatuweitong.bean;

/**
 * 系统参数
 * @author qi
 * @updateDts 2018/8/24
 */

public class SystemParameterBean {


    /**
     * ckey : telephone
     * cvalue : 0571-88888888
     * id : 5.0
     * remark : 服务热线
     * type : sys_txt
     * updateDatetime : Aug 15, 2018 5:33:30 PM
     * updater : USYS201800000000001
     */

    private String ckey;
    private String cvalue;
    private double id;
    private String remark;
    private String type;
    private String updateDatetime;
    private String updater;

    public String getCkey() {
        return ckey;
    }

    public void setCkey(String ckey) {
        this.ckey = ckey;
    }

    public String getCvalue() {
        return cvalue;
    }

    public void setCvalue(String cvalue) {
        this.cvalue = cvalue;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(String updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }
}

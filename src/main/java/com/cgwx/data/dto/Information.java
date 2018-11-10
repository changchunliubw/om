package com.cgwx.data.dto;
//根据主机查询监控项ID的返回类型
public class Information {
    String hostid;
    String itemid;
    String lastvalue;
    String key_;
    public String getHostid() {
        return hostid;
    }

    public String getItemid() {
        return itemid;
    }

    public String getLastvalue() {
        return lastvalue;
    }

    public void setHostid(String hosid) {
        this.hostid = hosid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public void setLastvalue(String lastvalue) {
        this.lastvalue = lastvalue;
    }

    public String getKey_() {
        return key_;
    }

    public void setKey_(String key_) {
        this.key_ = key_;
    }
}

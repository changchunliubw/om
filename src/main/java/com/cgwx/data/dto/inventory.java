package com.cgwx.data.dto;
//资产详情
public class inventory {
    String contact;          //联系人
    String hostid;           //
    String location;         //
    String os_short;         //
    String tag;              //
    String type;             //

    public String getContact() {
        return contact;
    }

    public String getHostid() {
        return hostid;
    }

    public String getLocation() {
        return location;
    }

    public String getOs_short() {
        return os_short;
    }

    public String getTag() {
        return tag;
    }

    public String getType() {
        return type;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setHostid(String hostid) {
        this.hostid = hostid;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setOs_short(String os_short) {
        this.os_short = os_short;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setType(String type) {
        this.type = type;
    }
}

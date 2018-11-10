package com.cgwx.data.dto;
//返回给前台所需的主机信息类型
public class HostInfo {
    String hostid;
    String deviceName;
    String deviceIP;
    String deviceID;
    String deviceOS;
    String deviceAddr;
    String deviceApart;
    String deviceGroup;
    public void setHostid(String hostid) {
        this.hostid = hostid;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setDeviceIP(String deviceIP) {
        this.deviceIP = deviceIP;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public void setDeviceOS(String deviceOS) {
        this.deviceOS = deviceOS;
    }

    public void setDeviceAddr(String deviceAddr) {
        this.deviceAddr = deviceAddr;
    }

    public void setDeviceApart(String deviceApart) {
        this.deviceApart = deviceApart;
    }

    public void setDeviceGroup(String deviceGroup) {
        this.deviceGroup = deviceGroup;
    }

    public String getHostid() {
        return hostid;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceIP() {
        return deviceIP;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public String getDeviceOS() {
        return deviceOS;
    }

    public String getDeviceAddr() {
        return deviceAddr;
    }

    public String getDeviceApart() {
        return deviceApart;
    }

    public String getDeviceGroup() {
        return deviceGroup;
    }


}

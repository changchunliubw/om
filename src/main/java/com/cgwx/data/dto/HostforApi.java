package com.cgwx.data.dto;
//zabbix数据库取出的主机信息类型
public class HostforApi {
    String host;
    String hostid;
    inventory inventory;

    public String getHost() {
        return host;
    }

    public String getHostid() {
        return hostid;
    }

    public com.cgwx.data.dto.inventory getInventory() {
        return inventory;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setHostid(String hostid) {
        this.hostid = hostid;
    }

    public void setInventory(com.cgwx.data.dto.inventory inventory) {
        this.inventory = inventory;
    }
}

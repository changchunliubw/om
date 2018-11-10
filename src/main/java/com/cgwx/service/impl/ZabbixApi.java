package com.cgwx.service.impl;

import com.alibaba.fastjson.JSONObject;

import com.cgwx.data.dto.*;

import java.util.List;

public interface ZabbixApi {
    void init();

    void destroy();

    String apiVersion();
     void SetZabbixApi(String url);
    JSONObject call(Request request);

    boolean login(String user, String password);
    //public DefaultsetZabbixApi(String url);
    List<HostforApi> GetHostList();
    void SearchGroupList();
    String[][] SearchHostListByGroupid(String groupid);
    String GetHostidBYhost(String host);
    HostforApi GetHostBYhostid(String hostid);
    Information GetCPUItemByHostid(String hostid);
    Information GetProcItemByHostid(String hostid);
    Information GetMemItemByHostid(String hostid, int type);
    List<Information> GetNetItemByHostid(String hostid,int type);
    List<Information> GetDiskItemByHostid(String hostid);
    void  GetHistoryByItemid();
    TrendList GetTrendByItemid(String itemids, int day);
    //void GetItemInfoByhostid();
    void GetTrigger();
    String GetGroupidByhostid(String hostid);


}

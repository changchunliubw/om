package com.cgwx.controller;

import com.cgwx.aop.result.Result;
import com.cgwx.aop.result.ResultUtil;
import com.cgwx.data.dto.HostInfo;
import com.cgwx.data.dto.HostforApi;
import com.cgwx.service.impl.ZabbixApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@EnableAutoConfiguration
@CrossOrigin
@Controller
public class HostdetailController {
    @Autowired
    ZabbixApi zabbixApi;

    @RequestMapping("/host")
    @CrossOrigin(methods = RequestMethod.POST)
    @ResponseBody
    public Result gethost(@RequestParam(value = "hostid", required = true) String hostid) {
       // System.out.println("dddddddddd");
        zabbixApi.SetZabbixApi("http://10.10.105.100:28087/api_jsonrpc.php");
        zabbixApi.init();
        zabbixApi.login("admin", "zabbix");
        HostforApi hostforApi ;
        hostforApi = zabbixApi.GetHostBYhostid(hostid);
        HostInfo hostInfo = new HostInfo();

        //HostInfo hostInfotemp=new HostInfo();
        hostInfo.setDeviceIP(hostforApi.getHost());
        hostInfo.setDeviceApart(hostforApi.getInventory().getContact());
        hostInfo.setDeviceID(hostforApi.getInventory().getTag());
        hostInfo.setDeviceAddr(hostforApi.getInventory().getLocation());
        hostInfo.setDeviceName(hostforApi.getInventory().getType());
        hostInfo.setDeviceOS(hostforApi.getInventory().getOs_short());
        hostInfo.setHostid(hostforApi.getHostid());
        hostInfo.setDeviceGroup(zabbixApi.GetGroupidByhostid(hostInfo.getHostid()));
        return ResultUtil.success(hostInfo);
    }
}

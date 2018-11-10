package com.cgwx.controller;


import com.cgwx.aop.result.ResultUtil;
import com.cgwx.aop.result.Result;
import com.cgwx.data.dto.Information;
import com.cgwx.data.dto.TrendList;
import com.cgwx.service.impl.ZabbixApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
@EnableAutoConfiguration
@CrossOrigin
@Controller
public class CpuTrendlistController {
    Information cpu;
    TrendList cpuTrendList;
    @Autowired
    ZabbixApi zabbixApi;

    @RequestMapping("/cupHistoryList")
    @CrossOrigin(methods = RequestMethod.GET)
    @ResponseBody
    public Result cupHistoryList(@RequestParam(value = "day", required = true) int day,
                                 @RequestParam(value = "hostid", required = true) String hostid)
    {
        zabbixApi.SetZabbixApi("http://10.10.105.100:28087/api_jsonrpc.php");
        zabbixApi.init();
        zabbixApi.login("admin","zabbix");
        cpu=zabbixApi.GetCPUItemByHostid(hostid);
        cpuTrendList=zabbixApi.GetTrendByItemid(cpu.getItemid(),day);
        return ResultUtil.success(cpuTrendList);
    }
   /* public Result test(){

        return ResultUtil.success("hello");
    }*/


}

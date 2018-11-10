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
public class ProcTrendlistController {

    Information proc;
    TrendList procTrendList;
    @Autowired
    ZabbixApi zabbixApi;

    @RequestMapping("/procHistoryList")
    @CrossOrigin(methods = RequestMethod.POST)
    @ResponseBody
    public Result cupHistoryList(@RequestParam(value = "day", required = true) int day,
                                 @RequestParam(value = "hostid", required = true) String hostid)
    {
        zabbixApi.SetZabbixApi("http://10.10.105.100:28087/api_jsonrpc.php");
        zabbixApi.init();
        zabbixApi.login("admin","zabbix");
        proc=zabbixApi.GetProcItemByHostid(hostid);
        procTrendList=zabbixApi.GetTrendByItemid(proc.getItemid(),day);
        return ResultUtil.success(procTrendList);
    }

}

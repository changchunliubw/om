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

import java.util.ArrayList;
import java.util.List;
@EnableAutoConfiguration
@CrossOrigin
@Controller
public class MemTrendlistController {

    @Autowired
    ZabbixApi zabbixApi;

    @RequestMapping("/MemHistoryList")
    @CrossOrigin(methods = RequestMethod.POST)
    @ResponseBody
    public Result cupHistoryList(@RequestParam(value = "day", required = true) int day,
                                 @RequestParam(value = "hostid", required = true) String hostid,
                                 @RequestParam(value = "os", required = true) String operationsystem ) {
        zabbixApi.SetZabbixApi("http://10.10.105.100:28087/api_jsonrpc.php");
        zabbixApi.init();
        zabbixApi.login("admin", "zabbix");

        Information Mem1;            //内存可用itemID
        Information Mem2;             //内存总量itemID
        TrendList MemTrendList;       //内存可用比历史记录
        TrendList MemTrendList1;      // 内存可用历史记录
        TrendList MemTrendList2;      //内存总量历史记录

        if(operationsystem.equals("linux")) {
            //获取item
            Mem1 = zabbixApi.GetMemItemByHostid(hostid, 1);
            Mem2 = zabbixApi.GetMemItemByHostid(hostid, 2);
        }
        else {
            Mem1 = zabbixApi.GetMemItemByHostid(hostid, 3);
            Mem2 = zabbixApi.GetMemItemByHostid(hostid, 2);
        }
            //获取历史记录
            MemTrendList1 = zabbixApi.GetTrendByItemid(Mem1.getItemid(), day);
            MemTrendList2 = zabbixApi.GetTrendByItemid(Mem2.getItemid(), day);
            //取记录最少的值为基准值
            List<String> clocklist;
            List<String> values = new ArrayList<String>();
            if (MemTrendList1.getClocks().size() > MemTrendList2.getClocks().size()) {
                clocklist = MemTrendList2.getClocks();
            } else {
                clocklist = MemTrendList1.getClocks();
            }
            List<String> values1 = MemTrendList1.getValues();
            List<String> values2 = MemTrendList2.getValues();
            //计算可用百分比
            for (int i = 0; i < clocklist.size(); i++) {
                String value1 = values1.get(i);
                String value2 = values2.get(i);

                float value;
                value = Float.parseFloat(value1) / Float.parseFloat(value2);
                //System.err.println(value1 + " "+value2+" "+value);
                if(operationsystem.equals("linux")==false)
                {
                    value=1-value;
                }
                values.add(Float.toString(value));
            }
            //System.err.println("--------------"+clocklist + " "+clocklist.size());
            MemTrendList = new TrendList();
            MemTrendList.setClocks(clocklist);
            MemTrendList.setValues(values);
            return ResultUtil.success(MemTrendList);
        }

}

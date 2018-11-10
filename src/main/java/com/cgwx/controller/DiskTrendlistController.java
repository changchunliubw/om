package com.cgwx.controller;

import com.cgwx.aop.result.Result;
import com.cgwx.aop.result.ResultUtil;
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
public class DiskTrendlistController {
    @Autowired
    ZabbixApi zabbixApi;

    @RequestMapping("/DiskHistoryList")
    @CrossOrigin(methods = RequestMethod.POST)
    @ResponseBody
    public Result diskHistoryList(@RequestParam(value = "day", required = true) int day,
                                  @RequestParam(value = "hostid", required = true) String hostid){
        zabbixApi.SetZabbixApi("http://10.10.105.100:28087/api_jsonrpc.php");
        zabbixApi.init();
        zabbixApi.login("admin", "zabbix");
        List<Information> Diskitemlist=new ArrayList<Information>();//硬盘itemID列表
        List<Information> Diskused=new ArrayList<Information>(); //硬盘使用itemID
        List<Information> Disktotal=new ArrayList<Information>();             //硬盘总量itemID
        TrendList DiskTrendList =new TrendList();       //使用比历史记录
        List <TrendList>  DiskusedTrendList=new ArrayList<TrendList>();      // 硬盘使用历史记录
        List <TrendList>  DisktotalTrendList=new ArrayList<TrendList>();      //硬盘总量历史记录
        //选择最小数量的历史列表的时间的临时变量
        int clocksizeused=1000;
        int clocksizetotal=1000;
        int clockused=1000;
        int clocktotal=1000;
        List<String> clocklistused;
        List<String> clocklisttotal;
        List<String> clocklist;
        List<String> values =new ArrayList<String>();
        List<Float> valuesused =new ArrayList<Float>();
        List<Float> valuestotal =new ArrayList<Float>();

        Diskitemlist=zabbixApi.GetDiskItemByHostid(hostid);
        for(int i=0;i<Diskitemlist.size();i++)
        {
            String key_=Diskitemlist.get(i).getKey_();

            if (key_.contains("used")){
                System.err.println("----key--used--"+key_);
                Diskused.add(Diskitemlist.get(i));
            }
            else if((key_.contains("total")))
            {
                System.err.println("----key--total-"+key_);
                Disktotal.add(Diskitemlist.get(i));
            }
        }
        for(int i=0;i<Diskused.size();i++)
        {
            TrendList trendtemp=zabbixApi.GetTrendByItemid(Diskused.get(i).getItemid(), day);
            //System.err.println("-22-"+trendtemp.getClocks());
             System.err.println("-22-"+trendtemp.getValues()+"222"+Diskused.get(i).getKey_());
            DiskusedTrendList.add(trendtemp);
        }
        for(int i=0;i<Disktotal.size();i++)
        {
            TrendList trendtemp=zabbixApi.GetTrendByItemid(Disktotal.get(i).getItemid(), day);
            //System.err.println("-22-"+trendtemp.getClocks());
             System.err.println("-33-"+trendtemp.getValues()+"333"+Disktotal.get(i).getKey_());
            DisktotalTrendList.add(trendtemp);
        }
        //选择时钟
        for(int i=0;i<DiskusedTrendList.size();i++)
        {
            if(DiskusedTrendList.get(i).getClocks().size()<clocksizeused)
            {
                clocksizeused=DiskusedTrendList.get(i).getClocks().size();
                clockused=i;
            }
        }
        for(int i=0;i<DisktotalTrendList.size();i++)
        {
            if(DisktotalTrendList.get(i).getClocks().size()<clocksizetotal)
            {
                clocksizetotal=DisktotalTrendList.get(i).getClocks().size();
                clocktotal=i;
            }
        }
        clocklistused=DiskusedTrendList.get(clockused).getClocks();
        clocklisttotal=DisktotalTrendList.get(clocktotal).getClocks();
        if(clocksizeused<clocksizetotal)
        {
            clocklist=clocklistused;
        }
        else
        {
            clocklist=clocklisttotal;
        }

        //计算磁盘使用和
        for(int i=0;i<clocklist.size();i++)
        {
            float value=0;
            for(int j=0;j<DiskusedTrendList.size();j++)
            {
                value=value+Float.parseFloat(DiskusedTrendList.get(j).getValues().get(i));
            }
            value=value/1024;
            valuesused.add(value);
        }
        //计算磁盘总量流量和
        for(int i=0;i<clocklist.size();i++)
        {
            float value=0;
            for(int j=0;j<DisktotalTrendList.size();j++)
            {
                value=value+Float.parseFloat(DisktotalTrendList.get(j).getValues().get(i));
            }
            value=value/1024;
            valuestotal.add(value);
        }
        int minsize=1000;
        if(valuesused.size()<valuestotal.size())
        {
            minsize=valuesused.size();
        }
        else
        {
            minsize=valuestotal.size();
        }
        for(int i=0;i<minsize;i++)
        {
            float valuetemp=valuesused.get(i)/valuestotal.get(i);
            values.add(Float.toString(valuetemp));
        }
        DiskTrendList.setValues(values);
        DiskTrendList.setClocks(clocklist);
        return ResultUtil.success(DiskTrendList);
    }
}

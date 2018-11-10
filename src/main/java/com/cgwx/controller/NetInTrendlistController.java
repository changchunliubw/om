package com.cgwx.controller;

import com.cgwx.aop.result.Result;
import com.cgwx.aop.result.ResultUtil;
import com.cgwx.data.dto.Information;
import com.cgwx.data.dto.TrendList;
import com.cgwx.data.dto.TrendListforNet;
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
public class NetInTrendlistController {


    @Autowired
    ZabbixApi zabbixApi;

    @RequestMapping("/NetHistoryList")
    @CrossOrigin(methods = RequestMethod.POST)
    @ResponseBody
    public Result cupHistoryList(@RequestParam(value = "day", required = true) int day,
                                 @RequestParam(value = "hostid", required = true) String hostid,
                                 @RequestParam(value = "os", required = true) String operationsystem) {
        zabbixApi.SetZabbixApi("http://10.10.105.100:28087/api_jsonrpc.php");
        zabbixApi.init();
        zabbixApi.login("admin", "zabbix");
        List<Information> Netinlist=new ArrayList<Information>();            //所有网卡下行流量item
        List<Information> Netoutlist=new ArrayList<Information>();          //所有网卡上行流量item
        TrendListforNet NetTrendList=new TrendListforNet();               //返回结果
        List <TrendList> NetTrendListin=new ArrayList<TrendList>();              //所有网卡下行流量历史
        List <TrendList> NetTrendListout=new ArrayList<TrendList>();              //所有网卡上行流量历史
        List<String> clocklistin;
        List<String> clocklistout;
        List<String> valuesin =new ArrayList<String>();
        List<String> valuesout =new ArrayList<String>();
        //选择最小数量的历史列表的时间的临时变量
        int clocksizein=1000;
        int clocksizeout=1000;
        int clockin=1000;
        int clockout=1000;
        List<String> clocklist;
        List<String> values =new ArrayList<String>();
        //取有效网卡历史记录
        if(operationsystem.equals("linux"))
        {
            Netinlist=zabbixApi.GetNetItemByHostid(hostid,1);
            Netoutlist=zabbixApi.GetNetItemByHostid(hostid,2);

            for(int i=0;i<Netinlist.size();i++)
            {
                // System.err.println("-22-"+Netinlist.get(i).getItemid());
                if(Netinlist.get(i).getLastvalue().equals("0"))
                {
                    continue;
                }
                TrendList trendtemp=zabbixApi.GetTrendByItemid(Netinlist.get(i).getItemid(), day);
                //System.err.println("-22-"+trendtemp.getClocks());
                // System.err.println("-22-"+trendtemp.getValues());
                NetTrendListin.add(trendtemp);
            }
            for(int i=0;i<Netoutlist.size();i++)
            {
                if(Netoutlist.get(i).getLastvalue().equals("0"))
                {
                    continue;
                }
                TrendList trendtemp=zabbixApi.GetTrendByItemid(Netoutlist.get(i).getItemid(), day);
                NetTrendListout.add(trendtemp);
            }
//            List<String> clocklistin;
//            List<String> clocklistout;
//            List<String> valuesin =new ArrayList<String>();
//            List<String> valuesout =new ArrayList<String>();
//            int clocksizein=1000;
//            int clocksizeout=1000;
//            int clockin=1000;
//            int clockout=1000;
//            for(int i=0;i<NetTrendListin.size();i++)
//            {
//                if(NetTrendListin.get(i).getClocks().size()<clocksizein)
//                {
//                    clocksizein=NetTrendListin.get(i).getClocks().size();
//                    clockin=i;
//                }
//            }
//            for(int i=0;i<NetTrendListout.size();i++)
//            {
//                if(NetTrendListout.get(i).getClocks().size()<clocksizeout)
//                {
//                    clocksizeout=NetTrendListout.get(i).getClocks().size();
//                    clockout=i;
//                }
//            }

//
//            clocklistin=NetTrendListin.get(clockin).getClocks();
//            clocklistout=NetTrendListout.get(clockout).getClocks();
//            if(clocksizein<clocksizeout)
//            {
//                clocklist=clocklistin;
//            }
//            else
//            {
//                clocklist=clocklistout;
//            }
//            for(int i=0;i<clocklist.size();i++)
//            {
//                float value=0;
//                for(int j=0;j<NetTrendListin.size();j++)
//                {
//                    value=value+Float.parseFloat(NetTrendListin.get(j).getValues().get(i));
//                }
//                value=value/1024;
//                valuesin.add(Float.toString(value));
//            }
//            for(int i=0;i<clocklist.size();i++)
//            {
//                float value=0;
//                for(int j=0;j<NetTrendListout.size();j++)
//                {
//                    value=value+Float.parseFloat(NetTrendListout.get(j).getValues().get(i));
//                }
//                value=value/1024;
//                valuesout.add(Float.toString(value));
//            }
            // System.err.println("-----3333------"+clocklist + " "+clocklist.size());
            // System.err.println("----33333----"+valuesin + " "+valuesin.size());
            //System.err.println("----33333----"+valuesout + " "+valuesout.size());
//            NetTrendList.setClocks(clocklist);
//            NetTrendList.setValuesin(valuesin);
//            NetTrendList.setValuesout(valuesout);
            //System.err.println("--------------"+clocklist + " "+clocklist.size());

           // return ResultUtil.success(NetTrendList);
        }
        else
        {
            Netinlist=zabbixApi.GetNetItemByHostid(hostid,3);
            Netoutlist=zabbixApi.GetNetItemByHostid(hostid,4);
            for(int i=0;i<Netinlist.size();i++)
            {
                // System.err.println("-22-"+Netinlist.get(i).getItemid());
                if(Netinlist.get(i).getLastvalue().equals("0"))
                {
                    continue;
                }
                String key_=Netinlist.get(i).getKey_();

                if (key_.contains("-")){
                    System.err.println("----key----"+key_);
                    continue;
                }
                TrendList trendtemp=zabbixApi.GetTrendByItemid(Netinlist.get(i).getItemid(), day);
                //System.err.println("-22-"+trendtemp.getClocks());
                // System.err.println("-22-"+trendtemp.getValues());
                NetTrendListin.add(trendtemp);
            }
            for(int i=0;i<Netoutlist.size();i++)
            {
                if(Netoutlist.get(i).getLastvalue().equals("0"))
                {
                    continue;
                }
                String key_=Netoutlist.get(i).getKey_();

                if (key_.contains("-")){
                    System.err.println("----key----"+key_);
                    continue;
                }
                TrendList trendtemp=zabbixApi.GetTrendByItemid(Netoutlist.get(i).getItemid(), day);
                NetTrendListout.add(trendtemp);
            }

//            for(int i=0;i<NetTrendListin.size();i++)
//            {
//                if(NetTrendListin.get(i).getClocks().size()<clocksizein)
//                {
//                    clocksizein=NetTrendListin.get(i).getClocks().size();
//                    clockin=i;
//                }
//            }
//            for(int i=0;i<NetTrendListout.size();i++)
//            {
//                if(NetTrendListout.get(i).getClocks().size()<clocksizeout)
//                {
//                    clocksizeout=NetTrendListout.get(i).getClocks().size();
//                    clockout=i;
//                }
//            }
//            List<String> clocklist;
//            List<String> values =new ArrayList<String>();


        }
        //选择时钟
        for(int i=0;i<NetTrendListin.size();i++)
        {
            if(NetTrendListin.get(i).getClocks().size()<clocksizein)
            {
                clocksizein=NetTrendListin.get(i).getClocks().size();
                clockin=i;
            }
        }
        for(int i=0;i<NetTrendListout.size();i++)
        {
            if(NetTrendListout.get(i).getClocks().size()<clocksizeout)
            {
                clocksizeout=NetTrendListout.get(i).getClocks().size();
                clockout=i;
            }
        }
        clocklistin=NetTrendListin.get(clockin).getClocks();
        clocklistout=NetTrendListout.get(clockout).getClocks();
        if(clocksizein<clocksizeout)
        {
            clocklist=clocklistin;
        }
        else
        {
            clocklist=clocklistout;
        }
        //计算下行（in）流量和
        for(int i=0;i<clocklist.size();i++)
        {
            float value=0;
            for(int j=0;j<NetTrendListin.size();j++)
            {
                value=value+Float.parseFloat(NetTrendListin.get(j).getValues().get(i));
            }
            value=value/1024;
            valuesin.add(Float.toString(value));
        }
        //计算上行（out）流量和
        for(int i=0;i<clocklist.size();i++)
        {
            float value=0;
            for(int j=0;j<NetTrendListout.size();j++)
            {
                value=value+Float.parseFloat(NetTrendListout.get(j).getValues().get(i));
            }
            value=value/1024;
            valuesout.add(Float.toString(value));
        }
        // System.err.println("-----3333------"+clocklist + " "+clocklist.size());
        // System.err.println("----33333----"+valuesin + " "+valuesin.size());
        //System.err.println("----33333----"+valuesout + " "+valuesout.size());
        NetTrendList.setClocks(clocklist);
        NetTrendList.setValuesin(valuesin);
        NetTrendList.setValuesout(valuesout);
        //System.err.println("--------------"+clocklist + " "+clocklist.size());

        // return ResultUtil.success(NetTrendList);
        return ResultUtil.success(NetTrendList);
    }
}

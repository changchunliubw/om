package com.cgwx.controller;

import com.cgwx.aop.result.ResultUtil;
import com.cgwx.aop.result.Result;
import com.cgwx.data.dto.HostInfo;
import com.cgwx.data.dto.HostforApi;
import com.cgwx.service.impl.ZabbixApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@EnableAutoConfiguration
@CrossOrigin
@Controller
public class HostListController {

    @Autowired
    ZabbixApi zabbixApi;

    @RequestMapping("/hostList")
    @CrossOrigin(methods = RequestMethod.POST)
    @ResponseBody
    public Result gethostlist(){
        //System.out.println("dddddddddd");
        zabbixApi.SetZabbixApi("http://10.10.105.100:28087/api_jsonrpc.php");
        zabbixApi.init();
        zabbixApi.login("admin","zabbix");
        List<HostforApi> hostforApiList=new ArrayList<HostforApi>();
        hostforApiList=zabbixApi.GetHostList();
        List<HostInfo> hostInfoList=new ArrayList<HostInfo>();
        for(int i=0;i<hostforApiList.size();i++)
        {
            HostInfo hostInfotemp=new HostInfo();
            hostInfotemp.setDeviceIP(hostforApiList.get(i).getHost());
            hostInfotemp.setDeviceApart(hostforApiList.get(i).getInventory().getContact());
            hostInfotemp.setDeviceID(hostforApiList.get(i).getInventory().getTag());
            hostInfotemp.setDeviceAddr(hostforApiList.get(i).getInventory().getLocation());
           hostInfotemp.setDeviceName(hostforApiList.get(i).getInventory().getType());
            hostInfotemp.setDeviceOS(hostforApiList.get(i).getInventory().getOs_short());
           hostInfotemp.setHostid(hostforApiList.get(i).getHostid());

            //hostInfotemp.setDeviceGroup(zabbixApi.GetGroupidByhostid(hostInfotemp.getHostid()));
            hostInfoList.add(hostInfotemp);
       }
       //取出每个group对应的主机ID
        String hostgrouplist1[][]=zabbixApi.SearchHostListByGroupid("15");
        String hostgrouplist2[][]=zabbixApi.SearchHostListByGroupid("16");
        String hostgrouplist3[][]=zabbixApi.SearchHostListByGroupid("17");
        String hostgrouplist4[][]=zabbixApi.SearchHostListByGroupid("18");
       // System.err.println(hostInfoList.size());
      //  System.err.println(hostgrouplist1.length);
        for(int i=0;i<hostInfoList.size();i++)
        {
            String hostids=hostInfoList.get(i).getHostid();
            int flag=0;
            for(int j=0;j<hostgrouplist1.length;j++)
            {
                if(hostgrouplist1[j][0].equals(hostids))
                {
                    hostInfoList.get(i).setDeviceGroup(hostgrouplist1[j][1]);
                    flag=1;
                    break;
                }

            }
            if(flag==1)
            {
                continue;                          //找到当前主机的groupID，查询下一个主机
            }
            for(int j=0;j<hostgrouplist2.length;j++)
            {
                if(hostgrouplist2[j][0].equals(hostids))
                {
                    hostInfoList.get(i).setDeviceGroup(hostgrouplist2[j][1]);
                    flag=1;
                    break;
                }

            }
            if(flag==1)
            {
                continue;
            }
            for(int j=0;j<hostgrouplist3.length;j++)
            {
                if(hostgrouplist3[j][0].equals(hostids))
                {
                    hostInfoList.get(i).setDeviceGroup(hostgrouplist3[j][1]);
                    flag=1;
                    break;
                }

            }
            if(flag==1)
            {
                continue;
            }
            for(int j=0;j<hostgrouplist4.length;j++)
            {
                if(hostgrouplist4[j][0].equals(hostids))
                {
                    hostInfoList.get(i).setDeviceGroup(hostgrouplist4[j][1]);
                    flag=1;
                    break;
                }

            }

        }
        //System.err.println(ResultUtil.success(hostInfoList));
        //zabbixApi.destroy();
        return  ResultUtil.success(hostInfoList);

    }

  /*  @RequestMapping("/test1")
    @CrossOrigin(methods = RequestMethod.GET)
    @ResponseBody
    public String test() {
        System.out.println("dddddddddd");
        return "ok";
    }*/



}

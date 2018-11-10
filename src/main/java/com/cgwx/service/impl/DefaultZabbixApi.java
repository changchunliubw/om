package com.cgwx.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.cgwx.data.dto.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DefaultZabbixApi implements ZabbixApi{
    private static final Logger logger = LoggerFactory.getLogger(DefaultZabbixApi.class);

    private CloseableHttpClient httpClient;

    private URI uri;

    private volatile String auth;
    @Override
    public void SetZabbixApi(String url) {
        try {
            uri = new URI(url.trim());
        } catch (URISyntaxException e) {
            throw new RuntimeException("url invalid", e);
        }
    }
   /* public  DefaultZabbixApi(String url) {
        try {
            uri = new URI(url.trim());
        } catch (URISyntaxException e) {
            throw new RuntimeException("url invalid", e);
        }
    }*/
/*    public DefaultZabbixApi(URI uri) {
        this.uri = uri;
    }

    public DefaultZabbixApi(String url, CloseableHttpClient httpClient) {
        this(url);
        this.httpClient = httpClient;
    }

    public DefaultZabbixApi(URI uri, CloseableHttpClient httpClient) {
        this(uri);
        this.httpClient = httpClient;
    }*/

    @Override
    public void init() {
        if (httpClient == null) {
            httpClient = HttpClients.custom().build();
        }
    }

    @Override
    public void destroy() {
        if (httpClient != null) {
            try {
                httpClient.close();
            } catch (Exception e) {
                logger.error("close httpclient error!", e);
            }
        }
    }

    @Override
    public boolean login(String user, String password) {
        this.auth = null;
        Request request = RequestBuilder.newBuilder().paramEntry("user", user).paramEntry("password", password)
                .method("user.login").build();
        JSONObject response = call(request);
        String auth = response.getString("result");
        if (auth != null && !auth.isEmpty()) {
            this.auth = auth;
            return true;
        }
        return false;
    }

    @Override
    public String apiVersion() {
        Request request = RequestBuilder.newBuilder().method("apiinfo.version").build();
        JSONObject response = call(request);
        return response.getString("result");
    }
    @Override
    public List<HostforApi> GetHostList(){
        //List<HostInfo> hostlist=new ArrayList<HostInfo>();
        Request request = RequestBuilder.newBuilder()
                .paramEntry("output",new String[]{"host","hostid"})
                .paramEntry("selectInventory",new String[]{"type","os_short","tag","location","contact"})
                //.paramEntry("hostids","10257")
                .method("host.get").build();
        JSONObject response = call(request);
        JSONArray jsontemp=response.getJSONArray("result");
       // System.err.println(jsontemp);
       // List<inventory> inventoryList =new ArrayList<inventory>();
        List<HostforApi> hostforApiList=new ArrayList<HostforApi>();

       // String[][] hostinfo=new String[jsontemp.size()][2];
        for(int i=0;i<jsontemp.size();i++){
           // hostinfo[i][0]=jsontemp.getJSONObject(i).getString("hostid");
            //hostinfo[i][1]=jsontemp.getJSONObject(i).getString("name");
            //hostinfo[i][2]=jsontemp.getJSONObject(i).getString("availabel");
           // System.err.println(hostinfo[i][0]+": "+hostinfo[i][1]);
            if(jsontemp.getJSONObject(i).getString("inventory").equals("[]"))
            {
                HostforApi hostforApi=new HostforApi();
                hostforApi.setHost(jsontemp.getJSONObject(i).getString("host"));
                hostforApi.setHostid(jsontemp.getJSONObject(i).getString("hostid"));
                hostforApi.setInventory(new inventory());
                hostforApiList.add(hostforApi);
            }
            else {
                String jasonstr=jsontemp.getJSONObject(i).toString();
               // System.err.println(jasonstr);
                hostforApiList.add(JSON.parseObject(jasonstr,HostforApi.class));
            }

        }
        return hostforApiList;
    }
    @Override
    public void SearchGroupList(){
        Request getRequest = RequestBuilder.newBuilder().method("hostgroup.get")
                .paramEntry("output","extend")
                .build();
        JSONObject getResponse = call(getRequest);
        System.err.println(getResponse);
    }
    @Override
    public String GetGroupidByhostid(String hostid){
        Request getRequest = RequestBuilder.newBuilder().method("hostgroup.get")
                .paramEntry("hostids",hostid)
                .paramEntry("output","groupid")
                .build();
        JSONObject getResponse = call(getRequest);
        //System.err.println(getResponse);
       // System.err.println(getResponse.getJSONArray("result").getJSONObject(0).getString("groupid"));
        return getResponse.getJSONArray("result").getJSONObject(0).getString("groupid");

    }
    @Override
    public String[][] SearchHostListByGroupid(String groupid){
         //groupid="15";
        Request getRequest = RequestBuilder.newBuilder().method("host.get")
                .paramEntry("groupids", groupid)
                .paramEntry("output",new String[]{"hostid","host"})
                .build();
        JSONObject getResponse = call(getRequest);
        //System.err.println(getResponse);
        JSONArray jsontemp=getResponse.getJSONArray("result");
        //System.err.println(jsontemp);
        String[][] hostinfo=new String[jsontemp.size()][2];
        for(int i=0;i<jsontemp.size();i++){
            hostinfo[i][0]=jsontemp.getJSONObject(i).getString("hostid");
            hostinfo[i][1]=groupid;
           // System.err.println(hostinfo[i][0]+": "+hostinfo[i][1]);
        }
        return hostinfo;
    }

    @Override
    public String GetHostidBYhost(String host) {
        //String host = "10.10.104.110";
        JSONObject filter = new JSONObject();

        filter.put("host", new String[] { host });
        Request getRequest = RequestBuilder.newBuilder().method("host.get")
                //.paramEntry("selectInvertory","extend")
                .paramEntry("filter", filter)
                .paramEntry("output","extend")
                .build();
		/*Request getRequest = RequestBuilder.newBuilder().method("host.get")
				//paramEntry("selectInvertory",new String[] {"os"})
				.paramEntry("hostids", 10257)
				.paramEntry("output","extend")
				.build();*/
        JSONObject getResponse = call(getRequest);
        System.err.println(getResponse);
        String hostid = getResponse.getJSONArray("result").getJSONObject(0)
                .getString("hostid");
        System.err.println(hostid);
        return  hostid;
    }

    @Override
    public HostforApi GetHostBYhostid(String hostid)
    {
        JSONObject filter = new JSONObject();
        Request getRequest = RequestBuilder.newBuilder().method("host.get")
                .paramEntry("output",new String[]{"host","hostid"})
                .paramEntry("selectInventory",new String[]{"type","os_short","tag","location","contact"})
                .paramEntry("hostids",hostid)
                .build();
        JSONObject getResponse = call(getRequest);
        System.err.println(getResponse);

        JSONArray jsontemp=getResponse.getJSONArray("result");
        String jasonstr=jsontemp.getJSONObject(0).toString();
        // System.err.println(jasonstr);
        HostforApi hostReturn  = new HostforApi();
        hostReturn=(JSON.parseObject(jasonstr,HostforApi.class));
        System.err.println(hostid);
        return hostReturn;

    }

    @Override
    public Information GetCPUItemByHostid(String hostid) {
        JSONObject search= new JSONObject();
        search.put("key_","system.cpu.load[percpu,avg15]");
        Request request = RequestBuilder.newBuilder()
                .paramEntry("hostids",hostid)
                .paramEntry("search",search)
                .paramEntry("output",new String[]{"itemid","hostid","lastvalue","key_"})
                .method("item.get").build();
        JSONObject response = call(request);
        Information cpureturn=null;
        JSONArray jsontemp=response.getJSONArray("result");
      //  String jasonstr=response.getJSONObject("result").toString();
        String jasonstr=jsontemp.getJSONObject(0).toString();
        System.err.println(jasonstr);
        cpureturn=JSON.parseObject(jasonstr, Information.class);
        System.err.println(response);
        return cpureturn;

    }

    @Override
    public Information GetProcItemByHostid(String hostid) {
        JSONObject search= new JSONObject();
        search.put("key_","proc.num[]");
        Request request = RequestBuilder.newBuilder()
                .paramEntry("hostids",hostid)
                .paramEntry("search",search)
                .paramEntry("output",new String[]{"itemid","hostid","lastvalue","key_"})
                .method("item.get").build();
        JSONObject response = call(request);
        Information Procreturn=null;
        JSONArray jsontemp=response.getJSONArray("result");
        //  String jasonstr=response.getJSONObject("result").toString();
        String jasonstr=jsontemp.getJSONObject(0).toString();
        //System.err.println(jasonstr);
        Procreturn=JSON.parseObject(jasonstr, Information.class);
        // System.err.println(response);
        return Procreturn;

    }

    @Override
    public Information GetMemItemByHostid(String hostid, int type) {
        JSONObject search= new JSONObject();
        if(type==1)
        {
            search.put("key_","vm.memory.size[available]");
        }else if(type==2)
        {
            search.put("key_","vm.memory.size[total]");
        }
        else {
            search.put("key_","vm.memory.size[free]");
        }

        Request request = RequestBuilder.newBuilder()
                .paramEntry("hostids",hostid)
                .paramEntry("search",search)
                .paramEntry("output",new String[]{"itemid","hostid","lastvalue","key_"})
                .method("item.get").build();
        JSONObject response = call(request);
        Information Memreturn=null;
        JSONArray jsontemp=response.getJSONArray("result");
        //  String jasonstr=response.getJSONObject("result").toString();
        System.err.println(response);
        String jasonstr=jsontemp.getJSONObject(0).toString();
        //System.err.println(jasonstr);
        Memreturn=JSON.parseObject(jasonstr, Information.class);

        return Memreturn;

    }

    @Override
    public List<Information> GetNetItemByHostid(String hostid,int type){
        JSONObject search= new JSONObject();
        if (type==1)
        {
            search.put("key_","net.if.in[e");
        }
        else if(type==2)
        {
            search.put("key_","net.if.out[e");
        }
        else  if(type==3)
        {
            search.put("key_","net.if.in");
        }
        else
        {
            search.put("key_","net.if.out");
        }

        Request request = RequestBuilder.newBuilder()
                .paramEntry("hostids",hostid)
                .paramEntry("search",search)
                .paramEntry("output",new String[]{"itemid","hostid","lastvalue","key_"})
                .method("item.get").build();
        JSONObject response = call(request);

        JSONArray jsontemp=response.getJSONArray("result");
        //  String jasonstr=response.getJSONObject("result").toString();
        System.err.println(response);
        //System.err.println(jasonstr);
        List<Information> netreturn=new ArrayList<Information>();
        for(int i=0;i<jsontemp.size();i++)
        {
            String jasonstr=jsontemp.getJSONObject(i).toString();
            //System.err.println(jasonstr);
            Information netin=new Information();
            netin=JSON.parseObject(jasonstr, Information.class);
            netreturn.add(netin);
        }
        return netreturn;
    }
    @Override
    public  List<Information> GetDiskItemByHostid(String hostid) {
        JSONObject search= new JSONObject();
        search.put("key_","vfs.fs.size");

        Request request = RequestBuilder.newBuilder()
                .paramEntry("hostids",hostid)
                .paramEntry("search",search)
                .paramEntry("output",new String[]{"itemid","hostid","lastvalue","key_"})
                .method("item.get").build();
        JSONObject response = call(request);

        JSONArray jsontemp=response.getJSONArray("result");
        //  String jasonstr=response.getJSONObject("result").toString();
        System.err.println(response);
        //System.err.println(jasonstr);
        List<Information> diskreturn=new ArrayList<Information>();
        for(int i=0;i<jsontemp.size();i++)
        {
            String jasonstr=jsontemp.getJSONObject(i).toString();
            //System.err.println(jasonstr);
            Information disk=new Information();
            disk=JSON.parseObject(jasonstr, Information.class);
            diskreturn.add(disk);
        }
        return diskreturn;

    }

    @Override
    public void GetHistoryByItemid() {
        JSONObject search= new JSONObject();
        Timestamp timefrom= new Timestamp(System.currentTimeMillis());
        LocalDateTime now = LocalDateTime.now();
        System.err.println(now);
        now = now.minus(1, ChronoUnit.HOURS);
        System.out.println(now.toString());
        timefrom=Timestamp.valueOf(now);
        System.err.println(timefrom);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeFromStr = df.format(timefrom);
        System.err.println(timeFromStr.toString());
        Long Timefrom=timefrom.getTime();
        // String tf=new String(Timefrom.toString());
        System.err.println(Timefrom);
        Timefrom=Timefrom/1000;
        //Long Timetemp=Timefrom+1000;
        System.err.println(Timefrom);
        // System.err.println(Timetemp);
        Request request = RequestBuilder.newBuilder()
                // .paramEntry("hostids",10258)
                .paramEntry("itemids",28915)
                .paramEntry("history",0)
                .paramEntry("time_from",Timefrom)
                //  .paramEntry("time_till",Timetemp)
                // .paramEntry("limit",10)
                //.paramEntry("search",search)
                //  .paramEntry("output",new String[]{"itemid","clock","value"})
                .method("history.get").build();
        JSONObject response = call(request);
        System.err.println(response);
        System.err.println(response.getJSONArray("result").size());
        Timestamp timenow= new Timestamp(System.currentTimeMillis());
        Long timenow1=timenow.getTime();
        System.err.println(timenow1);

    }

    @Override
    public TrendList GetTrendByItemid(String itemids, int day) {
        JSONObject search= new JSONObject();
        Timestamp timefrom= new Timestamp(System.currentTimeMillis());
        LocalDateTime now = LocalDateTime.now();
        //System.err.println(now);
        now = now.minus(day, ChronoUnit.DAYS);
       // System.out.println(now.toString());
        timefrom=Timestamp.valueOf(now);
        //System.err.println(timefrom);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeFromStr = df.format(timefrom);
       // System.err.println(timeFromStr.toString());
        Long Timefrom=timefrom.getTime();
        // String tf=new String(Timefrom.toString());
        //System.err.println(Timefrom);
        Timefrom=Timefrom/1000;
        //Long Timetemp=Timefrom+1000;
        //System.err.println(Timefrom);
        // System.err.println(Timetemp);
        Request request = RequestBuilder.newBuilder()
                // .paramEntry("hostids",10258)
                .paramEntry("itemids",itemids)
                .paramEntry("history",0)
                .paramEntry("time_from",Timefrom)
                //  .paramEntry("time_till",Timetemp)
                // .paramEntry("limit",10)
                //.paramEntry("search",search)
                //  .paramEntry("output",new String[]{"itemid","clock","value"})
                .method("trend.get").build();
        JSONObject response = call(request);
        //System.err.println(response);
        //System.err.println(response.getJSONArray("result").size());
        Timestamp timenow= new Timestamp(System.currentTimeMillis());
       // Long timenow1=timenow.getTime();
        //System.err.println(timenow1);
        JSONArray jsontemp=response.getJSONArray("result");
        //  String jasonstr=response.getJSONObject("result").toString();
        TrendList trend=new TrendList();
        List<String> clocklist=new ArrayList<>();;
        List<String> valuelist=new ArrayList<>();;
        for(int i=0;i<jsontemp.size();)
        {
            //clocklist.add(jsontemp.getJSONObject(i).getString("clock"));
            Long clocktemp=jsontemp.getJSONObject(i).getLong("clock");
            Date dt = new Date(clocktemp * 1000);
            String sDateTime = df.format(dt);  //得到精确到秒的表示：08/31/2006 21:08:00
           // System.err.println(sDateTime);
            clocklist.add(sDateTime);
            valuelist.add(jsontemp.getJSONObject(i).getString("value_avg"));
            i=i+day;
        }
        trend.setClocks(clocklist);
        trend.setValues(valuelist);
        System.err.println(itemids);
        System.err.println(trend.getClocks() + " "+clocklist.size());
        System.err.println(trend.getValues()+ " "+valuelist.size());
        return trend;
    }

   /* @Override
    public void GetItemInfoByhostid() {

    }*/

    @Override
    public void GetTrigger() {

    }

    public boolean hostExists(String name) {
        Request request = RequestBuilder.newBuilder().method("host.exists").paramEntry("name", name).build();
        JSONObject response = call(request);
        return response.getBooleanValue("result");
    }

    public String hostCreate(String host, String groupId) {
        JSONArray groups = new JSONArray();
        JSONObject group = new JSONObject();
        group.put("groupid", groupId);
        groups.add(group);
        Request request = RequestBuilder.newBuilder().method("host.create").paramEntry("host", host)
                .paramEntry("groups", groups).build();
        JSONObject response = call(request);
        return response.getJSONObject("result").getJSONArray("hostids").getString(0);
    }

    public boolean hostgroupExists(String name) {
        Request request = RequestBuilder.newBuilder().method("hostgroup.exists").paramEntry("name", name).build();
        JSONObject response = call(request);
        return response.getBooleanValue("result");
    }

    /**
     *
     * @param name
     * @return groupId
     */
    public String hostgroupCreate(String name) {
        Request request = RequestBuilder.newBuilder().method("hostgroup.create").paramEntry("name", name).build();
        JSONObject response = call(request);
        return response.getJSONObject("result").getJSONArray("groupids").getString(0);
    }

    @Override
    public JSONObject call(Request request) {
        if (request.getAuth() == null) {
            request.setAuth(this.auth);
        }

        try {
            HttpUriRequest httpRequest = org.apache.http.client.methods.RequestBuilder.post().setUri(uri)
                    .addHeader("Content-Type", "application/json")
                    .setEntity(new StringEntity(JSON.toJSONString(request), ContentType.APPLICATION_JSON)).build();
            CloseableHttpResponse response = httpClient.execute(httpRequest);
            HttpEntity entity = response.getEntity();
            byte[] data = EntityUtils.toByteArray(entity);
            return (JSONObject) JSON.parse(data);
        } catch (IOException e) {
            throw new RuntimeException("DefaultZabbixApi call exception!", e);
        }
    }
}

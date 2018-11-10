package com.cgwx;


import com.cgwx.service.impl.DefaultZabbixApi;
import com.cgwx.service.impl.ZabbixApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class Om {


	public static void main(String[] args) {

/*		ZabbixApi zabbixApi=new DefaultZabbixApi();
		zabbixApi.SetZabbixApi("http://10.10.105.100:28087/api_jsonrpc.php");
		zabbixApi.init();
		zabbixApi.login("admin","zabbix");*/
		SpringApplication.run(Om.class, args);

	}
}
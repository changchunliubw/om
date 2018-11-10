package com.cgwx.controller;


import com.cgwx.aop.result.*;
import com.cgwx.aop.result.ResultUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@CrossOrigin
@Controller
public class ControllerTest {

    @RequestMapping(value = "/test")
    @CrossOrigin(methods = RequestMethod.GET)
    @ResponseBody
    public Result test(){

        return ResultUtil.success("hello");
    }

}

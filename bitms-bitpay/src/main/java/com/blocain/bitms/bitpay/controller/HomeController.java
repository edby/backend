package com.blocain.bitms.bitpay.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>File：homeController.java</p>
 * <p>Title: </p>
 * <p>Description:bitpay</p>
 * <p>Copyright: Copyright (c) 2017-07-18 11:05</p>
 * <p>Company: jmwenhua.cn</p>
 * @author 施建波
 * @version 1.0
 */
@Controller
@RequestMapping("/admin")
public class HomeController extends BaseController{ 

	@RequestMapping
    public String main(){
        return "admin/home/main";  
    }
    
    @RequestMapping(value = "/top")
    public String top(){
        return "admin/home/top";   
    }
    
    @RequestMapping(value = "/left")
    public String left(){
        return "admin/home/left";  
    }
}
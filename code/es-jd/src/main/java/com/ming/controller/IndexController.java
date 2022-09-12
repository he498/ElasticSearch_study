package com.ming.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @version v1.0
 * @ClassNmae: IndexController
 * @Description:
 * @Author: Ming on 2022/9/6
 */

@Controller
public class IndexController {
    @GetMapping({"/","/index"})
    public String index(){
        return "index";
    }
}

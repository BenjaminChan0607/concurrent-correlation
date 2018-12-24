package com.cs.struc.concurrentcorrelation.ssm.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author benjaminChan
 * @date 2018/12/24 0024 下午 2:45
 */
@RestController
public class TestController {

    @RequestMapping("/testDomainName")
    public String execute() {
        return "domainName";
    }
}

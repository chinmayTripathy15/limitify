package com.api.api_rate_limiter.ApiRateLimiterApplication.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {
  
    @GetMapping("/test")
 
  public String testApi(){
    return "API call successful";
  }  

}

package com.api.api_rate_limiter.ApiRateLimiterApplication.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class RateLimitService {

    // 
   private  Map<String,Integer> requestCount=new HashMap<>();

   private Map<String,Long> lastTime= new HashMap<>();

   private static final int max=5;

   private static final long Window =60000;

   public boolean allowrequest(String ip){

    // current time 
    long now = System.currentTimeMillis();
  
    // case 1
    if(!requestCount.containsKey(ip)) {
   requestCount.put(ip,1);
   lastTime.put(ip,now);
   return true;
    }

    // Case 2
    long lastRequest=lastTime.get(ip);

   if(now - lastRequest > Window){
    requestCount.put(ip,1);
    lastTime.put(ip,now);
    return true;

   }
   // Current request count
   int currentCount=requestCount.get(ip);

   // Case 3

   if(currentCount < max) {
    requestCount.put(ip, currentCount + 1);
    return true ;
   }
   return false;
   }

   public boolean isAllowed(String ip) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'isAllowed'");
   }
}

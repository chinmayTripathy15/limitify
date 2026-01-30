package com.api.api_rate_limiter.ApiRateLimiterApplication.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.api.api_rate_limiter.ApiRateLimiterApplication.service.RateLimitService;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitFilter implements Filter {

    @Autowired
    private RateLimitService service;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // ✅ Allow CORS preflight
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            chain.doFilter(req, res);
            return;
        }

        String ip = request.getRemoteAddr();

        if (!service.allowrequest(ip)) {

            // ✅ ADD CORS HEADERS MANUALLY
            response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
            response.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "*");

            response.setStatus(429);
            response.setContentType("application/json");
            response.getWriter().write(
                "{\"message\":\"Rate limit exceeded. Please wait 60 seconds.\"}"
            );
            return;
        }

        chain.doFilter(req, res);
    }
}

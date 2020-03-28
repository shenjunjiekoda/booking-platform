package cn.shenjunjie.booking.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.servlet.FilterConfig;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/3/20 20:06
 */
//@Configuration
//@Component
//public class CorsFilter implements Filter {
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        HttpServletResponse res = (HttpServletResponse) response;
//        res.addHeader("Access-Control-Allow-Credentials", "true");
//        res.addHeader("Access-Control-Allow-Origin", "*");
//        res.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
//        res.addHeader("Access-Control-Allow-Headers", "Content-Type,X-CAF-Authorization-Token,sessionToken,X-TOKEN");
//        if ("OPTIONS".equals(((HttpServletRequest) request).getMethod())) {
//            response.getWriter().println("ok");
//            return;
//        }
//        chain.doFilter(request, response);
//    }
//    @Override
//    public void destroy() {
//    }
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//    }
//}
package com.zyh.spring.take_away.filter;

import com.alibaba.fastjson.JSON;
import com.zyh.spring.take_away.commen.R;
import com.zyh.spring.take_away.util.ThreadLocalContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @ Author:张宇航是个大帅哥
 * @ 求求了别逗我笑
 */
@Slf4j
@WebFilter(filterName = "checkLoginFilter", urlPatterns = "/*")
public class CheckLoginFilter implements Filter {
    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();
        /**
         * 将employeeId保存到当前线程中
         */


        HttpServletResponse response = (HttpServletResponse) servletResponse;
        log.info("{}", request.getRequestURI());
        String[] uris = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/login",
                "/user/sendMsg",
        };
        String requestURI = request.getRequestURI();

        Object employee = session.getAttribute("employee");
        Object user = session.getAttribute("user");
        boolean b = checkUri(requestURI, uris);
        if (b || employee != null || user != null) {
            
            if (employee != null) {
                Long employeeId = (Long) session.getAttribute("employee");
                ThreadLocalContext.set(employeeId);
            }
            if (user != null) {
                Long user1 = Long.valueOf(session.getAttribute("user").toString());
                ThreadLocalContext.set(user1);
            }

            filterChain.doFilter(request, response);
            return;
        }

        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));


    }

    public boolean checkUri(String uri, String[] uris) {
        for (String URI : uris) {
            boolean b = antPathMatcher.match(URI, uri);
            if (b) {
                return true;
            }
        }
        return false;
    }
}

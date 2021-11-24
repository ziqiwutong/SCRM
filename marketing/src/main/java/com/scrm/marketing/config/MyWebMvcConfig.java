package com.scrm.marketing.config;

import com.scrm.marketing.util.MyLoggerUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author fzk
 * @date 2021-11-21 16:33
 */
@Configuration
public class MyWebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MyInterceptor()).addPathPatterns("/**");
    }

    public static class MyInterceptor implements HandlerInterceptor {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            // 获取处理method
            if (handler instanceof HandlerMethod) {
                String remoteAddr = request.getRemoteAddr();
                String contentType = request.getContentType();
                String requestURI = request.getRequestURI();
                MyLoggerUtil.info("来自: " + remoteAddr + " uri: " + requestURI + " contentType: " + contentType);
                //Method method = ((HandlerMethod) handler).getMethod();
            }

            // 通过验证
            return true;
        }
    }
}

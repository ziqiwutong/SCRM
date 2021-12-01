package com.scrm.marketing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

/**
 * 开启WebSocket
 *
 * @author fzk
 * @date 2021-11-30 17:00
 */
@Configuration
@EnableWebSocket // 开启WebSocket配置
public class MyWebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myHandler(), "/mk/article/ws")
                .addInterceptors(new MyInterceptor())
                .setAllowedOrigins("*");
    }

    @Bean
    public WebSocketHandler myHandler() {
        return new MyHandler();
    }

    public static class MyHandler extends TextWebSocketHandler {
        /*不涉及发送到其他客户端，不需要缓存其他客户session*/
//        public static final ConcurrentHashMap<String, WebSocketSession> userSessionMap = new ConcurrentHashMap<>();

        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            super.afterConnectionEstablished(session);
            System.out.println(session.getId() + "    open...");
//            userSessionMap.put(session.getId(), session);
        }

        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
            super.handleTextMessage(session, message);
            System.out.println("处理来自：" + session.getId() + " 的text消息：" + message);
            session.sendMessage(message);// 将消息直接返回给发送者
        }

        @Override
        public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
            super.handleTransportError(session, exception);
//            userSessionMap.remove(session.getId());
            if (session.isOpen())
                session.close();

        }

        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
            super.afterConnectionClosed(session, status);
            System.out.println(session.getId() + "    close...");
//            userSessionMap.remove(session.getId());
        }
    }

    public static class MyInterceptor extends HttpSessionHandshakeInterceptor {
        @Override
        public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                       Map<String, Object> attributes) throws Exception {
            System.out.println("Before Handshake");
//            String userCode = ((ServletServerHttpRequest) request).getServletRequest().getParameter("userCode");
//            System.out.println(userCode);

//            attributes.put("userCode", userCode);

            return super.beforeHandshake(request, response, wsHandler, attributes);
        }

        @Override
        public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Exception ex) {
            System.out.println("After Handshake");
            super.afterHandshake(request, response, wsHandler, ex);
        }
    }
}

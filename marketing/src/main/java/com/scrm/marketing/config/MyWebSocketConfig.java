package com.scrm.marketing.config;

import com.scrm.marketing.exception.MyException;
import com.scrm.marketing.util.MyJsonUtil;
import com.scrm.marketing.util.resp.CodeEum;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 开启WebSocket
 *
 * @author fzk
 * @date 2021-11-30 17:00
 */
@Configuration
@EnableWebSocket // 开启WebSocket配置
public class MyWebSocketConfig implements WebSocketConfigurer {
    private final RedisTemplate<String, String> redisTemplate;
    private final RestTemplate restTemplate;

    public MyWebSocketConfig(@Autowired RedisTemplate<String, String> redisTemplate, @Autowired RestTemplate restTemplate) {
        this.redisTemplate = redisTemplate;
        this.restTemplate = restTemplate;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        MyHandler myHandler = new MyHandler(redisTemplate, restTemplate);
        MyInterceptor myInterceptor = new MyInterceptor();// 拦截器

        registry.addHandler(myHandler, "/mk/article/ws")
                .addInterceptors(myInterceptor)
                .setAllowedOrigins("*");// 允许跨域
    }

    public static class MyHandler extends TextWebSocketHandler {
        private final RedisTemplate<String, String> redisTemplate;
        private final RestTemplate restTemplate;
        private static final String keyPrefix = "WebSocketSession:";
        // 用微服务负载均衡的方式进行远程调用
        private static final String addReadRecordUrl = "http://marketing/mk/article/addReadRecord";

        public MyHandler(RedisTemplate<String, String> redisTemplate, RestTemplate restTemplate) {
            this.redisTemplate = redisTemplate;
            this.restTemplate = restTemplate;
        }

        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            super.afterConnectionEstablished(session);
            Map<String, Object> attributes = session.getAttributes();

            ReadInfo readInfo = new ReadInfo();
            readInfo.setArticleId(Long.valueOf(attributes.get("articleId").toString()));
            readInfo.setShareId(Long.valueOf(attributes.get("shareId").toString()));
            readInfo.setOpenid(attributes.get("openid").toString());

            readInfo.setStartTimeStamp(System.currentTimeMillis());

            // 放入缓存
            redisTemplate.opsForValue().set(keyPrefix + session.getId(), MyJsonUtil.toJsonStr(readInfo),
                    1000 * 60 * 20, TimeUnit.MILLISECONDS);
        }

        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
            super.handleTextMessage(session, message);

            System.out.println("WebSocket心跳: from:" + session.getId() + " ,text消息：" + message.getPayload());
            session.sendMessage(message);// 将消息直接返回给发送者
        }

        @Override
        public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
            super.handleTransportError(session, exception);
            // 添加阅读记录 不需要刻意去调用这个，在连接出异常后，关闭时依旧回去调用关闭回调方法
            //addReadRecord(session.getId());
            // 关闭session
            if (session.isOpen())
                session.close();
        }

        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
            super.afterConnectionClosed(session, status);
            // 添加阅读记录
            addReadRecord(session.getId());
        }

        private void addReadRecord(String sessionId) {
            // 取出缓存：计算阅读时间
            String readInfo_json = redisTemplate.opsForValue().get(keyPrefix + sessionId);
            if (readInfo_json != null) {
                ReadInfo readInfo = MyJsonUtil.toBean(readInfo_json, ReadInfo.class);
                redisTemplate.delete(keyPrefix + sessionId);

                int readTime = (int) (System.currentTimeMillis() - readInfo.getStartTimeStamp()) / 1000;
                readInfo.setReadTime(readTime);

                // 请求头
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                // 请求体
                LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
                map.add("articleId", readInfo.getArticleId());
                map.add("shareId", readInfo.getShareId());
                map.add("openid", readInfo.getOpenid());
                map.add("readTime", readInfo.getReadTime());
                HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);

                try {
                    System.out.println("添加阅读记录rest调用，传递参数：\n"+map);
                    restTemplate.postForObject(addReadRecordUrl, httpEntity, Void.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class MyInterceptor extends HttpSessionHandshakeInterceptor {

        @Override
        public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                       Map<String, Object> attributes) throws Exception {
            //System.out.println("Before Handshake");

            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
            String openid = servletRequest.getParameter("openid");
            String articleId = servletRequest.getParameter("articleId");
            String shareId = servletRequest.getParameter("shareId");

            if (openid == null || articleId == null || shareId == null)
                throw new MyException(CodeEum.CODE_PARAM_MISS, "缺少参数：articleId or shareId or openid ?");

            attributes.put("articleId", articleId);
            attributes.put("shareId", shareId);
            attributes.put("openid", openid);

            return super.beforeHandshake(request, response, wsHandler, attributes);
        }

        @Override
        public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Exception ex) {
            //System.out.println("After Handshake");
            super.afterHandshake(request, response, wsHandler, ex);
        }
    }

    @Data
    public static class ReadInfo {
        private Long articleId;
        private Long shareId;
        private String openid;

        private Integer readTime;
        private long startTimeStamp;
    }
}

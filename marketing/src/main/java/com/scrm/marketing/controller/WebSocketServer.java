package com.scrm.marketing.controller;

import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fzk
 * @date 2021-11-30 16:29
 */
@Controller
@ServerEndpoint("/mk/wx/addReadRecord")
public class WebSocketServer {
    /**
     * 用来存放每个客户端对应的WebSocketServer对象
     */
    private static final ConcurrentHashMap<String, WebSocketServer> map = new ConcurrentHashMap<>();
    /**
     * 与某个客户端的连接会话, 需要通过它来给客户端发送数据
     * 导入的是socket包下的Session
     */
    private Session session;


    @OnOpen
    public void open() {

    }

    @OnClose
    public void close() {

    }

    /**
     * 收到客户端消息的回调
     * @param msg 客户端发送的消息
     */
    @OnMessage
    public void message(String msg) {


    }

    @OnError
    public void handleError(Throwable t) {

    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }
}

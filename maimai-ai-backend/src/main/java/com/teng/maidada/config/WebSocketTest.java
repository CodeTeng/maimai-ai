package com.teng.maidada.config;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/6/30 21:12
 */
@Component
@ServerEndpoint(value = "/ws/{code}", configurator = HttpSessionConfigurator.class)
@Slf4j
public class WebSocketTest {
    /**
     * 记录当前在线人数
     */
    private static final AtomicInteger onlineCount = new AtomicInteger(0);

    /**
     * concurrent包的线程安全Map，用来存放每个客户端对应的对话
     */
    private static final Map<String, Session> clientMap = new ConcurrentHashMap<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 接收userId
     */
    private String code;

    @OnOpen
    public void onOpen(@PathParam("code") String code, Session session) {
        this.code = code;
        this.session = session;
        // 在线人数+1
        onlineCount.incrementAndGet();
        clientMap.put(code, session);
        log.info("有新连接加入：{}！当前在线人数为：{}", code, onlineCount.get());
    }

    @OnClose
    public void onClose() {
        onlineCount.decrementAndGet();
        clientMap.remove(this.code);
        log.info("有一连接关闭：{}！当前在线人数为：{}", this.code, onlineCount.get());
    }

    @OnMessage
    public void onMessage(@PathParam("code") String code, String message, Session session) {
        String msg = "[" + code + "]" + message;
        log.info("收到消息：{}", msg);
        // 群发消息
        sendMessageAll(msg, session);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket发生错误，错误信息为：{}", error.getMessage());
        error.printStackTrace();
    }

    /**
     * 群发消息
     * @param message 消息
     * @param fromSession 发送人
     */
    private void sendMessageAll(String message, Session fromSession) {
        clientMap.forEach((sessionId, session) -> sendMessage(message, session));
        // 有的业务场景下，需要排除自己。就是除了自己其他人都发送
//        clientMap.entrySet().forEach(sessionEntry -> {
//            Session toSession = sessionEntry.getValue();
//            // 排除掉自己
//            if (!fromSession.getId().equals(toSession.getId())) {
//                log.info("服务端给客户端 {} 发送消息 {}", toSession.getId(), message);
//                toSession.getAsyncRemote().sendText(message);
//            }
//        });
    }

    /**
     * 服务端发送消息给客户端
     * @param message 消息
     * @param session 接收人
     */
    private void sendMessage(String message, Session session) {
        try {
            // getAsyncRemote 和 getBasicRemote的区别
            // getAsyncRemote是异步发送消息它是非阻塞的，getBasicRemote是同步发送是阻塞的。推荐使用getAsyncRemote
            //session.getBasicRemote().sendText(message);
            session.getAsyncRemote().sendText(message);
        } catch (Exception e) {
            log.error("服务端发送消息 {} 给客户端 {} 失败", message, session.getId());
            e.printStackTrace();
        }
    }

    /**
     * 发送自定义消息
     */
    public static void sendCustomInfo(String message, @PathParam("code") String code) {
        log.info("发送消息到 {}, 报文 {}", code, message);
        JSONObject jsonObject = JSONUtil.parseObj(message);
        if (StringUtils.isNotBlank(code) && clientMap.containsKey(code)) {
            clientMap.get(code).getAsyncRemote().sendText(message);
        } else {
            log.error("用户 {} 不在线！", code);
        }
    }
}

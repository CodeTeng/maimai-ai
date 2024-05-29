package com.teng.maidada;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @description: SSE 服务端实现
 * @author: ~Teng~
 * @date: 2024/5/29 19:44
 */
@WebServlet("/sse/servlet")
public class SseServletTest extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/event-stream");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = resp.getWriter();
        for (int i = 0; i < 10; i++) {
            writer.write("data: Message " + i + "\n\n");
            writer.flush();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        writer.close();
    }

    @GetMapping("/sse")
    public SseEmitter testSSE() {
        // 建立 SSE 连接对象 0 表示不超时
        SseEmitter emitter = new SseEmitter(0L);
        // 业务逻辑处理
        return emitter;
    }
}

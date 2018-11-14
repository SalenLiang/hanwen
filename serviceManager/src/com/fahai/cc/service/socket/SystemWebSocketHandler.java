package com.fahai.cc.service.socket;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fahai.cc.service.dashboard.controller.DashboardController;
//websocket的具体的实现类，用于处理消息
public class SystemWebSocketHandler implements WebSocketHandler {
	
	private static final Log logger; 
	
	@Autowired
	private DashboardController dashboardController;
    public static final List<WebSocketSession>  users; 
 
    static {
        users = new ArrayList<WebSocketSession>();
        logger = LogFactory.getLog(SystemWebSocketHandler.class);
    }

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		
		//连接关闭，移除该用户
		logger.info("移除用户");
		users.remove(session);
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		
		//连接上执行添加用户
		logger.info("连接成功");
		users.add(session);
		dashboardController.checkInterfaceHeartBeat();
		
	}

	@Override
	public synchronized void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		
		//向用户推送消息
		logger.info("发送消息");
		sendMessageToAllUser(message);
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable t) throws Exception {
		
		//连接错误，执行
		if (session.isOpen()) {
			session.close();
		}
//		users.remove(session);
		
		logger.error("SystemWebSocketHandler.class [handleTransportError()] :error");
	}

	@Override
	public boolean supportsPartialMessages() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public static void sendMessageToAllUser(WebSocketMessage<?> message) {
		
        for (WebSocketSession user : users) {
            try {
                if (user.isOpen()) {
                    user.sendMessage(message);
                }
            }catch (Exception e) {
	            logger.error("SystemWebSocketHandler.class [sendMessageToAllUser()] :error",e);
	        }
        }
	 }

}


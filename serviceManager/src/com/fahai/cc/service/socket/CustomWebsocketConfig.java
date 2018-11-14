package com.fahai.cc.service.socket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
@Configuration   //声明为配置类
@EnableWebSocket  //可用websocket
public class CustomWebsocketConfig implements WebSocketConfigurer {

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		
		//注册websocket实现类，指定参数访问地址；设置访问websocket的拦截器;
		registry.addHandler(getHandler(), "/serviceManager/websocket").addInterceptors(getInterceptor());//使用H5原生的websocket
		
		registry.addHandler(getHandler(), "/serviceManager/sockjs/websocket").addInterceptors(getInterceptor()).withSockJS();//使用sockjs模拟websocket
		
	}
	
	@Bean 
	public SystemWebSocketHandler getHandler(){
	
		return new SystemWebSocketHandler();
	}
	
	@Bean
	public WebsocketHandshakeInterceptor getInterceptor(){
		
		return new WebsocketHandshakeInterceptor();
	}

}

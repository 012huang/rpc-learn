package com.slowlizard.rpc.server;

import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.slowlizard.rpc.contract.IDemoService;
import com.slowlizard.rpc.serializer.MethodInvoker;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {
	private static ApplicationContext springApplicationContext;
	static {
		springApplicationContext = new ClassPathXmlApplicationContext("context.xml");
	}
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("server come here:channelRead");
		MethodInvoker methodInvoker = (MethodInvoker) msg;
//		methodInvoker.getClazz();
		 Object service = springApplicationContext.getBean(methodInvoker.getClazz());
		 
		
		Method[] methods = service.getClass().getDeclaredMethods();
		for (Method method : methods) {
			if (method.getName().equals(methodInvoker.getMethod())) {
				Object result = method.invoke(service, methodInvoker.getArgs());
				ctx.writeAndFlush(result);
			}

		}

	}
@Override
public void channelActive(ChannelHandlerContext ctx) throws Exception {
	System.out.println("server come here:channelActive");
}
}

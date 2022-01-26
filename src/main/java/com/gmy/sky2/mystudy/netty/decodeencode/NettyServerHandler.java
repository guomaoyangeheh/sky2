package com.gmy.sky2.mystudy.netty.decodeencode;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Author guomaoyang
 * @Date 2021/4/22
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<User> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, User msg) throws Exception {
        System.out.println("接收到客户端消息：" + msg);
    }
}

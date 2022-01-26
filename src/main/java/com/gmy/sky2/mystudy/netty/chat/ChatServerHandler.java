package com.gmy.sky2.mystudy.netty.chat;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Date;

/**
 * @Author guomaoyang
 * @Date 2021/4/22
 */
public class ChatServerHandler extends ChannelInboundHandlerAdapter {

    //GlobalEventExecutor.INSTANCE 是全局的事件执行器，是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端：" + ctx.channel().remoteAddress()+"上线了");
        channelGroup.forEach(e->{
            e.writeAndFlush(DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN) + "-客户端" + ctx.channel().remoteAddress()+"上线了");
        });
        channelGroup.add(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        channelGroup.forEach(e ->{
            if(e != ctx.channel()){
                e.writeAndFlush(DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN)+ ctx.channel().remoteAddress() + "客户端：" +msg);
            }
        });
    }
}

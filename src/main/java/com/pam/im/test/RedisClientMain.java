package com.pam.im.test;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import static io.netty.util.CharsetUtil.UTF_8;

/**
 * redis 客户端测试
 */

public class RedisClientMain {
    /**
     * set name zhangsan
     * *3
     * $3
     * set
     * $4
     * name
     * $8
     * zhangsan
     *
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        final byte[] line = {13,10}; //回车和空格的数字定义
        NioEventLoopGroup work = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        ChannelFuture channelFuture = bootstrap.group(work)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));
                        pipeline.addLast(new ChannelInboundHandlerAdapter() { //进站处理
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception { //客户端监听连接建立
                                ByteBuf buffer = ctx.alloc().buffer();
                                buffer.writeBytes("*3".getBytes());
                                buffer.writeBytes(line);
                                buffer.writeBytes("$3".getBytes());
                                buffer.writeBytes(line);
                                buffer.writeBytes("set".getBytes());
                                buffer.writeBytes(line);
                                buffer.writeBytes("$4".getBytes());
                                buffer.writeBytes(line);
                                buffer.writeBytes("name".getBytes());
                                buffer.writeBytes(line);
                                buffer.writeBytes("$8".getBytes());
                                buffer.writeBytes(line);
                                buffer.writeBytes("zhangsan".getBytes());
                                buffer.writeBytes(line);
                                ctx.writeAndFlush(buffer);
                            }

                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf byteBuf = (ByteBuf) msg;
                                String s = byteBuf.toString(UTF_8);
                                System.out.println(s);
                            }
                        });
                    }
                }).connect("localhost", 6379).sync();
        channelFuture.channel().closeFuture().sync();
    }
}

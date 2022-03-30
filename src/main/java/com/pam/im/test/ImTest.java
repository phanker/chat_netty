package com.pam.im.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class ImTest {
    public static void main(String[] args) {
        //netty测试工具
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(
                //根据长度进行解码的解码器
                new LengthFieldBasedFrameDecoder(1024,0,4,1,0),
                new LoggingHandler(LogLevel.DEBUG)
        );
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();

        send(buffer,"Hello World!");
        send(buffer,"Hi");
        embeddedChannel.writeInbound(buffer);
    }

    private static void send(ByteBuf buffer,String s) {
        byte[] bytes = s.getBytes(); //实际内容
        buffer.writeInt(bytes.length); //传入内容长度
        buffer.writeByte(1);//版本号
        buffer.writeBytes(bytes);
    }
}

package com.pam.im.test;

import com.pam.im.message.LoginResponseMessage;
import com.pam.im.protocol.MessageCodec;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class ChartTest {
    public static void main(String[] args) throws Exception {
        //netty测试工具
        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);

        EmbeddedChannel channel = new EmbeddedChannel(
                // 可以解决粘包和半包
                new LengthFieldBasedFrameDecoder(1024,12,4,0,0),
                loggingHandler,
                //自定义解码器
                new MessageCodec()
        );
        //测试encode方法
//        LoginResponseMessage loginResponseMessage = new LoginResponseMessage("张三", "123456");
//        channel.writeOutbound(loginResponseMessage);

        //测试decode方法
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();

//        new MessageCodec().encode(null,loginResponseMessage,byteBuf);
        ByteBuf slice1 = byteBuf.slice(0,100);
        ByteBuf slice2 = byteBuf.slice(100,byteBuf.readableBytes()-100);
        slice1.retain();
        channel.writeInbound(slice1);
        channel.writeInbound(slice2);
    }
}

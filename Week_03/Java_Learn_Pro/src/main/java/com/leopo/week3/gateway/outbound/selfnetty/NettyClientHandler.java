package com.leopo.week3.gateway.outbound.selfnetty;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    public ChannelHandlerContext frontCtx;
    public String url;
    private FullHttpRequest fullHttpRequest;


    public NettyClientHandler(ChannelHandlerContext ctx, String url, FullHttpRequest fullHttpRequest) {
        frontCtx = ctx;
        this.url = url;
        this.fullHttpRequest = fullHttpRequest;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws URISyntaxException {
        URI uri = new URI(url);
        DefaultFullHttpRequest request = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1, fullHttpRequest.method(), uri.toASCIIString());
        ctx.writeAndFlush(request);
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws UnsupportedEncodingException {
        if (msg instanceof HttpResponse) {
            System.out.println("收到服务端返回的消息");
            HttpResponse response = (HttpResponse) msg;
            response.headers().set("Content-Type", "application/json");

            for (Map.Entry<String, String> head : fullHttpRequest.headers()) {
                response.headers().add(head.getKey(), head.getValue());
            }
            frontCtx.write(response);
        }
        frontCtx.write("test success");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

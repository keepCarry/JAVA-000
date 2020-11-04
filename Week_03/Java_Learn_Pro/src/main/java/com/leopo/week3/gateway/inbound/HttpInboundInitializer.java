package com.leopo.week3.gateway.inbound;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpInboundInitializer extends ChannelInitializer<SocketChannel> {

    private String proxyServer;
    private int port;

    public HttpInboundInitializer(String proxyServer, int port) {
        this.proxyServer = proxyServer;
        this.port = port;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();

        p.addLast(new HttpServerCodec());
        p.addLast(new HttpObjectAggregator(1024 * 1024));
        p.addLast(new HttpInboundHandler(this.proxyServer,port));
    }
}

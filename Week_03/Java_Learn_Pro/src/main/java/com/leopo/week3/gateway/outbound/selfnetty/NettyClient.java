package com.leopo.week3.gateway.outbound.selfnetty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import java.net.InetSocketAddress;

public class NettyClient {
    private String backendUrl;
    private final int port;
    private final ChannelHandlerContext frontCtx;
    private FullHttpRequest fullHttpRequest;

    public NettyClient(int port, ChannelHandlerContext frontCtx, String backendUrl, FullHttpRequest fullHttpRequest) {
        this.port = port;
        this.frontCtx = frontCtx;
        this.backendUrl = backendUrl.endsWith("/") ? backendUrl.substring(0,backendUrl.length()-1) : backendUrl;
        this.fullHttpRequest = fullHttpRequest;
    }

    public void start(final FullHttpRequest fullRequest) throws Exception {
        String url = this.backendUrl + fullRequest.uri();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress("127.0.0.1", port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new HttpResponseDecoder())
                                    .addLast(new HttpRequestEncoder())
                                    .addLast(new NettyClientHandler(frontCtx, url, fullHttpRequest));
                        }
                    });
            ChannelFuture f = b.connect().sync();
            f.channel().closeFuture().sync();

        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}

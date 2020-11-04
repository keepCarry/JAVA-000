package com.leopo.week3.gateway.inbound;

import com.leopo.week3.gateway.filter.AddHeaderFilter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.leopo.week3.gateway.outbound.selfhttpclient.HttpClient;

public class HttpInboundHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(HttpInboundHandler.class);
    private final String proxyServer;
    private HttpClient handler;
    private int port;
    
    public HttpInboundHandler(String proxyServer,int port) {
        this.proxyServer = proxyServer;
        this.port = port;
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {

            FullHttpRequest fullRequest = (FullHttpRequest) msg;


            AddHeaderFilter addHeaderFilter = new AddHeaderFilter();
            addHeaderFilter.filter(fullRequest, ctx);

            handler = new HttpClient(proxyServer);
            handler.handle(fullRequest, ctx);
    
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

}

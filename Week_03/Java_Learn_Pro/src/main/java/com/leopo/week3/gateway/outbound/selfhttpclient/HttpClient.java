package com.leopo.week3.gateway.outbound.selfhttpclient;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpClient {
    private CloseableHttpClient httpclient;
    private String backendUrl;
    private CloseableHttpResponse response;

    public HttpClient(String backendUrl) {
        this.backendUrl = backendUrl.endsWith("/") ? backendUrl.substring(0,backendUrl.length()-1) : backendUrl;

        httpclient = HttpClients.createDefault();
    }

    public void handle (final FullHttpRequest fullRequest, final ChannelHandlerContext ctx) throws IOException {
        final String url = this.backendUrl + fullRequest.uri();
        httpGet(fullRequest, ctx, url);
    }

    public void httpGet(final FullHttpRequest inbound, final ChannelHandlerContext ctx, final String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        response = httpclient.execute(httpGet);

        // 获取返回内容并输出
        String content = EntityUtils.toString(response.getEntity(), "UTF-8");

        // 确认过滤器一定是将这个头加进去了。
        System.out.println(inbound.headers().get("nio"));
        FullHttpResponse response = null;
        response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(content.getBytes()));
        response.headers().set("Content-Type", "application/json");
        response.headers().setInt("Content-Length", response.content().readableBytes());

        // 将经过了过滤器之后的所有的头加入到response的头里面
        for (Map.Entry<String, String> head : inbound.headers()) {
            response.headers().add(head.getKey(), head.getValue());
        }



        if (inbound != null) {
            if (!HttpUtil.isKeepAlive(inbound)) {
                ctx.write(response).addListener(ChannelFutureListener.CLOSE);
            } else {
                //response.headers().set(CONNECTION, KEEP_ALIVE);
                ctx.write(response);
            }
        }
        ctx.flush();
    }

    public static void main(String[] args) {
        // 创建http get请求
        HttpGet httpGet = new HttpGet("http://localhost:8809/test");

        try (
                // 创建http对象
                CloseableHttpClient httpClient = HttpClients.createDefault();
                // 创建http response
                CloseableHttpResponse response = httpClient.execute(httpGet);
        ) {
            // 获取返回内容并输出
            String content = EntityUtils.toString(response.getEntity(), "UTF-8");
            System.out.println(content);

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}

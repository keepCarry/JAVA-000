package com.leopo.week3.gateway.router;

import org.apache.http.conn.routing.HttpRoute;

import java.util.List;
import java.util.Random;

public class HttpRouter  implements HttpEndpointRouter {
    @Override
    public String route(List<String> endpoints) {
        int size = endpoints.size();
        return endpoints.get(new Random().hashCode()/size);
    }
}

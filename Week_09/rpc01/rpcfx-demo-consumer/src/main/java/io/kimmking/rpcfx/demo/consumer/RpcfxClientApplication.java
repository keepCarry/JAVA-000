package io.kimmking.rpcfx.demo.consumer;

import io.kimmking.rpcfx.client.Rpcfx;
import io.kimmking.rpcfx.demo.api.Order;
import io.kimmking.rpcfx.demo.api.OrderService;
import io.kimmking.rpcfx.demo.api.User;
import io.kimmking.rpcfx.demo.api.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RpcfxClientApplication {

    // 二方库
    // 三方库 lib
    // nexus, userserivce -> userdao -> user
    //

    public static void main(String[] args) {

        ApplicationContext ctx = SpringApplication.run(RpcfxClientApplication.class, args);

        UserService userService = ctx.getBean(UserService.class); // Rpcfx.create(UserService.class, "http://localhost:8080/");
        User user = userService.findById(1);
        System.out.println("find user id=1 from server: " + user.getName());

        OrderService orderService = ctx.getBean(OrderService.class); // Rpcfx.create(OrderService.class, "http://localhost:8080/");
        Order order = orderService.findOrderById(1992129);
        System.out.println(String.format("find order name=%s, amount=%f", order.getName(), order.getAmount()));
        order = orderService.findOrderById(-1);
        System.out.println(String.format("find order name=%s, amount=%f", order.getName(), order.getAmount()));

        // 新加一个OrderService

    }

    @Bean
    public UserService userService() {
        return Rpcfx.create(UserService.class, "http://localhost:8080/");
    }

    @Bean
    public OrderService orderService() {
        return Rpcfx.create(OrderService.class, "http://localhost:8080/");
    }
}

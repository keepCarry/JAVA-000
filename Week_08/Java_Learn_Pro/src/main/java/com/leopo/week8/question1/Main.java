package com.leopo.week8.question1;

import java.io.IOException;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws IOException, SQLException {
        TradeService orderService = new TradeService("application.yml");
        orderService.init();
        orderService.insert();
        orderService.cleanData(32);
        orderService.selectByOrderId(2);
        orderService.cleanData();
    }
}

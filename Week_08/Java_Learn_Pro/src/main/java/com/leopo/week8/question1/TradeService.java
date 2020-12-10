package com.leopo.week8.question1;

import org.apache.shardingsphere.driver.api.yaml.YamlShardingSphereDataSourceFactory;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Random;

public class TradeService {

    private final DataSource dataSource;

    TradeService(final String yamlConfigFile) throws IOException, SQLException {
        dataSource = YamlShardingSphereDataSourceFactory.createDataSource(getFile(yamlConfigFile));
    }

    private File getFile(final String fileName) {
        return new File(TradeService.class.getClassLoader().getResource(fileName).getFile());
    }

    public void init() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS trade_info");
            statement.execute("CREATE TABLE `trade_info` (\n" +
                    "  `order_id` bigint(20) NOT NULL,\n" +
                    "  `goods_id` int NOT NULL COMMENT '商品id',\n" +
                    "  `user_id` int NOT NULL COMMENT '买家id',\n" +
                    "  `update_time` datetime(0) NOT NULL COMMENT '更新时间',\n" +
                    "  `trade_time` datetime(0) NOT NULL COMMENT '交易时间',\n" +
                    "  `status` int NOT NULL COMMENT '状态',\n" +
                    "  `goods_basic_version` int DEFAULT '' COMMENT '商品基础信息版本',\n" +
                    "  `goods_detail_version` int NOT NULL COMMENT '商品详情信息版本',\n" +
                    "  PRIMARY KEY (`order_id`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;");
        }
    }

    public void cleanData() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS trade_info");
        }
    }

    public void insert() throws SQLException {
        Random random = new Random();
        try (Connection connection = dataSource.getConnection()) {
            int count = 0;
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 16; j++) {
                    PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO trade_info (order_id, goods_id, user_id, update_time, trade_time, status, goods_basic_version,goods_detail_version) VALUES (?, ?, ?, ?, ?, ?, ?,?)");
                    preparedStatement.setLong(1, ++count);
                    preparedStatement.setInt(2, random.nextInt(100));
                    preparedStatement.setInt(3, i + 1);
                    preparedStatement.setLong(4, System.currentTimeMillis());
                    preparedStatement.setLong(5, System.currentTimeMillis());
                    preparedStatement.setLong(6, random.nextInt(100));
                    preparedStatement.setLong(7, random.nextInt(100));
                    preparedStatement.executeUpdate();
                }
            }
        }
    }

    public void cleanData(long orderId) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM trade_info WHERE order_id=?")) {
            preparedStatement.setLong(1, orderId);
            preparedStatement.executeUpdate();
        }
    }


    public void selectByOrderId(long orderId) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT order_id FROM trade_info WHERE order_id=?")) {
            preparedStatement.setLong(1, orderId);
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                System.out.println("order_id:" + resultSet.getLong(1));
            }
        }
    }

}

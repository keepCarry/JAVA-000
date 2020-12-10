package com.leopo.week8.question2;

import org.apache.shardingsphere.driver.api.yaml.YamlShardingSphereDataSourceFactory;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.apache.shardingsphere.transaction.core.TransactionTypeHolder;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class XATradeService {

    private final DataSource dataSource;

    XATradeService(final String yamlConfigFile) throws IOException, SQLException {
        dataSource = YamlShardingSphereDataSourceFactory.createDataSource(getFile(yamlConfigFile));
    }

    private File getFile(final String fileName) {
        return new File(XATradeService.class.getClassLoader().getResource(fileName).getFile());
    }

    public void init() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS trade_info");
            statement.execute("CREATE TABLE `trade_info`  (\n" +
                    "  `goods_id` int NOT NULL,\n" +
                    "  `user_id` int NOT NULL,\n" +
                    "  `update_time` datetime(0) NOT NULL,\n" +
                    "  `trade_time` datetime(0) NOT NULL,\n" +
                    "  `status` int NOT NULL,\n" +
                    "  `goods_basic_version` int NOT NULL,\n" +
                    "  `goods_detail_version` int NOT NULL,\n" +
                    "  PRIMARY KEY (`goods_id`, `user_id`) USING BTREE\n" +
                    ") ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;");
        }
    }

    public void cleanup() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS trade_info");
        }
    }

    public void insert() throws SQLException {
        TransactionTypeHolder.set(TransactionType.XA);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO trade_info (order_id, goods_id, user_id, update_time, trade_time, status, goods_basic_version,goods_detail_version) VALUES (?, ?, ?, ?, ?, ?, ?,?)");
            doInsert(preparedStatement);
            connection.commit();
        } finally {
            TransactionTypeHolder.clear();
        }
    }

    public void insertFailed() throws SQLException {
        TransactionTypeHolder.set(TransactionType.XA);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO trade_info (order_id, goods_id, user_id, update_time, trade_time, status, goods_basic_version,goods_detail_version) VALUES (?, ?, ?, ?, ?, ?, ?,?)");
            doInsert(preparedStatement);
            connection.rollback();
        } finally {
            TransactionTypeHolder.clear();
        }
    }

    private void doInsert(final PreparedStatement preparedStatement) throws SQLException {
        for (int i = 0; i < 20; i++) {
            preparedStatement.setObject(1, i);
            preparedStatement.executeUpdate();
        }
    }
}

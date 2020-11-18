package com.leopo.week5.bizuo3.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.*;

public class HikariUtil {
    private HikariUtil(){
    }

    private static DataSource ds;

    static {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mysql://localhost:3306/jdbc");
        hikariConfig.setUsername("root");
        hikariConfig.setPassword("root");

        ds = new HikariDataSource(hikariConfig);
    }

    public static Connection getConn(){
        try {
            return ds.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static void close(ResultSet rs, Connection cn, PreparedStatement ps){
        try {
            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if (cn != null){
                try {
                    cn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }finally {
                    try {
                        if (ps != null){
                            ps.close();
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        }

    }
}

package com.leopo.week5.bizuo3.util;

import com.leopo.week5.bizuo3.handler.ResultHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcTemplate {

    private JdbcTemplate(){}


    public static void update(String sql,Object ... args) {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        try {
            con = HikariUtil.getConn();
            preparedStatement = con.prepareStatement(sql);
            setParameterParams(preparedStatement, args);
            preparedStatement.executeUpdate();
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }

    public static<T> T query(String sql, ResultHandler<T> resultHandler,Object ... args) {

        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        T handler =null;
        try {
            con = HikariUtil.getConn();
            preparedStatement = con.prepareStatement(sql);
            setParameterParams(preparedStatement, args);
            rs = preparedStatement.executeQuery();
            handler = resultHandler.handler(rs);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return handler;
    }

    private static void setParameterParams(PreparedStatement preparedStatement, Object[] args) throws SQLException {
        if (args != null && args.length > 0){
            for (int i = 0; i < args.length; i++) {
                //设置参数
                preparedStatement.setObject(i + 1, args[i]);
            }
        }
    }


}

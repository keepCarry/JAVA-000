package com.leopo.week5.bizuo3.handler;

import java.sql.ResultSet;

public interface ResultHandler<T> {

    T handler(ResultSet set) throws Exception;
}

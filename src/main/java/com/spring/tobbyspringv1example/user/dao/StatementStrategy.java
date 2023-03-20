package com.spring.tobbyspringv1example.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/* 전략 오브젝트 : 특정 확장 기능 (변하는 부분) */
public interface StatementStrategy {
    PreparedStatement makePreparedStatement(Connection c) throws SQLException;
}

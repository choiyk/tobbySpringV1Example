package com.spring.tobbyspringv1example.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcContext {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    //변하지 않는 공통 부분 = context = 템플릿
    public void workWithStatementStrategy(StatementStrategy stmt) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;

        try{
            c = dataSource.getConnection();

            ps = stmt.makePreparedStatement(c);

            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
        finally {
            if(ps != null) {
                try {
                    ps.close();
                }
                catch (SQLException e) {}
            }
            if(c != null) {
                try {
                    c.close();
                }
                catch (SQLException e) {}
            }
        }
    }

    //클라이언트
    public void executeSql(final String query) throws SQLException {
        workWithStatementStrategy(new StatementStrategy() { //콜백
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                return c.prepareStatement(query);
            }
        });
    }
}

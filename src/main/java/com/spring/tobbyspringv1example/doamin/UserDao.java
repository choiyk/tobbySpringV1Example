package com.spring.tobbyspringv1example.doamin;

import com.spring.tobbyspringv1example.dao.*;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.sql.*;

public class UserDao {
    /*
    스프링의 싱글톤 빈으로 사용되는 클래스를 만들 때는 개별적으로 바뀌는 정보는 로컬 변수로 정의하거나, 파라미터로 주고받으면서 시용하게 해야 한다.
    자신이 사용하는 다른 싱글톤 빈을 저장하려는 용도라면 인스턴스 변수를 사용해도 좋다.
     */
    private DataSource dataSource;
    private JdbcContext jdbcContext;

    /* 생성자를 이용한 의존관계 주입
    public UserDao(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }
     */
    //수정자 메소드를 이용한 의존관계 주입
    public void setDataSource(DataSource dataSource) {
        this.jdbcContext = new JdbcContext();
        this.jdbcContext.setDataSource(dataSource);

        this.dataSource = dataSource;
    }

    public void add(final User user) throws SQLException {
        StatementStrategy st = new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                PreparedStatement ps = c.prepareStatement(
                        "insert into users(id, name, password) values(?,?,?)");
                ps.setString(1, user.getId());
                ps.setString(2, user.getName());
                ps.setString(3, user.getPassword());

                return ps;
            }
        };
        jdbcContext.workWithStatementStrategy(st);
    }


    public User get(String id) throws SQLException {
        //Connection c = connectionMaker.makeConnection();
        Connection c = dataSource.getConnection();
        PreparedStatement ps = c
                .prepareStatement("select * from users where id = ?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();

        User user = null;
        if(rs.next()) {
            user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
        }

        rs.close();
        ps.close();
        c.close();

        if(user == null) throw new EmptyResultDataAccessException(1);
        return user;
    }

    public int getCount() throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            c = dataSource.getConnection();

            ps = c.prepareStatement("select count(*) from users");

            rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
        catch (SQLException e) {
            throw e;
        }
        finally {
            if(rs != null) {
                try {
                    rs.close();
                }
                catch (SQLException e) {}
            }
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

    public void deleteAll() throws SQLException {
        /*StatementStrategy st = new DeleteAllStatement();    //전략 선정
        jdbcContextWithStatementStrategy(st);   //컨텍스트 호출, 전략 오브젝트 전달*/

        jdbcContext.workWithStatementStrategy(new StatementStrategy() { //익명 내부 클래스로 만들어지는 오브젝트 = 콜백
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                PreparedStatement ps = c.prepareStatement("delete from users");
                return ps;
            }
        });
    }
}

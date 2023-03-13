package com.spring.tobbyspringv1example.doamin;

import com.spring.tobbyspringv1example.dao.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDaoJdbc implements UserDao {
    /*
    스프링의 싱글톤 빈으로 사용되는 클래스를 만들 때는 개별적으로 바뀌는 정보는 로컬 변수로 정의하거나, 파라미터로 주고받으면서 시용하게 해야 한다.
    자신이 사용하는 다른 싱글톤 빈을 저장하려는 용도라면 인스턴스 변수를 사용해도 좋다.
     */
    private JdbcTemplate jdbcTemplate;

    /* 생성자를 이용한 의존관계 주입
    public UserDaoJdbc(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }
     */
    //수정자 메소드를 이용한 의존관계 주입
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //RowMapper 콜백을 변수로 만들어두고 공유
    private RowMapper<User> userMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            return user;
        }
    };

    public void add(final User user) {
        this.jdbcTemplate.update("insert into users(id, name, password) values(?,?,?)", user.getId(), user.getName(), user.getPassword());
    }


    public User get(final String id) {
        return this.jdbcTemplate.queryForObject("select * from users where id = ?", this.userMapper, id);
    }

    public List<User> getAll() {
        return this.jdbcTemplate.query("select * from users order by id", this.userMapper);
    }

    public int getCount() {
        return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }

    public void deleteAll() {
        this.jdbcTemplate.update("delete from users");
    }
}

package com.spring.tobbyspringv1example.dao;

import com.spring.tobbyspringv1example.doamin.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)  //Junit 프레임워크의 테스트 실행 방법을 확장.
@ContextConfiguration(locations = "/test-ApplicationContext.xml")    //자동으로 만들어줄 애플리케이션 컨텍스트의 설정파일 위치 지정.
public class UserDaoTest {

    @Autowired private ApplicationContext context;
    @Autowired private UserDao dao;
    @Autowired DataSource dataSource;

    private User user1;
    private User user2;
    private User user3;


    @BeforeEach
    public void setup() {
        /*
        애플리케이션 컨텍스트 : 빈 팩토리를 확장한 IoC 컨테이너.(빈 팩토리 + 스프링이 제공하는 부가 서비스)
        빈 팩토리 : 스프링의 IoC를 담당하는 핵심 컨테이너. 빈 등록, 생성, 조회, 제공 등 빈을 관리.

        //ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        ApplicationContext context = new GenericXmlApplicationContext("ApplicationContext.xml"); */

        this.user1 = new User("gyumee", "박성철", "springno1");
        this.user2 = new User("leegw700", "이길원", "springno2");
        this.user3 = new User("bumjin", "박범진", "springno3");
    }

    @Test
    public void addAndGet() {
        dao.deleteAll();
        assertEquals(dao.getCount(), 0);

        dao.add(user1);
        dao.add(user2);
        assertEquals(dao.getCount(), 2);

        User userget1 = dao.get(user1.getId());
        assertEquals(userget1.getName(), user1.getName());
        assertEquals(userget1.getPassword(), user1.getPassword());

        User userget2 = dao.get(user2.getId());
        assertEquals(userget2.getName(), user2.getName());
        assertEquals(userget2.getPassword(), user2.getPassword());
    }

    @Test
    public void getUserFailure() {
        dao.deleteAll();
        assertEquals(dao.getCount(), 0);

        assertThrows(EmptyResultDataAccessException.class, () -> dao.get("unknown_id"));
    }

    @Test
    public void getAll() {
        dao.deleteAll();
        assertEquals(dao.getCount(), 0);

        List<User> users0 = dao.getAll();
        assertEquals(users0.size(), 0);

        dao.add(user1); //Id : gyumee
        List<User> users1 = dao.getAll();
        assertEquals(users1.size(), 1);
        checkSameUser(user1, users1.get(0));

        dao.add(user2); //Id : leegw700
        List<User> users2 = dao.getAll();
        assertEquals(users2.size(), 2);
        checkSameUser(user1, users2.get(0));
        checkSameUser(user2, users2.get(1));

        dao.add(user3); //Id : bumjin
        List<User> users3 = dao.getAll();
        assertEquals(users3.size(), 3);
        checkSameUser(user3, users3.get(0));
        checkSameUser(user1, users3.get(1));
        checkSameUser(user2, users3.get(2));
    }

    @Test
    public void count() {
        dao.deleteAll();
        assertEquals(dao.getCount(), 0);

        dao.add(user1);
        assertEquals(dao.getCount(), 1);

        dao.add(user2);
        assertEquals(dao.getCount(), 2);

        dao.add(user3);
        assertEquals(dao.getCount(), 3);
    }

    @Test
    public void duplicateKey() {
        dao.deleteAll();

        dao.add(user1);
        assertThrows(DataAccessException.class, () -> {dao.add(user1);});
    }

    @Test
    public void sqlExceptionTransate() {
        dao.deleteAll();

        try {
            dao.add(user1);
            dao.add(user1);
        }
        catch(DuplicateKeyException ex) {
            SQLException sqlException = (SQLException) ex.getCause();
            SQLExceptionTranslator sqlExceptionTranslator = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);

            assertEquals(DuplicateKeyException.class, sqlExceptionTranslator.translate(null, null, sqlException).getClass());
        }
    }

    private void checkSameUser(User user1, User user2) {
        assertEquals(user1.getId(), user2.getId());
        assertEquals(user1.getName(), user2.getName());
        assertEquals(user1.getPassword(), user2.getPassword());
    }
}

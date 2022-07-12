package com.spring.tobbyspringv1example.dao;

import com.spring.tobbyspringv1example.doamin.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDaoTest {

    @Test
    public void addAndGet() throws ClassNotFoundException, SQLException {
        /*
        애플리케이션 컨텍스트 : 빈 팩토리를 확장한 IoC 컨테이너.(빈 팩토리 + 스프링이 제공하는 부가 서비스)
        빈 팩토리 : 스프링의 IoC를 담당하는 핵심 컨테이너. 빈 등록, 생성, 조회, 제공 등 빈을 관리.
        */
        //ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        ApplicationContext context = new GenericXmlApplicationContext("ApplicationContext.xml");
        UserDao dao = context.getBean("userDao", UserDao.class);

        dao.deleteAll();
        assertEquals(dao.getCount(), 0);

        User user = new User();
        user.setId("whiteship");
        user.setName("백기선");
        user.setPassword("married");

        dao.add(user);
        assertEquals(dao.getCount(), 1);

        User user2 = dao.get(user.getId());
        
        assertEquals(user2.getName(), user.getName());
        assertEquals(user2.getPassword(), user.getPassword());
    }
}

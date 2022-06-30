package com.spring.tobbyspringv1example;

import com.spring.tobbyspringv1example.dao.DaoFactory;
import com.spring.tobbyspringv1example.dao.User;
import com.spring.tobbyspringv1example.doamin.UserDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

public class UserdaoTest {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        /*
        애플리케이션 컨텍스트 : 빈 팩토리를 확장한 IoC 컨테이너.(빈 팩토리 + 스프링이 제공하는 부가 서비스)
        빈 팩토리 : 스프링의 IoC를 담당하는 핵심 컨테이너. 빈 등록, 생성, 조회, 제공 등 빈을 관리.
        */
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao dao = context.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("whiteship");
        user.setName("백기선");
        user.setPassword("married");

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User user2 = dao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());

        System.out.println(user2.getId() + " 조회 성공");
    }
}

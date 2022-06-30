package com.spring.tobbyspringv1example.dao;

import com.spring.tobbyspringv1example.doamin.UserDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoFactory {
    //팩토리의 메소드는 UserDao 타입의 오브젝트를 어떻게 만들고 어떻게 준비시킬지 결정한다.
    @Bean
    public UserDao userDao() {
        ConnectionMaker connectionMaker = connectionMaker();
        UserDao dao = new UserDao(connectionMaker);
        return dao;
    }

    @Bean
    public ConnectionMaker connectionMaker() {
        return new SimpleConnectionMaker();
    }
}

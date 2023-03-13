package com.spring.tobbyspringv1example.dao;

import com.spring.tobbyspringv1example.doamin.UserDao;
import com.spring.tobbyspringv1example.doamin.UserDaoJdbc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Deprecated
public class CountingDaoFactory {
    @Bean
    public UserDao userDao() {
        //return new UserDao(connectionMaker());
        UserDao dao = new UserDaoJdbc();
        //dao.setConnectionMaker(connectionMaker()); //UserDao에서 DataSource를 통해 Connection을 가져오면서 더 이상 필요 없게 되었음.
        return dao;
    }

    @Bean
    public ConnectionMaker connectionMaker() {
        return new CountingConnectionMaker(realConnectionMaker());  //부가 기능 추가
    }

    @Bean
    public ConnectionMaker realConnectionMaker() {
        return new SimpleConnectionMaker(); //Connection을 만드는 방법
    }
}

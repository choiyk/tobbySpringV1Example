package com.spring.tobbyspringv1example.dao;

import com.spring.tobbyspringv1example.doamin.UserDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration  //설정정보/설정 메타정보 : IoC 컨테이너에 의해 관리되는 애플리케이션 오브젝트를 생성하고 구성할 때 사용되는 메타정보.
public class DaoFactory {
    //팩토리의 메소드는 UserDao 타입의 오브젝트를 어떻게 만들고 어떻게 준비시킬지 결정한다.
    @Bean   //빈 : 스프링이 IoC 방식으로 관리하는 오브젝트.
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

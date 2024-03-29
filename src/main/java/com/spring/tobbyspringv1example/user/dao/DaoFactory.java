package com.spring.tobbyspringv1example.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

@Configuration  //설정정보/설정 메타정보 : IoC 컨테이너에 의해 관리되는 애플리케이션 오브젝트를 생성하고 구성할 때 사용되는 메타정보.
public class DaoFactory {
    //팩토리의 메소드는 UserDao 타입의 오브젝트를 어떻게 만들고 어떻게 준비시킬지 결정한다.
    @Bean   //빈 : 스프링이 IoC 방식으로 관리하는 오브젝트.
    public UserDao userDao() {
        //ConnectionMaker connectionMaker = connectionMaker();
        //UserDao dao = new UserDao(connectionMaker);
        UserDaoJdbc dao = new UserDaoJdbc();
        //dao.setConnectionMaker(connectionMaker);
        dao.setDataSource(dataSource());
        return dao;
    }

    @Bean
    public ConnectionMaker connectionMaker() {
        //다른 기능으로 변경해야 할 때 이 부분을 새로운 ConnectionMaker 구현 클래스로 변경하면 된다.
        return new SimpleConnectionMaker();
    }

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        try {
            Class driverClass = Class.forName("org.mariadb.jdbc.Driver");
            dataSource.setDriverClass(driverClass);
            dataSource.setUrl("jdbc:mariadb://localhost/tobbyspring?characterEncoding=UTF-8");
            dataSource.setUsername("root");
            dataSource.setPassword("1234");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return dataSource;
    }
}

package com.illia.config;

import java.util.Properties;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;

@Configuration
public class HibernateConfig {


  @Bean
  public DataSource dataSource(@Value("${datasource.url}") String dataSourceUrl) {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("org.h2.Driver");
    dataSource.setUrl(dataSourceUrl);
    return dataSource;
  }

  @Bean
  public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
    LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
    sessionFactory.setDataSource(dataSource);
    sessionFactory.setHibernateProperties(hibernateProperties());
    sessionFactory.setPackagesToScan("com.illia.model");
    return sessionFactory;
  }

  @Bean
  public PlatformTransactionManager transactionManager(SessionFactory sessionFactory){
    var tm = new HibernateTransactionManager();
    tm.setSessionFactory(sessionFactory);
    return tm;
  }

  private Properties hibernateProperties() {
    Properties properties = new Properties();
    properties.setProperty("hibernate.show_sql", "true");
    properties.setProperty("hibernate.cache.use_second_level_cache", "true");
    properties.setProperty("hibernate.cache.use_query_cache", "true");
    properties.setProperty("hibernate.cache.redisson.config", "redisson-config.json");
    properties.setProperty("hibernate.cache.region.factory_class", "org.redisson.hibernate.RedissonRegionFactory");

    return properties;
  }

}

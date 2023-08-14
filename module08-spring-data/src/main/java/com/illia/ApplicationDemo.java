package com.illia;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class ApplicationDemo {

  static Logger logger = LoggerFactory.getLogger(ApplicationDemo.class);

  public static void main(String[] args) {
    var context = new ClassPathXmlApplicationContext("applicationContext.xml");
    logger.info("Context successfully loaded");
  }
}

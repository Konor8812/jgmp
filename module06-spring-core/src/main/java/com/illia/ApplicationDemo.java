package com.illia;


import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplicationDemo {

  public static void main(String[] args) {
     var context = new ClassPathXmlApplicationContext("applicationContext.xml");

  }
}

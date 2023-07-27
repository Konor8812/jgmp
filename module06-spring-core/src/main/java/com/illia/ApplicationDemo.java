package com.illia;


import com.illia.data.DataStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class ApplicationDemo {

  public static void main(String[] args) {
     var context = new ClassPathXmlApplicationContext("applicationContext.xml");
    var bean = context.getBean("dataStorage", DataStorage.class);
    System.out.println(bean.getAll("user:"));
  }
}

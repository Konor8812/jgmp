package com.illia.config;

import com.illia.data.InMemoryDataStorage;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class CustomBeanPostProcessor implements BeanPostProcessor {

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) {
    if (bean instanceof InMemoryDataStorage) {
      ((InMemoryDataStorage<?>) bean).postConstruct();
    }
    return bean;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) {
    return bean;
  }
}
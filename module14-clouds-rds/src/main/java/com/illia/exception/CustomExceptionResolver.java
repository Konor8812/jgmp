package com.illia.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

@Component
public class CustomExceptionResolver implements HandlerExceptionResolver {

  @Override
  public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) {
    var modelAndView = new ModelAndView();
    modelAndView.setViewName("resolvedExceptionTemplate");
    modelAndView.addObject("message", "An exception of " + ex.getClass().getName() + " occurred!");
    return modelAndView;
  }
}

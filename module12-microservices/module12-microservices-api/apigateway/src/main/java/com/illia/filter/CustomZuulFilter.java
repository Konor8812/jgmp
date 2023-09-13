package com.illia.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

@Component
public class CustomZuulFilter extends ZuulFilter {

  @Override
  public String filterType() {
    return "pre";
  }

  @Override
  public int filterOrder() {
    return 1;
  }

  @Override
  public boolean shouldFilter() {
    return true;
  }

  @Override
  public Object run() {
    System.out.println("filter run");
    var requestContext = RequestContext.getCurrentContext();
    requestContext.addZuulRequestHeader("WasFilteredByCustomZuulFilter", "true");
    return null;
  }
}

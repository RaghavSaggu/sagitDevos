package com.sagitDevos.sagitDevos.advice;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Component
@Aspect
@EnableAspectJAutoProxy
public class MyControllerHelper {
    @Before("execution(public StatusDTO createUser())")
    public void log() {
        System.out.println("printing from MyControllerHelper");
    }
}

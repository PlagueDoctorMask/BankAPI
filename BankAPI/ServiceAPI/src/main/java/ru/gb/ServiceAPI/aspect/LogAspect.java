package ru.gb.ServiceAPI.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Component
public class LogAspect {

    @Around("@annotation(TrackUserAction)")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {

        Object returnedByMethod = joinPoint.proceed();

        String[] parts = joinPoint.getThis().toString().split("\\.");
        String serviceName = parts[parts.length - 1];
        String role = serviceName.replace("Service", "");

        System.out.println(" ");
        System.out.println("Time: "+ LocalDateTime.now());
        System.out.println("Method: "+joinPoint.getSignature().getName());
        System.out.println("Parameters: "+ Arrays.toString(joinPoint.getArgs()));
        System.out.println("User: "+role);
        System.out.println(" ");


        return returnedByMethod;

    }
}

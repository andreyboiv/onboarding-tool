package com.boivalenko.businessapp.web.auth.aop;

import lombok.extern.java.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
@Log
public class AuthLoggingAspect {

    //Der Aspekt wird für alle Methoden bzw. für alle Klassen aus dem Controller-Paket ausgeführt
    @Around("execution(* com.boivalenko.businessapp.web.auth.controller..*(..)))")
    public Object profileControllerMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable
    {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

        // welche Klasse und Methode ausgeführt wird
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        log.info("-------- Executing "+ className + "." + methodName + "   ----------- ");

        StopWatch countdown = new StopWatch();
        countdown.start();

        // die Methode wird ausgeführt...
        // WICHTIG: Ohne diese Zeile wird die Methode nicht ausgeführt!!!
        Object result = proceedingJoinPoint.proceed();

        countdown.stop();
        log.info("-------- Execution time of "+ className + "." + methodName + " :: " + countdown.getTotalTimeMillis() + " ms");

        return result;
    }

}

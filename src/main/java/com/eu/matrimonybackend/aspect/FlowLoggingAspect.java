package com.eu.matrimonybackend.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Traces every public method entry/exit/exception on @RestController and @Service beans.
 * Scoping the pointcut to those two stereotypes (rather than a package-wide execution())
 * naturally excludes DTOs, entities, and their getters/setters, since none of those are
 * annotated with @RestController or @Service.
 */
@Aspect
@Component
@Slf4j
public class FlowLoggingAspect {

    @Around("(within(@org.springframework.web.bind.annotation.RestController *) "
            + "|| within(@org.springframework.stereotype.Service *)) "
            + "&& execution(public * *(..))")
    public Object logMethodFlow(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        log.info("==> ENTER: {}.{}()", className, methodName);
        long startTime = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long elapsedMs = System.currentTimeMillis() - startTime;
            log.info("<== EXIT: {}.{}() - Completed in {} ms", className, methodName, elapsedMs);
            return result;
        } catch (Throwable ex) {
            log.error("<== EXCEPTION in {}.{}(): {}", className, methodName, ex.getMessage());
            throw ex;
        }
    }
}

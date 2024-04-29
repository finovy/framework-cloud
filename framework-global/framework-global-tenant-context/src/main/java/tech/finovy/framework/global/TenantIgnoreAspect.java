package tech.finovy.framework.global;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@Slf4j
public class TenantIgnoreAspect {
    @Around("@annotation(tenantIgnore)")
    public Object around(ProceedingJoinPoint joinPoint, TenantIgnore tenantIgnore) throws Throwable {
        TenantContext.setIgnoreTenant(tenantIgnore.ignore());
        return joinPoint.proceed();
    }

}

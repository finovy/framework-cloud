package tech.finovy.framework.global.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *    Convert request parameters from underscore format to camel case.
 *    Usage:
 *    1. In Spring Boot, add `UnderlineToCamelArgumentResolver` to `argumentResolvers` in WebConfig:
 *
 *       @Override
 *       public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
 *           argumentResolvers.add(new UnderlineToCamelArgumentResolver());
 *       }
 *
 *    2. In the controller, annotate the method parameter with @ParameterConvert, e.g.:
 *
 *       userList(@ParameterConvert UserReq userReq)
 */
@Target(value = ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ParameterConvert {

}

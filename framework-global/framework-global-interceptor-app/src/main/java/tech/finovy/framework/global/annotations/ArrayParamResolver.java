package tech.finovy.framework.global.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * When receiving a parameter via a GET request from the frontend like 'url?list=["aa","bb","cc"]',
 * Node.js can interpret it as an array, but Spring MVC will parse it as a string.
 * This annotation also includes the functionality of converting underscores to camel case,
 * so there is no need for the @ParameterConvert annotation.
 */
@Target(value = ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ArrayParamResolver {
}

package tech.finovy.framework.global.resolver;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import tech.finovy.framework.global.annotations.ArrayParamResolver;
import tech.finovy.framework.global.annotations.PassToken;

import java.util.List;

@Slf4j
public class ResolverTest {

    @PassToken
    public void test1(@Validated @ArrayParamResolver Person person) {
        log.info("{}", person);
    }

    public void test2(@Validated @ArrayParamResolver Person person) {
        log.info("{}", person);
    }

    public void test3(@Validated @ArrayParamResolver Info info) {
        log.info("{}", info);
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Person {

        String userName;

        int age;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Info {

        List<String> userName;

        int age;
    }

}

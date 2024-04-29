package tech.finovy.framework.common;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Morty.Wang
 * @since 2023/2/27 14:04
 **/
public class RandomStringUtils {

    private static final List<Integer> VALID_PWD_CHARS = new ArrayList<>();

    static {
        IntStream.rangeClosed('0', '9').forEach(VALID_PWD_CHARS::add);    // 0-9
        IntStream.rangeClosed('A', 'Z').forEach(VALID_PWD_CHARS::add);    // A-Z
        IntStream.rangeClosed('a', 'z').forEach(VALID_PWD_CHARS::add);    // a-z
//        IntStream.rangeClosed('!', '*').forEach(VALID_PWD_CHARS::add);    // !-*
    }

    public static String randomAlphanumeric(int count) {
        return new SecureRandom().ints(count, 0, VALID_PWD_CHARS.size())
                .map(VALID_PWD_CHARS::get)
                .mapToObj(x -> String.valueOf((char) x)).collect(Collectors.joining());
    }
}

package tech.finovy.framework.security.auth.extend;

public interface CustomPasswordEncoder {

    String encode(CharSequence rawPassword);

    boolean matches(CharSequence rawPassword, String encodedPassword);

}

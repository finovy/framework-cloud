package tech.finovy.framework.security.oidc.core.account;

import org.springframework.security.crypto.password.PasswordEncoder;
import tech.finovy.framework.security.oidc.extend.CustomPasswordEncoder;

public class CustomPasswordEncoderEnhance implements PasswordEncoder {

    private final CustomPasswordEncoder customPasswordEncoder;

    public CustomPasswordEncoderEnhance(CustomPasswordEncoder customPasswordEncoder) {
        this.customPasswordEncoder = customPasswordEncoder;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return customPasswordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return customPasswordEncoder.matches(rawPassword, encodedPassword);
    }
}

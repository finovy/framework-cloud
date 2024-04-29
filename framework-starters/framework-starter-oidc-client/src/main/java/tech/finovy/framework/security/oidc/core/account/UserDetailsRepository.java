package tech.finovy.framework.security.oidc.core.account;

import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import tech.finovy.framework.security.oidc.extend.UsernameAndPasswordService;

import java.util.Objects;

/**
 * 代理 {@link org.springframework.security.provisioning.UserDetailsManager} 所有功能
 */
public class UserDetailsRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsRepository.class);

    private final UsernameAndPasswordService userService;

    public UserDetailsRepository(@Nullable UsernameAndPasswordService userService) {
        this.userService = userService;
    }

    /**
     * check user
     *
     * @param username the username
     * @return the boolean
     */
    public boolean userExists(String username) {
        return userService.userExists(username);
    }


    /**
     * load user
     *
     * @param username the username
     * @return the user details
     * @throws UsernameNotFoundException the username not found exception
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = userService.loadUserByUsername(username);
        if (Objects.nonNull(user)) {
            return user;
        }
        // 无用户则提示未授权
        throw new UsernameNotFoundException("error account/password", new LockedException("deleted"));
    }

}

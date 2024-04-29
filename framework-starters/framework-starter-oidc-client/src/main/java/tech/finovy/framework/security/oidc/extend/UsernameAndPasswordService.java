package tech.finovy.framework.security.oidc.extend;

import org.springframework.security.core.userdetails.UserDetails;

public interface UsernameAndPasswordService {

    boolean userExists(String username);

    UserDetails loadUserByUsername(String username);
}

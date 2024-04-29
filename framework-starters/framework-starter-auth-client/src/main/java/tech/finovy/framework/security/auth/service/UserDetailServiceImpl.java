package tech.finovy.framework.security.auth.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import tech.finovy.framework.security.auth.extend.UserDetailService;

public class UserDetailServiceImpl implements UserDetailService {
    @Override
    public String getCurrentUsername() {
        // 获取当前认证的用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 检查用户是否已认证
        if (authentication != null && authentication.isAuthenticated()) {
            // 获取用户详细信息
            Object principal = authentication.getPrincipal();
            // 检查 principal 是否是 UserDetails 类型
            if (principal instanceof UserDetails) {
                return ((UserDetails) principal).getUsername();
            } else {
                // 如果 principal 不是 UserDetails 类型，可以直接使用 toString()
                return principal.toString();
            }
        }
        return null; // 如果用户未认证，则返回 null
    }

    @Override
    public String getCurrentUserEmail() {
        return getUserInfo().getEmail();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof DefaultOidcUser userDetails) {
            return userDetails.getUserInfo();
        }
        throw new IllegalStateException("can not find user");
    }
}

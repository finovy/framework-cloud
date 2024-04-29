package tech.finovy.framework.security.oidc.core.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import tech.finovy.framework.security.oidc.core.account.LoginPostProcessor;
import tech.finovy.framework.security.oidc.core.account.LoginTypeEnum;
import tech.finovy.framework.security.oidc.core.account.ParameterRequestWrapper;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY;
import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY;

/**
 * 预登录控制器
 *
 */
public class PreLoginFilter extends GenericFilterBean {

    private static final String LOGIN_TYPE_KEY = "loginType";

    private final RequestMatcher requiresAuthenticationRequestMatcher;
    private final Map<LoginTypeEnum, LoginPostProcessor> processors = new HashMap<>();

    public PreLoginFilter(String loginProcessingUrl, Collection<LoginPostProcessor> loginPostProcessors) {
        Assert.notNull(loginProcessingUrl, "loginProcessingUrl must not be null");
        requiresAuthenticationRequestMatcher = new AntPathRequestMatcher(loginProcessingUrl, "POST");
        LoginPostProcessor loginPostProcessor = defaultLoginPostProcessor();
        processors.put(loginPostProcessor.getLoginTypeEnum(), loginPostProcessor);
        if (!CollectionUtils.isEmpty(loginPostProcessors)) {
            loginPostProcessors.forEach(element -> processors.put(element.getLoginTypeEnum(), element));
        }
    }

    /**
     * 默认Form .
     * @return the login post processor
     */
    private LoginPostProcessor defaultLoginPostProcessor() {
        return new LoginPostProcessor() {
            @Override
            public LoginTypeEnum getLoginTypeEnum() {
                return LoginTypeEnum.FORM;
            }
            @Override
            public String obtainUsername(ServletRequest request) {
                return request.getParameter(SPRING_SECURITY_FORM_USERNAME_KEY);
            }
            @Override
            public String obtainPassword(ServletRequest request) {
                return request.getParameter(SPRING_SECURITY_FORM_PASSWORD_KEY);
            }
        };
    }

    private LoginTypeEnum getTypeFromReq(ServletRequest request) {
        String parameter = request.getParameter(LOGIN_TYPE_KEY);
        if (!StringUtils.hasLength(parameter)) {
            return LoginTypeEnum.FORM;
        }
        int i = Integer.parseInt(parameter);
        LoginTypeEnum[] values = LoginTypeEnum.values();
        return values[i];
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ParameterRequestWrapper parameterRequestWrapper = new ParameterRequestWrapper((HttpServletRequest) request);
        if (requiresAuthenticationRequestMatcher.matches((HttpServletRequest) request)) {
            LoginTypeEnum typeFromReq = getTypeFromReq(request);
            LoginPostProcessor loginPostProcessor = processors.get(typeFromReq);
            String username = loginPostProcessor.obtainUsername(request);
            String password = loginPostProcessor.obtainPassword(request);
            parameterRequestWrapper.setAttribute(SPRING_SECURITY_FORM_USERNAME_KEY, username);
            parameterRequestWrapper.setAttribute(SPRING_SECURITY_FORM_PASSWORD_KEY, password);
        }
        chain.doFilter(parameterRequestWrapper, response);
    }
}

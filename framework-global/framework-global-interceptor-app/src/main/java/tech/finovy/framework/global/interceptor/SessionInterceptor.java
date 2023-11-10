package tech.finovy.framework.global.interceptor;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import tech.finovy.framework.distributed.cache.api.CacheService;
import tech.finovy.framework.global.Constant;
import tech.finovy.framework.global.annotations.PassToken;
import tech.finovy.framework.global.utils.JwtUtil;
import tech.finovy.framework.redis.entity.cache.entity.CachePack;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * request interceptor
 */
@Slf4j
@Service
public class SessionInterceptor implements HandlerInterceptor {

    @DubboReference
    private CacheService cacheService;

    /**
     * 1. Retrieve the token from the HTTP request header,
     * 2. Check if mapped to a method,
     * 3. Check for the presence of 'passtoken' annotation; if present, skip authentication,
     * 4. Check for annotations that require user login; if present, retrieve and validate,
     * 5. If authentication is successful, allow access; otherwise, return relevant error messages.
     */
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        // If not mapped to a method, pass through directly.
        if (!(object instanceof HandlerMethod handlerMethod)) {
            return true;
        }
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        // Retrieve the token from the HTTP request header.
        String token = httpServletRequest.getHeader("authorization");
        if (StringUtils.isEmpty(token)) {
            log.info("token not exist, Please try again!");
            return false;
        }
        // Retrieve the token from the cache.
        CachePack<String> cacheJwt = cacheService.getCache(String.class, token, true);
        Claims claims = JwtUtil.parseJwt(cacheJwt.getData());
        String appId = (String) claims.get(Constant.TOKEN_HEADER_APPID);
        RpcContext.getContext().setAttachment(Constant.APPID, appId);
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }
}

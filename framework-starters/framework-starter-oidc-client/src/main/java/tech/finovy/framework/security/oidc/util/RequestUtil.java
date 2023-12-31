package tech.finovy.framework.security.oidc.util;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import tech.finovy.framework.security.oidc.common.SecurityConstants;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * @author Dax
 * @since 13:41  2019-04-09
 */
public class RequestUtil {
    private static final Logger log = LoggerFactory.getLogger(RequestUtil.class);

    private RequestUtil() {
    }

    /**
     * 获取 request body
     * @param request req
     * @return body str
     */
    public static String obtainBody(ServletRequest request) {


        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            br = request.getReader();
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            br.close();
        } catch (IOException e) {
            log.error(" requestBody read error");
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    log.error(" close io error");
                }
            }
        }
        return sb.toString();

    }

    public static String obtainAuthorization(HttpServletRequest request,
                                       String headerName, String parameterName) {
        String token = request.getHeader(headerName);
        if (!StringUtils.hasLength(token)) {
            token = request.getParameter(parameterName);
        }
        if (!StringUtils.hasText(token)) {
            return null;
        }
        int index = token.indexOf(SecurityConstants.AUTHORIZATION_BEARER + " ");
        return index >= 0 ? token.substring(index + 7).trim() : token;
    }
}

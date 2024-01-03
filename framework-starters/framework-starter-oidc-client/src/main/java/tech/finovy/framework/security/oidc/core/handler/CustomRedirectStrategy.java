package tech.finovy.framework.security.oidc.core.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.log.LogMessage;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;
import tech.finovy.framework.security.oidc.util.ResponseUtil;
import tech.finovy.framework.security.oidc.util.RestBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Ryan Luo
 * @Date: 2023/12/27 18:30
 */
public class CustomRedirectStrategy implements RedirectStrategy {
    protected final Log logger = LogFactory.getLog(getClass());

    private boolean contextRelative;

    /**
     * Redirects the response to the supplied URL.
     * <p>
     * If <tt>contextRelative</tt> is set, the redirect value will be the value after the
     * request context path. Note that this will result in the loss of protocol
     * information (HTTP or HTTPS), so will cause problems if a redirect is being
     * performed to change to HTTPS, for example.
     */
    @Override
    public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
        String redirectUrl = calculateRedirectUrl(request.getContextPath(), url);
        redirectUrl = response.encodeRedirectURL(redirectUrl);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(LogMessage.format("Redirecting to %s", redirectUrl));
        }
        Map<String, Object> map = new HashMap<>(3);
        map.put("redirect_url", redirectUrl);
        ResponseUtil.responseJsonWriter(response, RestBody.okData(map, "SUCCESS"));
    }


    protected String calculateRedirectUrl(String contextPath, String url) {
        if (!UrlUtils.isAbsoluteUrl(url)) {
            if (isContextRelative()) {
                return url;
            }
            return contextPath + url;
        }
        // Full URL, including http(s)://
        if (!isContextRelative()) {
            return url;
        }
        Assert.isTrue(url.contains(contextPath), "The fully qualified URL does not include context path.");
        // Calculate the relative URL from the fully qualified URL, minus the last
        // occurrence of the scheme and base context.
        url = url.substring(url.lastIndexOf("://") + 3);
        url = url.substring(url.indexOf(contextPath) + contextPath.length());
        if (url.length() > 1 && url.charAt(0) == '/') {
            url = url.substring(1);
        }
        return url;
    }

    /**
     * If <tt>true</tt>, causes any redirection URLs to be calculated minus the protocol
     * and context path (defaults to <tt>false</tt>).
     */
    public void setContextRelative(boolean useRelativeContext) {
        this.contextRelative = useRelativeContext;
    }

    /**
     * Returns <tt>true</tt>, if the redirection URL should be calculated minus the
     * protocol and context path (defaults to <tt>false</tt>).
     */
    protected boolean isContextRelative() {
        return this.contextRelative;
    }
}

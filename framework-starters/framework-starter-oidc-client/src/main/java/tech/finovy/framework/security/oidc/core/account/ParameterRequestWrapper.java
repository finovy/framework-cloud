package tech.finovy.framework.security.oidc.core.account;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class ParameterRequestWrapper extends HttpServletRequestWrapper {

    public ParameterRequestWrapper(HttpServletRequest request ) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
       return (String) super.getAttribute(name);
    }
}

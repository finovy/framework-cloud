package tech.finovy.framework.transaction.tcc.client.core;


import tech.finovy.framework.transaction.tcc.client.auth.SecurityContent;

public interface DispatchHandler {

    boolean dispatch(SecurityContent content, String typeName, String methodName);

}

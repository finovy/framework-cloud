package tech.finovy.framework.token.api;

import tech.finovy.framework.token.entity.AccessToken;

public interface TokenService {
    AccessToken creatAccessToken(String appId,String secret);
    AccessToken clearAccessToken(String token);
}

package tech.finovy.framework.common;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Provider;

public class ProviderInstance {
    private ProviderInstance() {
    }

    private static final Provider BOUNCY_CASTLE_PROVIDER =new BouncyCastleProvider();
    public static Provider getInstance(){
        return BOUNCY_CASTLE_PROVIDER;
    }
}

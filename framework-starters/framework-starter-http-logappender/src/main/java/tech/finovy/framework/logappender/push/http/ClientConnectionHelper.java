package tech.finovy.framework.logappender.push.http;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientConnectionHelper {

    private final Map<String, ClientConnectionContainer> mAllConnections;

    private ClientConnectionHelper() {
        mAllConnections = new ConcurrentHashMap<>();
        (new Thread(new ClientConnectionUpdateThread(this))).start();
    }

    public static ClientConnectionHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public ClientConnectionContainer getConnectionContainer(String endpoint, String accessId, String accessKey) {
        String key = endpoint + "#" + accessId;
        if (!mAllConnections.containsKey(key)) {
            ClientConnectionContainer container = new ClientConnectionContainer();
            container.init(endpoint, accessId, accessKey);
            mAllConnections.put(key, container);
            return container;
        } else {
            return mAllConnections.get(key);
        }
    }

    private static class SingletonHolder {
        private static final ClientConnectionHelper INSTANCE = new ClientConnectionHelper();
    }

    class ClientConnectionUpdateThread implements Runnable {
        ClientConnectionHelper mHelper;

        ClientConnectionUpdateThread(ClientConnectionHelper helper) {
            mHelper = helper;
        }

        @Override
        public void run() {
            while (true) {
                for (Map.Entry<String, ClientConnectionContainer> entry : mHelper.mAllConnections.entrySet()) {
                    entry.getValue().updateConnections();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}

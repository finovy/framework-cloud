package tech.finovy.framework.logappender.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public final class NetworkUtils {
    private NetworkUtils() {
    }

    public static boolean isIPAddr(String ipAddress) {
        if (ipAddress != null && !ipAddress.isEmpty()) {
            try {
                String[] tokens = ipAddress.split("\\.");
                if (tokens.length != 4) {
                    return false;
                } else {
                    String[] var2 = tokens;
                    int var3 = tokens.length;

                    for (int var4 = 0; var4 < var3; ++var4) {
                        String token = var2[var4];
                        int i = Integer.parseInt(token);
                        if (i < 0 || i > 255) {
                            return false;
                        }
                    }

                    return true;
                }
            } catch (Exception var7) {
                return false;
            }
        } else {
            return false;
        }
    }

    public static String getLocalMachineIP() {
        try {
            Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();

            while (true) {
                NetworkInterface ni;
                do {
                    if (!networkInterfaces.hasMoreElements()) {
                        return null;
                    }

                    ni = (NetworkInterface) networkInterfaces.nextElement();
                } while (!ni.isUp());

                Enumeration addresses = ni.getInetAddresses();

                while (addresses.hasMoreElements()) {
                    InetAddress address = (InetAddress) addresses.nextElement();
                    if (!address.isLinkLocalAddress() && address.getHostAddress() != null) {
                        String ipAddress = address.getHostAddress();
                        if (!ipAddress.equals("127.0.0.1") && isIPAddr(ipAddress)) {
                            return ipAddress;
                        }
                    }
                }
            }
        } catch (SocketException var5) {
            return null;
        }
    }
}

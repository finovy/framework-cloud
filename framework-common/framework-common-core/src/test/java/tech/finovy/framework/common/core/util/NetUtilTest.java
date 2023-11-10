package tech.finovy.framework.common.core.util;


import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;

import static org.assertj.core.api.Assertions.assertThat;

class NetUtilTest {

    @Test
    void testToStringAddress1() {
        assertThat(NetUtil.toStringAddress(new InetSocketAddress("localhost", 80))).isEqualTo("127.0.0.1:80");
    }

    @Test
    void testToIpAddress() {
        assertThat(NetUtil.toIpAddress(new InetSocketAddress("localhost", 80))).isNotNull();
    }

    @Test
    void testToStringAddress2() {
        assertThat(NetUtil.toStringAddress(new InetSocketAddress("localhost", 80))).isNotNull();
    }

    @Test
    void testToInetSocketAddress() {
        assertThat(NetUtil.toInetSocketAddress("address")).isNotNull();
    }

    @Test
    void testToLong() {
        assertThat(NetUtil.toLong("127.0.0.1")).isEqualTo(139637976793088L);
    }

    @Test
    void testGetLocalIp() {
        assertThat(NetUtil.getLocalIp("preferredNetworks")).isNotNull();
    }

    @Test
    void testGetLocalHost() {
        assertThat(NetUtil.getLocalHost()).isNotNull();
    }

    @Test
    void testGetLocalAddress() throws Exception {
        assertThat(NetUtil.getLocalAddress("preferredNetworks")).isNotNull();
    }

    @Test
    void testValidAddress() {
        // Setup
        // Run the test
        NetUtil.validAddress(new InetSocketAddress("localhost", 80));

        // Verify the results
    }

    @Test
    void testIsValidIp() {
        assertThat(NetUtil.isValidIp("127.0.0.1", false)).isFalse();
    }
}

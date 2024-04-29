package tech.finovy.framework.global.utils;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JwtUtilTest {

    @Test
    void testParseJwt() throws Exception {
        // Setup
        final Claims expectedResult = null;

        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJYLUF1dGgtU2lnbmF0dXJlIjoiZmJjMjZlZTgwYjU2NjliMmIyMGFiMjA1YmIwYmNiZDMiLCJYLUF1dGgtVGltZXN0YW1wIjoxNzA3MjA1MDE5OTMzLCJYLUF1dGgtTm9uY2UiOiJiYWQyYTlkMS0xMGZjLTRiZDItYWM4NC1iOWRkM2I1YjczZGEiLCJYLUF1dGgtQXBwaWQiOiJhcHBpZCIsImp0aSI6ImZhYzdmN2Q4LTZkZjctNGI4Ni1hODkwLWFhMWQ2MDkxZTA4ZCIsImlhdCI6MTcwNzIwNTAxOSwic3ViIjoiYXBwaWQifQ.q488EczO5aCADoEifylSmad2pje0q2V4IyZU0GPUwzY";

        // Run the test
        final Claims result = JwtUtil.parseJwt(jwt);

        assertEquals("appid",result.get("X-Auth-Appid"));
    }

    @Test
    void testParseJwt_ThrowsException() {
        // Setup
        // Run the test
        assertThrows(Exception.class, () -> JwtUtil.parseJwt("jwt"));
    }

    @Test
    void testCreateJwt() throws Exception {
        assertNotNull( JwtUtil.createJwt("appid", "userJson", 10000000L));
    }

    @Test
    void testGeneralKey() {
        // Setup
        // Run the test
        final SecretKey result = JwtUtil.generalKey("projecttechfinovyabcdefghijklmnopqrstuvwxyz");

        // Verify the results
        assertNotNull(result);
    }
}

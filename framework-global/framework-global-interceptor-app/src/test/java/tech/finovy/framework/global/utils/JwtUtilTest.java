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

        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJYLUF1dGgtU2lnbmF0dXJlIjoiNmI5OTdmMWZkNGQ1ZTZkZmFiNGZkNjFjNWY0NTkzNDciLCJzdWIiOiJ1c2VySnNvbiIsIlgtQXV0aC1UaW1lc3RhbXAiOjE2OTcyODkzMzIxNDEsIlgtQXV0aC1Ob25jZSI6IjM0NmZhODgzLThhZjEtNGViOS04YmJkLTk4NGQ1MDMxY2FhMyIsImV4cCI6MzE3MDU3Mjg5MzMyLCJpYXQiOjE2OTcyODkzMzIsImp0aSI6IjMzMzM3Njk1LTRmNmMtNDk0NC04ZWMxLTY0MmJkMTBmMTg3MiIsIlgtQXV0aC1BcHBpZCI6ImFwcGlkIn0.xaJfvR14JytpFhs6-6ZZQVVqgAr7A27JSivmqOwDR2o";

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
        final SecretKey result = JwtUtil.generalKey("project");

        // Verify the results
        assertNotNull(result);
    }
}

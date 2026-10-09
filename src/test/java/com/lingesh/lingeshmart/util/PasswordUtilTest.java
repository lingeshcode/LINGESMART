package com.lingesh.lingeshmart.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PasswordUtilTest {

    @Test
    void testHashPasswordAndVerification() {
        String plainPassword = "securePassword123";
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);

        assertNotNull(hashedPassword);
        assertNotEquals(plainPassword, hashedPassword);
        assertTrue(PasswordUtil.checkPassword(plainPassword, hashedPassword));
        assertFalse(PasswordUtil.checkPassword("wrongPassword", hashedPassword));
    }

    @Test
    void testInvalidInputs() {
        assertThrows(IllegalArgumentException.class, () -> PasswordUtil.hashPassword(""));
        assertFalse(PasswordUtil.checkPassword(null, "someHash"));
    }
}

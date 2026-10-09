package com.lingesh.lingeshmart.util;

import com.lingesh.lingeshmart.exception.ValidationException;
import java.math.BigDecimal;
import java.util.regex.Pattern;

public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    private ValidationUtil() {}

    public static void validateEmail(String email) {
        if (isNullOrBlank(email) || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("Invalid email format");
        }
    }

    public static void validateNotEmpty(String fieldName, String value) {
        if (isNullOrBlank(value)) {
            throw new ValidationException(fieldName + " is required and cannot be empty");
        }
    }

    public static void validatePositivePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Price must be greater than zero");
        }
    }

    public static void validateNonNegativeQuantity(Integer qty) {
        if (qty == null || qty < 0) {
            throw new ValidationException("Quantity cannot be negative");
        }
    }

    public static boolean isNullOrBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}

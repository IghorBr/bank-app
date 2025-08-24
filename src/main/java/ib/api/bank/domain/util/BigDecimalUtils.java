package ib.api.bank.domain.util;

import java.math.BigDecimal;

public class BigDecimalUtils {

    public static boolean lessThanOrEqual(BigDecimal value, BigDecimal limit) {
        if (value == null || limit == null) {
            return false;
        }
        return value.compareTo(limit) <= 0;
    }

    public static boolean greaterThanOrEqual(BigDecimal value, BigDecimal limit) {
        if (value == null || limit == null) {
            return false;
        }
        return value.compareTo(limit) >= 0;
    }

    public static boolean equal(BigDecimal value, BigDecimal limit) {
        if (value == null || limit == null) {
            return false;
        }
        return value.compareTo(limit) == 0;
    }

    public static boolean lessThan(BigDecimal value, BigDecimal limit) {
        if (value == null || limit == null) {
            return false;
        }
        return value.compareTo(limit) < 0;
    }

    public static boolean greaterThan(BigDecimal value, BigDecimal limit) {
        if (value == null || limit == null) {
            return false;
        }
        return value.compareTo(limit) > 0;
    }

    public static boolean isZeroOrNull(BigDecimal value) {
        return value == null || value.compareTo(BigDecimal.ZERO) == 0;
    }

    public static boolean isPositive(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean isNegative(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) < 0;
    }
}

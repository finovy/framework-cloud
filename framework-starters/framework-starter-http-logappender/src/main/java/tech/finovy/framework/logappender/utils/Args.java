package tech.finovy.framework.logappender.utils;

import java.util.Collection;

public final class Args {
    private Args() {
    }

    public static void notNull(final Object value, final String name) {
        if (value == null) {
            throw new IllegalArgumentException("[" + name + "] must not be null");
        }
    }

    public static void notNullOrEmpty(final Collection collection, final String name) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException("[" + name + "] must not be null or empty!");
        }
    }

    public static void notNullOrEmpty(final String value, final String name) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("[" + name + "] must not be null or empty!");
        }
    }

    public static void check(final boolean expression, final String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void checkDuration(String duration) {
        notNullOrEmpty(duration, "duration");
        try {
            AbstractUtils.parseDuration(duration);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid duration: " + duration, ex);
        }
    }
}

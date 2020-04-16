package io.github.portlek.configs.yaml.utils;

/**
 * Utils for casting number types to other number types
 */
public final class NumberConversions {

    private NumberConversions() {
    }

    public static int toInt(final Object object) {
        if (object instanceof Number) {
            return ((Number) object).intValue();
        }

        try {
            return Integer.parseInt(object.toString());
        } catch (final NumberFormatException | NullPointerException ignored) {
        }
        return 0;
    }

    public static double toDouble(final Object object) {
        if (object instanceof Number) {
            return ((Number) object).doubleValue();
        }

        try {
            return Double.parseDouble(object.toString());
        } catch (final NumberFormatException | NullPointerException ignored) {
        }
        return 0;
    }

    public static float toFloat(final Object object) {
        if (object instanceof Number) {
            return ((Number) object).floatValue();
        }

        try {
            return Float.parseFloat(object.toString());
        } catch (final NumberFormatException | NullPointerException ignored) {
        }
        return 0;
    }

    public static long toLong(final Object object) {
        if (object instanceof Number) {
            return ((Number) object).longValue();
        }

        try {
            return Long.parseLong(object.toString());
        } catch (final NumberFormatException | NullPointerException ignored) {
        }
        return 0;
    }

}

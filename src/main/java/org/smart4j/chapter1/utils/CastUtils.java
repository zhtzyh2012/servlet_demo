package org.smart4j.chapter1.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 转型工具类
 *
 * @author neutron
 */
public class CastUtils {

    public static String castString(Object object) {
        return castString(object, "");
    }

    public static String castString(Object obj, String defaultVal) {
        return Objects.nonNull(obj) ? obj.toString() : defaultVal;
    }

    public static double castDouble(Object object) {
        return castDouble(object, 0);
    }

    public static double castDouble(Object obj, double defaultVal) {
        double doubleVal = defaultVal;
        if (Objects.nonNull(obj)) {
            String strValue = castString(obj);
            if (StringUtils.isNoneBlank(strValue)) {
                try {
                    doubleVal = Double.parseDouble(strValue);
                } catch (NumberFormatException e) {
                    doubleVal = defaultVal;
                }
            }
        }
        return doubleVal;
    }

    public static long castLong(Object object) {
        return castLong(object, 0);
    }

    public static long castLong(Object obj, long defaultVal) {
        long longVal = defaultVal;
        if (Objects.nonNull(obj)) {
            String strValue = castString(obj);
            if (StringUtils.isNoneBlank(strValue)) {
                try {
                    longVal = Long.parseLong(strValue);
                } catch (NumberFormatException e) {
                    longVal = defaultVal;
                }
            }
        }
        return longVal;
    }

    public static int castInt(Object object) {
        return castInt(object, 0);
    }

    public static int castInt(Object obj, int defaultVal) {
        int intVal = defaultVal;
        if (Objects.nonNull(obj)) {
            String strValue = castString(obj);
            if (StringUtils.isNoneBlank(strValue)) {
                try {
                    intVal = Integer.parseInt(strValue);
                } catch (NumberFormatException e) {
                    intVal = defaultVal;
                }
            }
        }
        return intVal;
    }

    public static boolean castBoolean(Object object) {
        return castBoolean(object, false);
    }

    public static boolean castBoolean(Object obj, boolean defaultVal) {
        boolean booleanVal = defaultVal;
        if (Objects.nonNull(obj)) {
            String strValue = castString(obj);
            if (StringUtils.isNoneBlank(strValue)) {
                try {
                    booleanVal = Boolean.parseBoolean(strValue);
                } catch (NumberFormatException e) {
                    booleanVal = defaultVal;
                }
            }
        }
        return booleanVal;
    }

}

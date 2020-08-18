package org.thirteen.datamation.util;

import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.Map;

/**
 * @author Aaron.Sun
 * @description 集合工具类
 * @date Created in 2020/8/18 17:24
 * @modified by
 */
public class CollectionUtils extends org.springframework.util.CollectionUtils {

    public static boolean isNotEmpty(@Nullable Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static boolean isNotEmpty(@Nullable Map<?, ?> map) {
        return !isEmpty(map);
    }
}

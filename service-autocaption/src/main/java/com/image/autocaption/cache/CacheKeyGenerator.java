package com.image.autocaption.cache;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;


/**
 * The utility which creates bytes image array as the caching key.
 */
@Component(value = "cacheKeyGenerator")
public class CacheKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        byte[] image = (byte[]) params[0];
        return Arrays.hashCode(image);
    }
}

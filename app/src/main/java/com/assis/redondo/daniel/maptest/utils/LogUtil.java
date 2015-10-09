package com.assis.redondo.daniel.maptest.utils;

/**
 * Created by diego on 12/11/14.
 */
public  class LogUtil {

     public static String getTag(Class<?> clazz) {
        return "MapTest " + clazz.getSimpleName();
    }

}

package com.baichang.library.test.base;


import com.baichang.android.common.BCApplication;

public class AppDiskCache {

    /**
     * 用户的token
     *
     * @param data
     */
    public static void setToken(String data) {
        BCApplication.aCache.put(APIConstants.CACHE_TOKEN, data);
    }

    public static String getToken() {
        Object obj = BCApplication.aCache.getAsString(APIConstants.CACHE_TOKEN);
        if (obj == null) return null;
        return (String) obj;
    }

}

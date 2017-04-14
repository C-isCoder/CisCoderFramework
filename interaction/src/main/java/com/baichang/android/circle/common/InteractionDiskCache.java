package com.baichang.android.circle.common;


import com.baichang.android.common.BCApplication;

public class InteractionDiskCache {

    /**
     * 用户的token
     */
    public static void setToken(String data) {
        BCApplication.aCache.put(InteractionFlag.CACHE_TOKEN, data);
    }

    public static String getToken() {
        Object obj = BCApplication.aCache.getAsString(InteractionFlag.CACHE_TOKEN);
        if (obj == null) {
            return null;
        }
        return (String) obj;
    }

    /**
     * 用户
     */
//    public static void setUser(UserData data) {
//        BCApplication.aCache.put(InteractionFlag.CACHE_USER, data);
//    }
//
//    public static UserData getUser() {
//        UserData obj = (UserData) BCApplication.aCache.getAsObject(InteractionFlag.CACHE_USER);
//        if (obj == null) {
//            return null;
//        }
//        return obj;
//    }

    public static Boolean clearUser() {
        return BCApplication.aCache.remove(InteractionFlag.CACHE_USER);
    }

}

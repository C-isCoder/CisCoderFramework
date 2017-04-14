package com.baichang.android.circle.common;

/**
 * Created by iCong on 2017/3/13.
 */

public class InteractionFlag {

  //InteractionDiskCache
  public static final String CACHE_TOKEN = "cache_token";
  public static final String CACHE_USER = "cache_user";
  public static final String ACTION_INTERACTION_DATA = "action_interaction_data";
  public static final String ACTION_INTERACTION_ID = "action_interaction_id";
  public static final String ACTION_INTERACTION_IS_ONESELF = "action_interaction_is_oneself";

  public enum Event {
    INTERACTION_LIST_REFRESH,
  }

}

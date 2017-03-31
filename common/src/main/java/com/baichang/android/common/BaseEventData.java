package com.baichang.android.common;

import java.io.Serializable;
import java.util.List;

/**
 * Created by iCong on 2017/2/28.
 *
 * C is a Coder
 */

public class BaseEventData<T, E> implements Serializable {

  public int type;

  public T value;
  public List<E> valueList;

  public BaseEventData(int type, T value) {
    this.type = type;
    this.value = value;
  }

  public BaseEventData(int type, List<E> list) {
    this.type = type;
    this.valueList = list;
  }

  public BaseEventData(int type) {
    this.type = type;
  }
}

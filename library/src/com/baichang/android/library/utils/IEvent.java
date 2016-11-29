/**
 * FlieName:IEvent.java
 * Destribution:
 * Author:michael
 * 2013-5-27 上午11:33:50
 */
package com.baichang.android.library.utils;

public interface IEvent<T> {

	void onEvent(Object source, T eventArg);

}

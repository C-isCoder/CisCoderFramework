/**
 * FlieName:LTHttpException.java
 * Destribution:
 * Author:michael
 * 2013-5-17 下午4:07:03
 */
package com.baichang.android.library.exception;

/**
 * @author michael
 *
 */
public class MLHttpException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2199603193956026137L;

	
	public MLHttpException(String detailMessage) {
		super(detailMessage);
	}


	@Override
	public String getMessage() {
		//TODO 去掉了请求服务器异常
//		if(MLStrUtil.isEmpty(super.getMessage()))
//			return "请求服务器异常";
//		else
			return super.getMessage();
		//	return String.format("请求服务器异常：%s", super.getMessage());
	}
}

package com.baichang.android.request;

/**
 * Created by iscod.
 * Time:2016/6/21-14:46.
 */
public class HttpResponse<T> {

    private String msg;
    private BaseRes<T> res;
    private boolean state;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public BaseRes<T> getRes() {
        return res;
    }

    public void setRes(BaseRes<T> res) {
        this.res = res;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public static class BaseRes<T> {
        public int code;
        public String msg;
        public T data;

        @Override
        public String toString() {
            return "\n" + "   [code: " + code + "\n" + "     msg: " + msg + "\n" + "    data: " + data;
        }
    }

    @Override
    public String toString() {
        return "[服务器返回信息:]" + "\n" + "msg:" + msg + "\n" + "res:" + res.toString() + "\n" + "state:" + state;
    }
}

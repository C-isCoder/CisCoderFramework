package com.baichang.android.request;

/**
 * Created by iscod.
 * Time:2016/6/21-14:46.
 */
public class HttpResponse<T> {

    private String msg;
    private BaseRes<T> res;
    private int state;

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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public static class BaseRes<T> {
        private int code;
        private String msg;
        private T data;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return "\n" + "   [code: " + code + "\n" + "     msg: " + msg + "\n" + "    data: " + data + "\n";
        }
    }

    @Override
    public String toString() {
        return "服务器返回信息:" + "\n" + "msg:" + msg + "\n" + "res:" + res.toString() + "\n" + "state:" + state;
    }
}

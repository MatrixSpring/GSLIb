package com.dawn.api.router.result;

/**
 * Created by dawn on 2018/9/13.
 */

public class RouterResult {
    static final int SUCCEED_CODE = 0x000011;
    static final int ERROR_CODE = 0x000022;
    private String msg;
    private int code;
    private Object object;

    private RouterResult(Builder builder){
        this.code = builder.code;
        this.msg = builder.msg;
        this.object = builder.object;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public boolean isSucceed() {
        return code == SUCCEED_CODE;
    }

    public static class Builder{
        int code = SUCCEED_CODE;
        String msg;
        Object object;

        public Builder error(){
            this.code = ERROR_CODE;
            return this;
        }

        public Builder success(){
            this.code = SUCCEED_CODE;
            return this;
        }

        public Builder msg(String msg){
            this.msg = msg;
            return this;
        }

        public Builder object(Object obect){
            this.object = obect;
            return this;
        }

        public RouterResult build(){
            return new RouterResult(this);
        }
    }
}

package com.daxi.dxdriverapplication.bean;

public class DataStringBean extends BaseBean {

    /**
     * code : 200
     * msg : SUCCESS
     * data : 24.0=12.0+12.0+
     */

    private int code;
    private String msg;
    private String data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

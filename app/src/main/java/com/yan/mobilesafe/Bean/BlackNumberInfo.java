package com.yan.mobilesafe.Bean;

/**
 * 黑名单信息
 * Created by a7501 on 2015/12/5.
 */
public class BlackNumberInfo {
    private String number;  //黑名单号码

    /**
     * 1全部拦截，电话+短信
     * 2电话拦截
     * 3短信拦截
     */
    private String mode;  //模式

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getNumber() {

        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}

package com.scrm.service.util.resp;

public enum State {
    SUCCESS(0, "请求成功"),
    CONFIRM_SUCCESS(0, "确认成功"),
    REQUEST_WRONG(1, "参数错误"),
    NOT_EXIST(2, "所查内容不存在"),
    RE_SEND(2, "重复发送"),
    NOT_LOGIN(3, "用户未登录"),
    REQUEST_ABNORMAL(3, "请求异常"),
    REGISTER_FAIL(3, "注册失败"),
    ERROR(4, "Server Error"),
    OPERATION_FAIL(5, "操作失败"),
    AUTH_FAIL(5, "没有操作权限"),
    OPEN_ID_ERROR(6, "获取用户信息失败"),
    UNKNOWN_CLIENT(15, "客户端异常，即将重新进入H5"),
    ERROR_IN_TOKEN(304, "验证码错误，请重新输入");

    private int errorCode;
    private String msg;

    private State(int errorCode, String msg) {
        this.errorCode = errorCode;
        this.msg = msg;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static State stateOf(int index) {
        State[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            State state = var1[var3];
            if (state.getErrorCode() == index) {
                return state;
            }
        }

        return null;
    }
}

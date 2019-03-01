public enum HanccThingsUDPStatusType {


    RECEIVEEXCEPTION(10000,"接收数据异常"),
    RECEIVETERMINATE(10002,"接收数据终止");

    public int code;
    public String msg;
    HanccThingsUDPStatusType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

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

    @Override
    public String toString() {
        return "HanccThingsUDPStatusType{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }


    public static void main(String[] args) {
        System.out.println(HanccThingsUDPStatusType.RECEIVETERMINATE.getMsg());
    }
}

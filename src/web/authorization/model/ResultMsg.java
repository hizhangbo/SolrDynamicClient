package web.authorization.model;

public class ResultMsg {
    private int errcode;
    private String errmsg;

    public ResultMsg(int ErrCode, String ErrMsg)
    {
        this.errcode = ErrCode;
        this.errmsg = ErrMsg;
    }
    public int getErrcode() {
        return errcode;
    }
    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }
    public String getErrmsg() {
        return errmsg;
    }
    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}

package web.authorization.model;

public enum ResultStatusCode {
    OK(200, "OK"),
    SYSTEM_ERR(500, "System error"),
    PERMISSION_DENIED(401, "Unauthorized");

    private int errcode;
    private String errmsg;
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
    private ResultStatusCode(int Errode, String ErrMsg)
    {
        this.errcode = Errode;
        this.errmsg = ErrMsg;
    }
}

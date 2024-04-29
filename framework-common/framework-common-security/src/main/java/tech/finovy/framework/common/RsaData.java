package tech.finovy.framework.common;

public class RsaData {
    private String data;
    private String errorMsg;

    public RsaData(String data) {
        this.data = data;
    }
    public RsaData() {
    }
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}

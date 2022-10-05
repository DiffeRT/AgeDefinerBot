public class TokenValue {
    private String func;
    private String descr;

    public TokenValue(String func, String descr) {
        this.func = func;
        this.descr = descr;
    }

    public TokenValue() {

    }

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }
}

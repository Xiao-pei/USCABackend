package xiaopei.bigdata;

public class MyException extends RuntimeException {
    private int errCode;

    public MyException(int errCode, String message) {
        super(message);
        this.errCode = errCode;
    }

    public MyException(String message, Throwable cause, int errCode) {
        super(message, cause);
        this.errCode = errCode;
    }

    public MyException(Throwable cause, int errCode) {
        super(cause);
        this.errCode = errCode;
    }

    public MyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int errCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errCode = errCode;
    }

    public int getErrCode() {
        return errCode;
    }
}

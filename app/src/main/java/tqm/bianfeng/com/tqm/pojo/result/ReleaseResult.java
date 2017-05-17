package tqm.bianfeng.com.tqm.pojo.result;

/**
 * Created by johe on 2017/5/17.
 */

public class ReleaseResult {
    boolean success;
    String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ReleaseResult{" +
                "success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}

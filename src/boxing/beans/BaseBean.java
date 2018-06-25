package boxing.beans;

/**
 * @author Liuyuli
 * @date 2018/6/25.
 */

public class BaseBean {
    private int code;
    private String message;
    private Object object;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}

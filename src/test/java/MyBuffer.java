/**
 * Created by stef on 10/12/16.
 */
public class MyBuffer {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MyBuffer() {
        setMessage("Start...");
    }

    public void append(String s) {
        setMessage(getMessage() + s);
    }
}

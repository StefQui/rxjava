import java.util.ArrayList;
import java.util.List;

/**
 * Created by stef on 10/12/16.
 */
public class MyBuffer {

    private String message;

    private List<MyProduct> products = new ArrayList<MyProduct>();



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

    public List<MyProduct> getProducts() {
        return products;
    }

    public void setProducts(List<MyProduct> products) {
        this.products = products;
    }
}

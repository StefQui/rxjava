/**
 * Created by stef on 12/12/16.
 */
public class MyProduct {


    public MyProduct(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Product:" + getName() + " " + getType();
    }

    private String name;
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

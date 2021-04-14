package Tools;

import java.io.Serializable;

public class StringEntity implements Serializable {
    private String s;

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }
    public StringEntity(String s){setS(s);}
}

package Tools;

import java.io.Serializable;

public class SerializeTo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String command;
    private Object data;
    private Object authority;

    public Object getAuthority() {
        return authority;
    }

    public SerializeTo(String command) {
        this.command = command;
        this.data = "";
    }

    public SerializeTo(String command, String data) {
        this.command = command;
        this.data = data;
    }

    public SerializeTo(String command, String data, String authority) {
        this.command = command;
        this.data = data;
        this.authority = authority;
    }

    public String getCommand() {
        return command;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Command{" +
                "command='" + command + '\'' +
                ", data=" + data + '\'' +
                ", authority=" + authority + '\'' +
                '}';
    }
}
package Tools;

import java.io.Serializable;

public class SerializeTo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String command;
    private Object data;
    private Object credentials;

    public SerializeTo(String command){
        this.command = command;
        data ="";
    }

    public SerializeTo(String command, Object data) {
        this.command = command;
        this.data = data;
    }

    public SerializeTo(String command,Object data, String credentials){
        this.command = command;
        this.data = data;
        this.credentials = credentials;
    }

    public String getCommand() {
        return command;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return ( data);
    }

    @Override
    public String toString() {
        return "Command{" +
                "command='" + command + '\'' +
                ", data=" + data + '\'' +
                ", credentials=" + credentials + '\'' +
                '}';
    }



}

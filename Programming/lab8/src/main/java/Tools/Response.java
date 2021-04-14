package Tools;

import java.io.Serializable;

public class Response implements Serializable {
    private static final long serialVersionUID = 1L;

    private Object response;
    private String command;

    public Response(Object response,String command) {
        this.response = response;
        this.command = command;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }
    public String getCommand(){
        return command;
    }
}

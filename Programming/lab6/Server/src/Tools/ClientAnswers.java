package Tools;

import java.io.Serializable;

public class ClientAnswers implements Serializable {
    private static final long serialVersionUID = 1L;

    private Object ClientAnswer;

    public ClientAnswers(Object ClientAnswer) {
        this.ClientAnswer = ClientAnswer;
    }

    public Object getClientAnswer() {
        return ClientAnswer;
    }

    public void setClientAnswer(Object ClientAnswer) {
        this.ClientAnswer = ClientAnswer;
    }
}

package weblab4.api.v1.session;

public class SessionDTO {

    public final long userId;
    public final String token;

    public SessionDTO(long userId, String token) {
        this.userId = userId;
        this.token = token;
    }
}

package weblab4.beans.session;

import weblab4.beans.users.UserEntity;

import javax.ejb.Remote;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Remote
public interface SessionService extends Serializable {
    String createSession(@NotNull UserEntity user);

    boolean destroySession(@NotNull UserEntity user, @NotNull String token);

    boolean checkSession(@NotNull UserEntity user, @NotNull String token);
}

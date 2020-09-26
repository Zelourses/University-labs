package weblab4.beans.users;

import javax.ejb.Remote;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Remote
public interface UserService extends Serializable {

    UserEntity findUser(@NotNull String username);

    UserEntity createUser(@NotNull @NotBlank String username, @NotNull @NotBlank String password);

    boolean checkPassword(@NotNull UserEntity user, @NotNull String password);

    UserEntity getUser(long userId);
}

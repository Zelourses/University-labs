package weblab4.api.v1.auth;

import weblab4.beans.users.UserEntity;

public class AuthenticationEvent {

    public final UserEntity user;

    AuthenticationEvent(UserEntity user) {
        this.user = user;
    }
}

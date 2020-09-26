package weblab4.api.v1.auth;

import weblab4.beans.users.UserEntity;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;

@RequestScoped
public class AuthenticatedUserInjectionProducer {

    private AuthenticationEvent event;

    public void handleAuthenticationEvent(@Observes @AuthenticatedUser AuthenticationEvent event) {
        this.event = event;
    }

    @Produces
    @AuthenticatedUser
    public UserEntity getAuthenticatedUser() {
        return event == null ? null : event.user;
    }
}

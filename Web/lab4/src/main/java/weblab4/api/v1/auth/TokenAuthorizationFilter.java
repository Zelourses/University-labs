package weblab4.api.v1.auth;

import weblab4.beans.session.SessionService;
import weblab4.beans.users.UserEntity;
import weblab4.beans.users.UserService;

import javax.annotation.Priority;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.Objects;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class TokenAuthorizationFilter implements ContainerRequestFilter {

    private static final String USER_ID_HEADER = "X-USER-ID";
    private static final String TOKEN_HEADER = "X-TOKEN";

    private final SessionService sessionsService;
    private final UserService usersService;

    private final Event<AuthenticationEvent> userAuthenticatedEvent;

    @Deprecated
    public TokenAuthorizationFilter() {
        this.sessionsService = null;
        this.usersService = null;
        this.userAuthenticatedEvent = null;
    }

    @Inject
    public TokenAuthorizationFilter(
            SessionService sessionsService,
            UserService usersService,
            @AuthenticatedUser Event<AuthenticationEvent> userAuthenticatedEvent
    ) {
        this.sessionsService = sessionsService;
        this.usersService = usersService;
        this.userAuthenticatedEvent = userAuthenticatedEvent;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) {
        if (!authorize(requestContext)) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

    private boolean authorize(ContainerRequestContext requestContext) {
        final String userIdString = requestContext.getHeaderString(USER_ID_HEADER);
        final String token = requestContext.getHeaderString(TOKEN_HEADER);

        // If userId or token isn't present abort
        if (userIdString == null || token == null) {
            return false;
        }

        final long userId;
        try {
            userId = Long.parseLong(userIdString);
        } catch (NumberFormatException e) {
            // If userId isn't long number abort
            return false;
        }

        final UserEntity user = Objects.requireNonNull(usersService).getUser(userId);
        if (user == null) {
            // If user isn't exists abort
            return false;
        }

        if (!Objects.requireNonNull(sessionsService).checkSession(user, token)) {
            // If token isn't correspond to existing session of user abort
            return false;
        }

        // Update security context
        requestContext.setSecurityContext(new SecurityContextImpl(requestContext.getSecurityContext(), user.getUsername()));

        // Fire event for CDI
        Objects.requireNonNull(userAuthenticatedEvent).fire(new AuthenticationEvent(user));

        return true;
    }
}

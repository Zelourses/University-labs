package weblab4.api.v1.auth;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

class SecurityContextImpl implements SecurityContext {

    private static final String AUTHENTICATION_SCHEME = "";

    private final SecurityContext oldSecurityContext;
    private final String username;

    public SecurityContextImpl(SecurityContext oldSecurityContext, String username) {
        this.oldSecurityContext = oldSecurityContext;
        this.username = username;
    }

    @Override
    public Principal getUserPrincipal() {
        return () -> username;
    }

    @Override
    public boolean isUserInRole(String role) {
        return "USER".equals(role);
    }

    @Override
    public boolean isSecure() {
        return oldSecurityContext.isSecure();
    }

    @Override
    public String getAuthenticationScheme() {
        return AUTHENTICATION_SCHEME;
    }
}

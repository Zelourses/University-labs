package weblab4.api.v1.session;



import weblab4.api.v1.auth.AuthenticatedUser;
import weblab4.api.v1.auth.Secured;
import weblab4.beans.session.SessionService;
import weblab4.beans.users.UserEntity;
import weblab4.beans.users.UserService;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Objects;

@Path("/session")
@Produces(MediaType.APPLICATION_JSON)
public class SessionController {

    private final UserService userService;
    private final UserEntity userEntity;
    private final SessionService sessionService;

    /*@GET
    @Path("/check/{token}")
    public boolean check(@PathParam("token") String token) {
        return Objects.requireNonNull(service).checkSession(authenticatedUser, token);
    }*/
    public SessionController(){
        userService = null;
        userEntity = null;
        sessionService = null;
    }
    @Inject
    public SessionController(
            UserService service,
            @AuthenticatedUser UserEntity userEntity,
            SessionService sessionService
    ){
        this.userService = service;
        this.userEntity = userEntity;
        this.sessionService = sessionService;
    }

    @POST
    @Path("/programming")
    public Response programming(
            @NotNull @FormParam("code") String code
    ){
        int i = 0;
        StringBuilder response = new StringBuilder();
        for (char c: code.toUpperCase().toCharArray()){
            if (c == 'H'){
                response.append("Hello World!\n");
            }else if (c == 'Q'){
                response.append(code+"\n");
            }else if (c == '9'){
                for (int j=99;j>1;j--) {
                    response.append(j+"bottles of beer on the wall.\n" +
                            "Take one down and pass it around,"+ (j-1)+"\n");
                    response.append("1 bottle of beer on the wall.\n Take one down and pass it around, no more bottles of beer on the wall.");
                    response.append("No more bottles of beer on the wall.\n Go to the store and buy some more, 99 bottles of beer on the wall.");
                }
            }else if (c =='+'){
                i-=-1;
            }
        }
        return Response.ok(response.toString()).build();

    }

    @POST
    @Path("/create")
    public Response create(
        @NotNull @FormParam("username") String username,
        @NotNull @FormParam("password") String password
    ){
        System.out.println("username: "+ username + "password: "+ password);
        final UserEntity user = Objects.requireNonNull(userService).findUser(username);

        if (user !=null)
            System.out.println("user is not null");
        if (userService.checkPassword(user,password))

        if (user !=null && userService.checkPassword(user, password)){
            final String token = Objects.requireNonNull(sessionService).createSession(user);

            return Response.ok(token ==null ? null : new SessionDTO(user.getId(), token)).build();
        }

        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    @DELETE
    @Secured
    @Path("/destroy")
    public boolean destroy(@NotNull @FormParam("token") String token) {
        return Objects.requireNonNull(sessionService).destroySession(userEntity, token);
    }

    @GET
    @Secured
    @Path("/check")
    public boolean check() {
        return true;
    }

    @GET
    @Secured
    @Path("/check/{token}")
    public boolean check(@PathParam("token") String token) {
        return Objects.requireNonNull(sessionService).checkSession(userEntity, token);
    }

}

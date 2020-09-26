package weblab4.api.v1.users;


import weblab4.api.v1.auth.AuthenticatedUser;
import weblab4.beans.users.UserEntity;
import weblab4.beans.users.UserService;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Objects;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    private UserEntity userEntity;

    private UserService userService;

    public UserController(){

    }
    @Inject
    public UserController(UserService service, @AuthenticatedUser UserEntity authenticatedUser) {
        this.userService = service;
        this.userEntity = authenticatedUser;
    }

    @POST
    @Path("/create")
    public Response create(
        @NotNull @FormParam("username") String username,
        @NotNull @FormParam("password") String password
    ){
        System.out.println("username: "+ username + "password: "+ password);
        UserEntity user = Objects.requireNonNull(userService).createUser(username,password);

        return user == null
                ? Response.status(Response.Status.BAD_REQUEST).build()
                : Response.ok(user.getId()).build();


    }
}

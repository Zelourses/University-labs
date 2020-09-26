package weblab4.api.v1;

import weblab4.api.v1.auth.AuthenticatedUser;
import weblab4.api.v1.auth.Secured;
import weblab4.beans.Area.AreaService;
import weblab4.beans.history.HistoryEntity;
import weblab4.beans.history.HistoryService;
import weblab4.beans.users.UserEntity;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.Objects;

@Secured
@Path("/area")
@Produces(MediaType.APPLICATION_JSON)
public class AreaController {

    private final AreaService areaService;
    private final HistoryService historyService;
    private final UserEntity userEntity;

    public AreaController(){
        areaService = null;
        historyService = null;
        userEntity = null;
    }

    @Inject
    public AreaController(
            AreaService areaService,
            HistoryService historyService,
            @AuthenticatedUser UserEntity userEntity
    ){
        this.areaService = areaService;
        this.historyService = historyService;
        this.userEntity = userEntity;
    }

    @GET
    @Path("/check/r/{r}/x/{x}/y/{y}")
    public boolean check(
            @NotNull @PathParam("x") BigDecimal x,
            @NotNull @PathParam("y") BigDecimal y,
            @NotNull @PathParam("r") BigDecimal r
    ){
        return Objects.requireNonNull(areaService).checkPoint(x,y,r);
    }

    @POST
    @Path("/check")
    public boolean checkAndSave(
            @NotNull @FormParam("x") BigDecimal x,
            @NotNull @FormParam("y") BigDecimal y,
            @NotNull @FormParam("r") BigDecimal r
    ) {
        final boolean result = check(x, y, r);
        Objects.requireNonNull(historyService)
                .addQuery(new HistoryEntity(null, Objects.requireNonNull(userEntity), x, y, r, result));

        return result;
    }
}

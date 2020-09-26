package weblab4.api.v1.history;


import weblab4.api.v1.auth.AuthenticatedUser;
import weblab4.api.v1.auth.Secured;
import weblab4.beans.history.HistoryEntity;
import weblab4.beans.history.HistoryService;
import weblab4.beans.users.UserEntity;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Secured
@Path("/history")
@Produces(MediaType.APPLICATION_JSON)
public class HistoryController {
    private final HistoryService historyService;

    private  final UserEntity userEntity;

    public HistoryController(){
        historyService = null;
        userEntity = null;
    }

    @Inject
    public HistoryController(
            HistoryService historyService,
            @AuthenticatedUser UserEntity userEntity
    ){
        this.historyService = historyService;
        this.userEntity = userEntity;
    }

    @GET
    @Path("/get")
    public List<HistoryDTO> get() {
        return Objects.requireNonNull(historyService).getQueries(Objects.requireNonNull(userEntity)).stream()
                .map(this::entityToDto).collect(Collectors.toList());
    }

    @GET
    @Path("/get/page/offset/{offset}/count/{count}")
    public List<HistoryDTO> getPage(
        @PathParam("offset") long offset,
        @PathParam("count") long count
    ){
        return Objects.requireNonNull(historyService).getQueries(Objects.requireNonNull(userEntity),
                offset, count).stream().map(this::entityToDto).collect(Collectors.toList());
    }
    private HistoryDTO entityToDto(HistoryEntity entity) {
        return new HistoryDTO(
                entity.getX().toPlainString(),
                entity.getY().toPlainString(),
                entity.getR().toPlainString(),
                entity.isResult()
        );
    }
}

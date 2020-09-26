package weblab4.api.v1;

import weblab4.beans.Area.AreaService;
import weblab4.beans.history.HistoryService;
import weblab4.beans.session.SessionService;
import weblab4.beans.users.UserService;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class InjectionProducer {

    @Produces @EJB
    private UserService usersService;

    @Produces @EJB
    private SessionService sessionsService;

    @Produces @EJB
    private AreaService areaService;

    @Produces @EJB
    private HistoryService historyService;
}

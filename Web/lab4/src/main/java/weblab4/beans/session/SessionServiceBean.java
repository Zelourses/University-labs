package weblab4.beans.session;

import weblab4.beans.users.UserEntity;
import weblab4.beans.users.UserService;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Stateless
public class SessionServiceBean  implements SessionService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public String createSession(@NotNull UserEntity user) throws PersistenceException {
        //128 рандомных бит(да?)
        //сюда лут
        final String token = UUID.randomUUID().toString();
        entityManager.persist(new SessionEntity(entityManager.getReference(UserEntity.class, user.getId()), token));
        return token;
    }

    @Override
    public boolean destroySession(@NotNull UserEntity user, @NotNull String token) {
        final SessionEntity session = entityManager.find(SessionEntity.class, new SessionEntity.PrimaryKey(user, token));
        if (session == null)
        return false;

        entityManager.remove(session);
        return true;
    }

    @Override
    public boolean checkSession(@NotNull UserEntity user, @NotNull String token) {
        return entityManager.find(SessionEntity.class, new SessionEntity.PrimaryKey(user, token)) != null;
    }
}

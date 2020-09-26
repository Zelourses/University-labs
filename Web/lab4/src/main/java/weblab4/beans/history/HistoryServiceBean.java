package weblab4.beans.history;


import weblab4.beans.users.UserEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.constraints.NotNull;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.Collectors;

@Stateless
public class HistoryServiceBean implements HistoryService{
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public ArrayDeque<HistoryEntity> getQueries(UserEntity userEntity) {
        return entityManager.createNamedQuery("history.findByUser",HistoryEntity.class)
                .setParameter("user",userEntity).getResultStream()
                .collect(Collectors.toCollection(ArrayDeque::new));
    }

    @Override
    public ArrayDeque<HistoryEntity> getQueries(UserEntity user, long offset, long count) {
        return entityManager.createNamedQuery("history.findByUser", HistoryEntity.class)
                .setParameter("user", user).setFirstResult((int) offset).setMaxResults((int) count)
                .getResultStream().collect(Collectors.toCollection(ArrayDeque::new));
    }

    @Override
    public boolean addQuery(@NotNull HistoryEntity query) {
        query = new HistoryEntity(
                null,
                query.getUser(),
                query.getX().setScale(20, RoundingMode.DOWN),
                query.getY().setScale(20, RoundingMode.DOWN),
                query.getR().setScale(20, RoundingMode.DOWN),
                query.isResult()
        );
        final HistoryEntity last = entityManager.createNamedQuery("history.findByUserDesc", HistoryEntity.class)
                .setParameter("user", query.getUser()).setMaxResults(1)
                .getResultStream().findAny().orElse(null);

        if (last != null
                && last.getX().equals(query.getX())
                && last.getY().equals(query.getY())
                && last.getR().equals(query.getR())) {
            return false;
        }

        try {
            entityManager.persist(query);
            entityManager.flush();
        } catch (PersistenceException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean clear(UserEntity user) {
        try {
            return entityManager.createNamedQuery("history.deleteByUser")
                    .setParameter("user", user).executeUpdate() < 1;
        } catch (PersistenceException e) {
            e.printStackTrace();
            return false;
        }
    }
}

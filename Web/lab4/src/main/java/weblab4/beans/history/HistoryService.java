package weblab4.beans.history;

import weblab4.beans.users.UserEntity;

import javax.ejb.Remote;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Deque;

@Remote
public interface HistoryService extends Serializable {
    Deque<HistoryEntity> getQueries(UserEntity userEntity);

    Deque<HistoryEntity> getQueries(UserEntity user, long offset, long count);

    boolean addQuery(@NotNull HistoryEntity query);

    boolean clear(UserEntity user);
}

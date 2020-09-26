package weblab4.beans.session;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import weblab4.beans.users.UserEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity(name = "sessions")
@IdClass(SessionEntity.PrimaryKey.class)
@NamedQuery(name = "sessions.findByUser", query = "from sessions where user = :user")
public class SessionEntity {

    public static class PrimaryKey implements Serializable {

        public final UserEntity user;
        public final String token;

        public PrimaryKey() {
            user = null;
            token = null;
        }

        public PrimaryKey(UserEntity user, String token) {
            this.user = user;
            this.token = token;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final PrimaryKey that = (PrimaryKey) o;
            return Objects.equals(user, that.user) &&
                    Objects.equals(token, that.token);
        }

        @Override
        public int hashCode() {
            return Objects.hash(user, token);
        }
    }

    @Id
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private final UserEntity user;

    @Id @Column(nullable = false)
    private final String token;

    public SessionEntity() {
        user = null;
        token = null;
    }

    public SessionEntity(UserEntity user, String token) {
        this.user = user;
        this.token = token;
    }

    public UserEntity getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final SessionEntity that = (SessionEntity) o;
        return Objects.equals(user, that.user) &&
                Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, token);
    }
}

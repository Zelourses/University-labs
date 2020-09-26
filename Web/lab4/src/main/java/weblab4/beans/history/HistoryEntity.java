package weblab4.beans.history;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import weblab4.beans.users.UserEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity(name = "history")
@NamedQuery(name = "history.findByUser", query = "from history where user = :user order by id asc ")
@NamedQuery(name = "history.findByUserDesc", query = "from history where user = :user order by id desc")
@NamedQuery(name = "history.deleteByUser", query = "delete from history where user = :user")
public class HistoryEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private final UserEntity user;

    @Column(nullable = false, precision = 25, scale = 20)
    private final BigDecimal x,y,r;

    private final boolean result;

    public HistoryEntity(){
        id = null;
        user = null;
        x =y = r = null;
        result = false;
    }

    public HistoryEntity(Long id, UserEntity user, BigDecimal x,BigDecimal y,BigDecimal r, boolean result){
        this.id = id;
        this.user = user;
        this.x = x;
        this.y = y;
        this.r = r;
        this.result = result;
    }

    public Long getId() {
        return id;
    }

    public UserEntity getUser() {
        return user;
    }

    public BigDecimal getX() {
        return x;
    }

    public BigDecimal getY() {
        return y;
    }

    public BigDecimal getR() {
        return r;
    }

    public boolean isResult() {
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoryEntity that = (HistoryEntity) o;
        return result == that.result &&
                Objects.equals(id, that.id) &&
                Objects.equals(user, that.user) &&
                Objects.equals(x, that.x) &&
                Objects.equals(y, that.y) &&
                Objects.equals(r, that.r);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, x, y, r, result);
    }
}

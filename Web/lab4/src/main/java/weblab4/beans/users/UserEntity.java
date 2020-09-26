package weblab4.beans.users;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity(name = "users")
@NamedQuery(name = "users.findByUsername", query = "from users where username = :username")
public class UserEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @Column(nullable = false, unique = true)
    private final String username;

    @Column(nullable = false)
    private final String password;

    public UserEntity(){
        id = null;
        username = null;
        password = null;
    }

    public UserEntity(Long id, String username, String password){
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(username, that.username) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password);
    }
}

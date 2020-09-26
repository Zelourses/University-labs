package weblab4.beans.users;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Stateless
public class UserServiceBean implements UserService {

    @PersistenceContext
    private EntityManager em;

    @Override
    public UserEntity findUser(@NotNull String username) {
        return em.createNamedQuery("users.findByUsername",UserEntity.class)
                .setParameter("username",username).getResultStream()
                .findAny().orElse(null);
    }

    @Override
    public UserEntity createUser(@NotNull @NotBlank String username, @NotNull @NotBlank String password) {
        String hash = hashPassword(password);
        try {
            UserEntity entity = new UserEntity(null, username,hash);
            em.persist(entity);
            em.flush();

            return entity;
        }catch (PersistenceException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean checkPassword(@NotNull UserEntity user, @NotNull String password) {
        return user.getPassword().equals(hashPassword(password));
    }

    private String hashPassword(String password){
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-1");
        }catch (NoSuchAlgorithmException e){
            throw new RuntimeException("no such hash algorithm(how??) (SHA1-1)",e);
        }

        messageDigest.update(password.getBytes());
        byte[] d =messageDigest.digest();
        StringBuilder s =new StringBuilder();
        for(byte b : d){
            s.append(String.format("%02x",b));
        }
        return s.toString();
    }
    @Override
    public UserEntity getUser(long userId) {
        return em.find(UserEntity.class, userId);
    }
}

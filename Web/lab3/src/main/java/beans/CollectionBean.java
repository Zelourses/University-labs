package beans;

import javax.persistence.*;
import java.util.List;

public class  CollectionBean{
    @PersistenceUnit(unitName = "PostgresDS")
    private EntityManagerFactory emf;

    public Point add(Point point) {
        try {
            EntityManager em = getEntityManager();
            boolean unique = true;
            List<Point> pointEntities = getAll();
            for(Point p : pointEntities) {
                if(p.equals(point)) {
                    unique = false;
                    break;
                }
            }
            if(unique) {
                em.getTransaction().begin();
                em.persist(point);
                em.getTransaction().commit();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return point;
    }


    public List<Point> getAll() {
        EntityManager em = getEntityManager();
        TypedQuery<Point> namedQuery = em.createNamedQuery("beans.Point.getAll", Point.class);
        return namedQuery.getResultList();

    }

    public EntityManager getEntityManager() {
        if(emf == null) {
            System.out.println("EntityManagerFactory null");
        }
        return emf.createEntityManager();
    }
}

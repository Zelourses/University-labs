package beans;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name= "results")
@NamedQuery(name = "beans.Point.getAll", query = "SELECT c from beans.Point c")
public class Point implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //@Column(name = "id", nullable = false)
    private Integer id;
   //@Column(name = "x", nullable = false)
    private Double x;
    //@Column(name = "y", nullable = false)
    private Double y;
   // @Column(name = "r", nullable = false)
    private Double r;
    @Column(name = "hit")
    private Boolean isCheck;


    public Point() { }

    public void setR(double r) { this.r = r;}

    public double getR() {
        return r;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getX() {
        return x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getY() {
        return y;
    }

    public void setHit(boolean hit) {
        this.isCheck = hit;
    }

    public Boolean getHit() {
        return isCheck;
    }
}

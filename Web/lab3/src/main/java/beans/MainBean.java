package beans;

public class MainBean {
    private double y;
    private double x;
    private double r;
    private boolean hit;

    private CollectionBean cb;

    public void setR(double r) {
        this.r = r;
    }

    public double getR() { return r; }

    public void setX(double x) {
        this.x = x;
    }

    public double getX() {
        return x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getY() { return y; }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public String getHit() {
        if (hit) return "Есть пробитие";
        else return "Наводчик контужен";
    }

    public void setCb(CollectionBean cb) {
        this.cb = cb;
    }

    public CollectionBean getCb() {
        return cb;
    }

    public static boolean check(double x, double y, double r){
        if ((x<=0 && y>=0 && (x*x+y*y) <= r*r) ||(x>=0 && y>=0 &&y<=r/2-0.5*x) || (x<=0 &&y<=0 && x>=-r && y>=-r))
            return true;
        else return false;

    }


    public boolean validateR() {
        if(r==0.0) return false;
        else  return true;
    }


    public void create() {
        Point point = new Point();
        point.setX(x);
        point.setY(y);
        point.setR(r);
        point.setHit(   check(x,y,r));
        cb.add(point);
        System.out.println(point.getR());
    }

}

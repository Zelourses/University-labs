package weblab4.beans.Mbeans;

public interface CounterMBean {
    void addPoints();
    void addHits();
    int getHits();
    int getPoints();
}

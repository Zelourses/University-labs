package weblab4.beans.Mbeans;

import java.util.ArrayList;

public class AverageClicks implements AverageClicksMBean {
    private ArrayList<Long> clickTime;
    private long lastTime;

    public AverageClicks(){
        clickTime = new ArrayList<>();
        lastTime =System.currentTimeMillis();
    }
    @Override
    public long averageOfAllClicks() {
        long average = 0;
        for (int i=0; i<clickTime.size();i++){
            average+= clickTime.get(i);
        }
        average = (long)average/ clickTime.size();
        return average;
    }

    @Override
    public void addClickTime() {
        long tmp =System.currentTimeMillis();
        clickTime.add(tmp-lastTime);
        lastTime =tmp;
    }
}

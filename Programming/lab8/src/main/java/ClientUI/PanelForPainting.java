package ClientUI;

import essence.Inflammable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PanelForPainting extends JPanel {
    ConcurrentHashMap<Integer,Component> components=new ConcurrentHashMap<Integer, Component>();
    WorkingPlate worker;

    public PanelForPainting(WorkingPlate g){
        this.worker=g;
        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                components.forEach((k,v) -> v.transparentBorder());
                boolean click = false;
                for (Map.Entry<Integer, Component> entry : components.entrySet()) {
                    if(entry.getValue().checkCoordinate(e.getX(), e.getY())){
                        worker.setCurrentObject(entry.getValue().id);
                        worker.setValues();
                        click = true;
                        break;
                    }
                    System.out.println(entry.getValue().x+"-butrningpower for "+entry.getValue().inflammable.getBurningPower());
                    System.out.println(entry.getValue().x+"size fro"+entry.getValue().inflammable.getSize());
                }
                worker.setButtons(click);
                repaint();
            }
        });
    }
    public void setCol(ConcurrentHashMap<Integer,Inflammable>raw){
        components =new ConcurrentHashMap<Integer, Component>();
        raw.forEach((k,v)->{
            components.put(v.getId(),new Component(v.getId(),v.getOwner(),v,this));
        });
        repaint();
    }
    public void addInflam(Integer id,Inflammable inflammable){
        components.put(id,new Component(id,inflammable.getOwner(),inflammable,this));
        repaint();
    }
    public void addInflams(ConcurrentHashMap<Integer,Inflammable> collection){
        collection.forEach((k,v)->{
            components.put(k, new Component(k,v.getOwner(),v,this));
            repaint();
        });
    }
    public void removeInflam(Integer id){
        components.remove(id);
        repaint();
    }
    public void removeInflams(ArrayList<Integer> list){
        list.forEach(e -> removeInflam(e));
    }


    public void changeInflam(Integer id, Inflammable element){
        components.get(id).changeComponent(element);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        components.forEach((k,v)->{
            v.paintComponent(g);
        });
        repaint();
    }

}

class Component{
    int size;
    int constshift = 4;
    int x;
    int y;
    int maxX;
    int maxY;
    Integer id;
    Inflammable inflammable;
    PanelForPainting panel;
    Rectangle2D border;
    boolean needborder=false;

    Color RedFlameColor=new Color(0xFF0311);
    Polygon RedFlame = new Polygon();
    Color ThingColor;
    Color inOrangeFlame=new Color(0xFFAB00);
    int[] RedFlameX = new int[]{15,30,40,50,60,70,80,100,90,75,50,5,0};
    int[] RedFlameY = new int[]{0,20,0,15,0,25,0,40,80,100,95,100,40};
    int[] RedFlameX0 = new int[]{15,30,40,50,60,70,80,100,90,75,50,5,0};
    int[] RedFlameY0= new int[]{0,20,0,15,0,25,0,40,80,100,95,100,40};

    int[] OrangeFlameX=new int[]{20,25,43,50,63,70,85,90,75,50,5,0};
    int[] OrangeFlameY=new int[]{10,25,11,20,13,28,10,80,100,95,100,40};
    int[] OrangeFlameX0=new int[]{20,25,43,50,63,70,85,90,75,50,5,0};
    int[] OrangeFlameY0=new int[]{10,25,11,20,13,28,10,80,100,95,100,40};
    Polygon OrangeFlame = new Polygon();

    int[] ThingX=new int[]{10,60,80,70,25};
    int[] ThingY=new int[]{35,30,65,75,65};

    Polygon Thing = new Polygon();

    int[] InnerFlameX = new int[]{20,36,50,60,75,85,90,75,50,5};
    int[] InnerFlameY = new int[]{55,40,55,43,55,40,80,100,95,100};
    int[] InnerFlameX0 = new int[]{20,36,50,60,75,85,90,75,50,5};
    int[] InnerFlameY0 = new int[]{55,40,55,43,55,40,80,100,95,100};
    Polygon InnerFlame = new Polygon();

    int[] Inner2FlameX = new int[]{36,50,60,73,83,90,75,50,5};
    int[] Inner2FlameY = new int[]{50,65,55,65,50,80,100,95,100};
    int[] Inner2FlameX0 = new int[]{36,50,60,73,83,90,75,50,5};
    int[] Inner2FlameY0 = new int[]{50,65,55,65,50,80,100,95,100};
    Polygon Inner2Flame = new Polygon();


    int[] RedFlameX1 = new int[]{10,25,43,55,60,71,82,100,90,80,53,3,2};
    int[] RedFlameY1= new int[]{5,15,4,20,3,20,4,45,90,100,100,100,40};

    int[] OrangeFlameX1=new int[]{11,25,43,53,63,72,83,90,80,53,3,2};
    int[] OrangeFlameY1=new int[]{11,25,11,21,13,30,13,90,100,100,100,40};

    int[] InnerFlameX1 = new int[]{20,36,50,61,74,88,90,75,50,5};
    int[] InnerFlameY1 = new int[]{55,45,60,48,57,43,80,100,95,100};

    int[] Inner2FlameX1 = new int[]{36,52,63,75,80,90,75,50,5};
    int[] Inner2FlameY1 = new int[]{55,67,60,69,52,80,100,95,100};


    int[] RedFlameX2 = new int[]{13,32,44,54,67,73,84,98,90,75,50,5,0};
    int[] RedFlameY2= new int[]{2,23,3,18,2,27,3,43,80,100,95,100,40};

    int[] OrangeFlameX2=new int[]{17,21,44,53,60,73,84,90,75,50,5,0};
    int[] OrangeFlameY2=new int[]{11,25,13,17,16,25,11,80,100,95,100,40};

    int[] InnerFlameX2 = new int[]{20,37,54,58,76,87,90,75,50,5};
    int[] InnerFlameY2 = new int[]{55,42,63,45,54,44,80,100,95,100};

    int[] Inner2FlameX2 = new int[]{33,54,59,77,83,90,75,50,5};
    int[] Inner2FlameY2 = new int[]{53,67,56,66,49,80,100,95,100};


    public void paintComponent(Graphics g){
        Graphics2D g2 =(Graphics2D) g;
        g2.setComposite(AlphaComposite.SrcOver);
        border.setRect(x,y,100*size+6,100*size+6);

        initPolygon(RedFlameX,RedFlameY,RedFlame);
        g2.draw(RedFlame);
        g2.setColor(RedFlameColor);
        g2.fill(RedFlame);

        initPolygon(OrangeFlameX,OrangeFlameY,OrangeFlame);
        g2.draw(OrangeFlame);
        g2.setColor(inOrangeFlame);
        g2.fill(OrangeFlame);

        initPolygon(ThingX,ThingY,Thing);
        g2.draw(Thing);
        g2.setColor(ThingColor);
        g2.fill(Thing);

        initPolygon(InnerFlameX,InnerFlameY,InnerFlame);
        g2.draw(InnerFlame);
        g2.setColor(RedFlameColor);
        g2.fill(InnerFlame);

        initPolygon(Inner2FlameX,Inner2FlameY,Inner2Flame);
        g2.draw(Inner2Flame);
        g2.setColor(inOrangeFlame);
        g2.fill(Inner2Flame);

        if(needborder) {g2.draw(border);}
        g2.setColor(Color.BLACK);
    }
    public void transparentBorder(){
        needborder = false;
    }

    public void changeComponent(Inflammable inflammable){
        this.x=Math.round(inflammable.getBurningPower());
        this.y=Math.round(inflammable.getSize());
        this.size=inflammable.getSizeUI();
        this.id=inflammable.getId();
        this.inflammable=inflammable;
        border = new Rectangle2D.Double();
        ThingColor=new Color(SetColor(inflammable.getOwner()));
        this.maxX=100*this.size+x;
        this.maxY=100*this.size+y;

        new Thread(()->{
            int i=0;
                    while(i<5){
                        animation1();
                        panel.repaint();
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        animation2();
                        panel.repaint();
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        animation0();
                        panel.repaint();
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        i++;
                    }
            animation0();
            panel.repaint();
        }).start();


    }
    private void animation0(){
        this.RedFlameX=this.RedFlameX0;
        this.RedFlameY=this.RedFlameY0;
        this.OrangeFlameX=this.OrangeFlameX0;
        this.OrangeFlameY=this.OrangeFlameY0;
        this.InnerFlameX=this.InnerFlameX0;
        this.InnerFlameY=this.InnerFlameY0;
        this.Inner2FlameX=this.Inner2FlameX0;
        this.Inner2FlameY=this.Inner2FlameY0;
    }
    private void animation1(){
        this.RedFlameX=this.RedFlameX1;
        this.RedFlameY=this.RedFlameY1;
        this.OrangeFlameX=this.OrangeFlameX1;
        this.OrangeFlameY=this.OrangeFlameY1;
        this.InnerFlameX=this.InnerFlameX1;
        this.InnerFlameY=this.InnerFlameY1;
        this.Inner2FlameX=this.Inner2FlameX1;
        this.Inner2FlameY=this.Inner2FlameY1;
    }

    private void animation2(){
        this.RedFlameX=this.RedFlameX2;
        this.RedFlameY=this.RedFlameY2;
        this.OrangeFlameX=this.OrangeFlameX2;
        this.OrangeFlameY=this.OrangeFlameY2;
        this.InnerFlameX=this.InnerFlameX2;
        this.InnerFlameY=this.InnerFlameY2;
        this.Inner2FlameX=this.Inner2FlameX2;
        this.Inner2FlameY=this.Inner2FlameY2;
    }

    public boolean checkCoordinate(int x, int y){
        if(x>=this.x&&x<=maxX&&y>=this.y&&y<=maxY){
            needborder = true;
            return true;
        } else {
            return false;
        }
    }
    public Component(Integer id,String owner,Inflammable inflammable,PanelForPainting panel){
        this.x=Math.round(inflammable.getBurningPower());
        this.y=Math.round(inflammable.getSize());
        this.size=inflammable.getSizeUI();
        this.id=id;
        this.panel=panel;
        this.inflammable=inflammable;
        border = new Rectangle2D.Double();
        if (owner!=null)
        ThingColor=new Color(SetColor(owner));
        else ThingColor=new Color(SetColor(inflammable.getOwner()));
        this.maxX=100*this.size+x;
        this.maxY=100*this.size+y;

    }
    private int SetColor(String s){
        char[] ss=s.toCharArray();
        int lf=0;
        for (char f : ss){
            if(f<0){
                lf+=f*(-1);
            }else {
                lf += f;
            }
        }
        int Stt=0xff000000;
        Stt=Stt-(13*(lf/3)+lf)*13-lf;
        Stt=Stt-Integer.valueOf(lf+"000");
        return Stt;

    }
    private void initPolygon(int[]x,int[]y,Polygon polygon){
        polygon.reset();
        for(int i =0; i < x.length; i++){
            int x1 = x[i]*size + constshift;
            int y1 = y[i]*size + constshift;
            if(x1>maxX) maxX = x1;
            if(y1>maxY) maxY = y1;
            polygon.addPoint(x[i]*size + constshift + this.x, y[i]*size + constshift + this.y);
        }
    }


}

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.awt.image.*;

public class PolyBox extends JPanel implements MouseListener, ActionListener{
  
  public PolyBox(JFrame frame){
    initMenus();
    //setupCanvas();
    makeFrame(frame);
  }
  
  //public PhysObject ball;
  //public PhysObject ball2;
  public static int colorIndex;
  public static Color[] colorList=new Color[5];
  public static Color renderColor=Color.ORANGE;
  public static Color revertColor=renderColor;
  public static final int XLIM=800;
  public static final int YLIM=400;
  SimplePoly suspendedPoly;
 // Canvas c;
  public ArrayList<SimplePoly> polygons=new ArrayList<SimplePoly>();
  public ArrayList<Point> points=new ArrayList<Point>();
  int currentPoints=0;
  public boolean started=false;
  public boolean drawable=false;
  public boolean clickable=false;
  public boolean clicked=false;
  public static long lastTime;
  Point lastLoc=new Point(70,70);
  JMenuBar mBar=new JMenuBar();
  JMenu menu1=new JMenu("Options");
  JMenu menu2=new JMenu("Polygon");
  JMenuItem m1i1=new JMenuItem("Red");
  JMenuItem m1i2=new JMenuItem("Green");
  JMenuItem m1i3=new JMenuItem("Blue");
  JMenuItem m2i1=new JMenuItem("Start Drawing");
  JMenuItem m2i2=new JMenuItem("Finalize");
  JMenuItem m2i3=new JMenuItem("Undo last point");
  JMenuItem m2i4=new JMenuItem("Undo last polygon");

  public void initMenus(){
   m1i1.addActionListener(this);
   m1i2.addActionListener(this);
   m1i3.addActionListener(this);
   m2i1.addActionListener(this);
   m2i2.addActionListener(this);
   m2i2.setEnabled(false);
   m2i3.addActionListener(this);
   m2i3.setEnabled(false);
   m2i4.addActionListener(this);
   m2i4.setEnabled(false);///MouseInfo.getPointerInfo().getLocation().x;
  }
  
  public void makeFrame(JFrame frame) {
   colorList[0]=Color.GREEN;
   colorList[1]=Color.CYAN;
   colorList[2]=Color.RED;
   colorList[3]=Color.BLUE;
   colorList[4]=Color.BLACK;
   addMouseListener(this);
   frame.pack();
   frame.setVisible(true);
   frame.setJMenuBar(mBar);
   frame.setContentPane(this);
   //f.add(c);
   //c.createBufferStrategy(2);
   mBar.add(menu1);
   mBar.add(menu2);
   menu1.add(m1i1);
   menu1.add(m1i2);
   menu1.add(m1i3);
   menu2.add(m2i1);
   menu2.add(m2i2);
   menu2.add(m2i3);
   menu2.add(m2i4);
   frame.setSize(800,445);
   frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   frame.setResizable(false);
  }

  /*public void setupCanvas(){
   c=new Canvas();
   c.setVisible(true);
   c.setSize(800,400);
   c.addMouseMotionListener(this);
  }*/
  
  public void mouseClicked(MouseEvent me){
    if (drawable){
      started=true;
      if (currentPoints>0){
        if (Math.abs(me.getX()-points.get(0).getX())<4 && Math.abs(me.getY()-points.get(0).getY())<4){
          clickable=true;
          started=false;
          polygons.add(new SimplePoly(points,this));
          points.clear();
          m2i3.setEnabled(false);
          m2i4.setEnabled(true);
          currentPoints=0;
        } else {
          m2i3.setEnabled(true);
          currentPoints+=1;
          points.add(new Point(me.getX(),me.getY()));
          System.out.println(points.size());
        }
      } else {
          m2i3.setEnabled(true);
          m2i2.setEnabled(true);
          currentPoints+=1;
          points.add(new Point(me.getX(),me.getY()));
          System.out.println(points.size());
      }
    } else {
      if (clickable){
        for (int i=0;i<polygons.size();i++){
          SimplePoly sp=polygons.get(i);
          if (Math.abs(me.getX()-sp.cm.getX())<6 && Math.abs(me.getY()-sp.cm.getY())<6){
            sp.suspended=true;
            suspendedPoly=sp;
            clickable=false;
            clicked=true;
          }
        }
      } else if (clicked) {
        suspendedPoly.suspended=false;
        clickable=true;
        clicked=false;
      }
    }
   //int newX=MouseInfo.getPointerInfo().getLocation().x;
   //int newY=MouseInfo.getPointerInfo().getLocation().y;
  }

  public void mousePressed(MouseEvent me){
    for (int i=0;i<polygons.size();i++){
      SimplePoly sp=polygons.get(i);
      if (Math.abs(me.getX()-sp.cm.getX())<6 && Math.abs(me.getY()-sp.cm.getY())<6 && !sp.suspended){
        sp.suspended=true;
        lastTime=System.currentTimeMillis();
      }
    }
    //suspended=true;
    revertColor=renderColor;
    //renderColor=Color.RED;
  }
  
  public void mouseReleased(MouseEvent me){
    for (int i=0;i<polygons.size();i++){
      polygons.get(i).suspended=false;
    }
    renderColor=revertColor;
  }
  
  public void mouseEntered(MouseEvent me){}
  public void mouseExited(MouseEvent me){}
  
  public void paintComponent(Graphics g){
   super.paintComponent(g);
   g.setColor(Color.WHITE);
   g.fillRect(0,0,800,450);
   if (started && currentPoints>0){
     Point lastMarked=points.get(0);
     for (int i=0;i<points.size();i++){
       Point p=points.get(i);
       g.setColor(renderColor);
       g.fillOval((int)(p.getX()-3),(int)(p.getY()-3),6,6);
       g.setColor(Color.BLACK);
       g.drawLine((int)p.getX(),(int)p.getY(),(int)lastMarked.getX(),(int)lastMarked.getY());
       lastMarked=p;
     }
     if (drawable){
       g.drawLine((int)(lastMarked.getX()),(int)(lastMarked.getY()),MouseInfo.getPointerInfo().getLocation().x-2,MouseInfo.getPointerInfo().getLocation().y-69);
     }
   }
   if (polygons.size()>0){
     int x,lastDrawnX;
     int y,lastDrawnY;
     for (int i=0;i<polygons.size();i++){
       SimplePoly sp=polygons.get(i);
       lastDrawnX=sp.getNthX(0);
       x=lastDrawnX;
       lastDrawnY=sp.getNthY(0);
       y=lastDrawnY;
       for(int j=0;j<sp.getNumOfPoints();j++){
         x=sp.getNthX(j);
         y=sp.getNthY(j);
         g.setColor(renderColor);
         g.fillOval(x-3,y-3,6,6);
         g.setColor(Color.BLACK);
         g.drawLine(x,y,lastDrawnX,lastDrawnY);
         lastDrawnX=x;
         lastDrawnY=y;
       }
       g.drawLine(lastDrawnX,lastDrawnY,sp.getNthX(0),sp.getNthY(0));
       g.drawOval((int)(sp.cm.getX()-5),(int)(sp.cm.getY()-5),10,10);
       g.fillOval((int)(sp.cm.getX()-3),(int)(sp.cm.getY()-3),6,6);
     }
   }
   if(drawable){
     g.setColor(Color.GREEN);
     g.drawLine(0,400,800,400);
     g.drawLine(MouseInfo.getPointerInfo().getLocation().x-7,MouseInfo.getPointerInfo().getLocation().y-69,MouseInfo.getPointerInfo().getLocation().x+3,MouseInfo.getPointerInfo().getLocation().y-69);
     g.drawLine(MouseInfo.getPointerInfo().getLocation().x-2,MouseInfo.getPointerInfo().getLocation().y-74,MouseInfo.getPointerInfo().getLocation().x-2,MouseInfo.getPointerInfo().getLocation().y-64);
   }
  }

  public void actionPerformed(ActionEvent ae){
    /*Object source=ae.getSource();
    if (source.equals(mysteryItem)){
      source=actions[actionsIndex%numOfActions];
    }*/
   Object source=ae.getSource();
   if (source.equals(m1i1)){
      System.out.println("RED selected");
      renderColor=Color.RED;
   } else if (source.equals(m1i2)){ 
      System.out.println("GREEN selected");
      renderColor=Color.GREEN;
   } else if (source.equals(m1i3)){
      System.out.println("BLUE selected");
      renderColor=Color.BLUE;
   } else if (source.equals(m2i1)){
     drawable=true;
   } else if (source.equals(m2i2)){
     drawable=false;
     if (points.size()>1){
       polygons.add(new SimplePoly(points,this));
     } 
     points.clear();
     clickable=true;
     m2i2.setEnabled(false);
     m2i3.setEnabled(false);
     m2i4.setEnabled(true);
     currentPoints=0;
   } else if (source.equals(m2i3)){
     points.remove(points.size()-1);
     currentPoints-=1;
     if (points.size()==0){
       m2i3.setEnabled(false);
       m2i2.setEnabled(false);
     }
   } else if (source.equals(m2i4)){
     polygons.get(polygons.size()-1).cease();
     polygons.remove(polygons.size()-1);
     if (polygons.size()==0){
       m2i4.setEnabled(false);
       m2i2.setEnabled(false);
       clickable=false;
     }
   }
  }
  
  public static void main(String[] args){
    try{
      PolyBox p=new PolyBox(new JFrame("Spinning Custom polygons!"));
      lastTime=System.currentTimeMillis();
      while (true){
        p.repaint();
      }
    } catch (NullPointerException e){}
    //PolyBox q=new PolyBox(new JFrame("Second Window!"));
    /*p.ball=new PhysObject(35,0,50,50,800,400,p);
    p.ball2=new PhysObject(25,0,350,50,800,400,p);
    p.ball.start();
    p.ball2.start();*/
    /*q.ball=new PhysObject(35,0,50,50,800,400,q);
    q.ball2=new PhysObject(25,0,350,50,800,400,q);
    q.ball.start();
    q.ball2.start();*/
    /*Graphics g=p.c.getGraphics();
    g.setColor(Color.WHITE);
    g.fillRect(0,0,800,400);
    while (true){
     ball.doInteractions();
     m.drawGraphics(m.lastLoc);
    }*/
  }
}
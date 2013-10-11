import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.awt.image.*;

public class PhysBox extends JPanel implements MouseListener, ActionListener{
  
  public PhysBox(JFrame frame){
    initMenus();
    //setupCanvas();
    makeFrame(frame);
  }
  
  public PhysObject ball;
  public PhysObject ball2;
  public static int colorIndex;
  public static Color[] colorList=new Color[5];
  public static Color renderColor=Color.ORANGE;
  public static Color revertColor=renderColor;
 // Canvas c;
  public static long lastTime;
  Point lastLoc=new Point(70,70);
  JMenuBar mBar=new JMenuBar();
  JMenu menu1=new JMenu("Options");
  JMenu menu2=new JMenu("Better Options");
  JMenuItem m1i1=new JMenuItem("Red");
  JMenuItem m1i2=new JMenuItem("Green");
  JMenuItem m1i3=new JMenuItem("Blue");
  JMenuItem m2i1=new JMenuItem("New ball");
  JMenuItem m2i2=new JMenuItem("Remove ball");

  public void initMenus(){
   m1i1.addActionListener(this);
   m1i2.addActionListener(this);
   m1i3.addActionListener(this);
   m2i1.addActionListener(this);
   m2i2.addActionListener(this);///MouseInfo.getPointerInfo().getLocation().x;
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
    colorIndex+=1;
    renderColor=colorList[colorIndex%5];
    boolean foundTarget=false;
    for (int i=0;i<PhysObject.ao.size();i++){
      PhysObject co=PhysObject.ao.get(i);
      if (co.contains(me.getX(),me.getY())){
        foundTarget=true;
      }
    }
    if (!foundTarget){
      new PhysObject(15,0,me.getX(),me.getY(),800,400,this);
    }
    foundTarget=false;
   //int newX=MouseInfo.getPointerInfo().getLocation().x;
   //int newY=MouseInfo.getPointerInfo().getLocation().y;
  }

  public void mousePressed(MouseEvent me){
    for (int i=0;i<PhysObject.ao.size();i++){
      PhysObject co=PhysObject.ao.get(i);
      if (co.contains(me.getX(),me.getY()) && !co.suspended){
        co.suspended=true;
        lastTime=System.currentTimeMillis();
      }
    }
    //suspended=true;
    revertColor=renderColor;
    //renderColor=Color.RED;
  }
  
  public void mouseReleased(MouseEvent me){
    for (int i=0;i<PhysObject.ao.size();i++){
      PhysObject.ao.get(i).suspended=false;
    }
    renderColor=revertColor;
  }
  
  public void mouseEntered(MouseEvent me){}
  public void mouseExited(MouseEvent me){}
  
  public void paintComponent(Graphics g){
   super.paintComponent(g);
   g.setColor(Color.WHITE);
   g.fillRect(0,0,800,450);
   for (int i=0;i<PhysObject.ao.size();i++){
     PhysObject o=PhysObject.ao.get(i);
     g.setColor(renderColor);
     g.fillOval(o.getX()-o.radius,o.getY()-o.radius,o.radius*2,o.radius*2);
     g.setColor(Color.CYAN);
     //g.drawLine(o.getX(),o.getY(),o.getX()-o.radius,o.getY()-o.radius);
     g.setColor(Color.RED);
     g.drawLine(o.getX(),o.getY(),MouseInfo.getPointerInfo().getLocation().x,MouseInfo.getPointerInfo().getLocation().y-60);
   }
   
   g.setColor(Color.GREEN);
   //g.drawLine(0,390,800,390);
   g.drawLine(0,400,800,400);
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
     new PhysObject(15,0,0,0,800,400,this);
   } else if (source.equals(m2i2)){
     PhysObject.removeLast();
   }
  }
  
  public static void main(String[] args){
    try{
      PhysBox p=new PhysBox(new JFrame("Menus!"));
    } catch (NullPointerException e){}
    //PhysBox q=new PhysBox(new JFrame("Second Window!"));
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
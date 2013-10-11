import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.awt.image.*;

public class SimplePoly extends Thread{
  int[] xpoints;
  int[] ypoints;
  int[] relXpoints;
  int[] relYpoints;
  double dRot=Math.PI/100;
  int cmdx=1;
  int cmdy=0;
  int numOfPoints=0;
  public Point cm;
  PolyBox pb;
  Affine af;
  public boolean ceased=false;
  public boolean suspended=false;
  int lastX, lastY;
  int bottomYn=0;
  int elevation=0;
  int leftXn=0;
  int leftDist=900;
  int rightDist=0;
  int rightXn=0;
  
  public SimplePoly(ArrayList<Point> points,PolyBox pb){
    xpoints=new int[points.size()];
    ypoints=new int[points.size()];
    relXpoints=new int[points.size()];
    relYpoints=new int[points.size()];
    numOfPoints=points.size();
    int xSum=0;
    int ySum=0;
    this.pb=pb;
    
    for (int i=0;i<points.size();i++){
      xpoints[i]=(int)(points.get(i).getX());
      xSum+=xpoints[i];
      ypoints[i]=(int)(points.get(i).getY());
      ySum+=ypoints[i];
      if (xpoints[i]>rightDist){ rightXn=i; rightDist=xpoints[i];}
      if (xpoints[i]<leftDist) {leftXn=i; leftDist=xpoints[i];}
      if (ypoints[i]>elevation) {bottomYn=i; elevation=ypoints[i];}
    }
    
    cm=new Point((int)(xSum/points.size()),(int)(ySum/points.size()));
    for (int i=0;i<numOfPoints;i++){
      relXpoints[i]=(int)(xpoints[i]-cm.getX());
      relYpoints[i]=(int)(ypoints[i]-cm.getY());
    }
    leftDist=(int)(cm.getX()-xpoints[leftXn]);
    rightDist=(int)(xpoints[rightXn]-cm.getX());
    elevation=(int)(ypoints[bottomYn]-cm.getY());
    
    this.start();
    
  }
    
  
  public void run(){
    while (!ceased){
      if (!suspended){
        //System.out.println(y+" : "+cmdx);
        //translateVertices();
        lastX=(int)cm.getX();
        lastY=(int)cm.getY();
        cm.x+=cmdx;
        cm.y+=cmdy;
        affineRotate(dRot,cm);
        //translateVertices();
        if (cm.getX() - leftDist < 0) {
          cmdx = -cmdx; // Reflect along normal
          cm.x = leftDist; // Re-position the ball at the edge
        } else if (cm.getX() + rightDist > PolyBox.XLIM) {
          cmdx = -cmdx;
          cm.x = 800 - rightDist;
        }
        //cmdy+=1;
        // May cross both x and y bounds
        /*if (cm.y - radius < 0) {
          cmdy = -cmdy;
          y = radius;
        } else*/ if (cm.getY() + elevation > PolyBox.YLIM) {
          //System.out.println(cmdy);
          /*if (cmdy<7){
            cmdy=0;
          }
          if (Math.abs(cmdx)<3){
            //cmdx=0;
          }*/
          cmdy = (-2*cmdy)/3;
          cmdx=((2*cmdx)/3);
          //System.out.println("cm at "+cm.getY());
          cm.y = PolyBox.YLIM - elevation;
        }
        //doCollisions();
      } else {
        cm.x=MouseInfo.getPointerInfo().getLocation().x-2;
        cm.y=MouseInfo.getPointerInfo().getLocation().y-69;
        long thisTime=System.currentTimeMillis();
        long dt=thisTime-pb.lastTime;
        if(thisTime-pb.lastTime>30){
          cmdx=(cm.x-lastX)/(int)(dt/30);
          cmdy=(cm.y-lastY)/(int)(dt/30);
          lastX=cm.x;
          lastY=cm.y;
          translateVertices();
          pb.lastTime=thisTime;
        }
      }
      // Refresh the display
      //pb.repaint(); // Callback paintComponent()
      // Delay for timing control and give other threads a chance
      try {
        Thread.sleep(1000 / 30);  // milliseconds
      } catch (InterruptedException e) { }
    }
  }
  
  public int getNthX(int n){
    return xpoints[n];
  }
 
  public int getNthY(int n){
    return ypoints[n];
  }
  
  public int getNumOfPoints(){
    return numOfPoints;
  }
  
  public void affineRotate(double theta, Point center){
    for (int i=0;i<numOfPoints;i++){
      double origx=xpoints[i]-center.x;
      double origy=ypoints[i]-center.y;
      double tempx=(origx*Math.cos(theta)-origy*Math.sin(theta));
      double tempy=(origx*Math.sin(theta)+origy*Math.cos(theta));
      tempx-=origx;
      tempy-=origy;
      xpoints[i]=(int)(center.x+tempx+origx);
      ypoints[i]=(int)(center.y+tempy+origy);
      relXpoints[i]=(int)(tempx+origx);
      relYpoints[i]=(int)(tempy+origy);
      //delay(1000/30);
    }
  }
  
  /* public void rotate(Point pivot, Point moved, double theta){
    double ox=moved.x-pivot.x;
    double oy=moved.y-pivot.y;
    double x1=(ox*Math.cos(theta)-oy*Math.sin(theta));
    double y1=(ox*Math.sin(theta)+oy*Math.cos(theta));
    x1-=ox;
    y1-=oy;
    //jl.setText("relX,Y: "+x1+" , "+y1+" movedX,Y: "+moved.x+" , "+moved.y);
    delay(1000/60);
    moved.x=(int)(pivot.x+ox+x1);
    moved.y=(int)(pivot.y+oy+y1);
  }*/
  
  public void translateVertices(){
    int correctX=0;
    int correctY=0;
    for (int i=0;i<numOfPoints;i++){
      xpoints[i]=(int)(cm.getX()+relXpoints[i]);
      ypoints[i]=(int)(cm.getY()+relYpoints[i]);
    }
  }
  
  public void cease(){
    ceased=true;
  }
  
  public void delay(int millis){
    try{
      Thread.sleep(millis);
    }catch (InterruptedException e){}
  }
  
  public static void main(String[] args){
    PolyBox.main(args);
  }
}
  
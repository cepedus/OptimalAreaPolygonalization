import Jcg.geometry.Point_2;

/**
 * A class for representing a 2D point (integer coordinates)
 * 
 * @author Luca Castelli Aleardi (2019)
 *
 */
public class GridPoint_2{
  public int x,y;

  public GridPoint_2() {}
  
  public GridPoint_2(int x,int y) { 
  	this.x=x; 
  	this.y=y;
  }
  
  public GridPoint_2(double x, double y) { 
	  	this.x=(int)x; 
	  	this.y=(int)y;
	  }

  public GridPoint_2(Point_2 p) { 
	  	this.x=p.x.intValue(); 
	  	this.y=p.y.intValue(); 
	  }
  
  public GridPoint_2(GridPoint_2 p) { 
  	this.x=p.x; 
  	this.y=p.y; 
  }

  public int getX() {return x; }
  public int getY() {return y; }
  
  public void setX(int x) {this.x=x; }
  public void setY(int y) {this.y=y; }
    
  public void translateOf(GridVector_2 v) {
    this.x=this.x+v.x;
    this.y=this.y+v.y;
  }

  public boolean equals(Object o) {
	  if (o instanceof GridPoint_2) {
		  GridPoint_2 p = (GridPoint_2) o;
		  return this.x==p.x && this.y==p.y; 
	  }
	  throw new RuntimeException ("Method equals: comparing Point_2 with object of type " + o.getClass());  	
  }

  public int hashCode () {
	 return (int)(this.x*this.x + this.y);
  }

  public double distanceFrom(GridPoint_2 p) {
    return Math.sqrt((double)this.squareDistance(p));
  }
  
  public int squareDistance(GridPoint_2 p) {
    int dX=p.getCartesian(0)-x;
    int dY=p.getCartesian(1)-y;
    return dX*dX+dY*dY;
  }

  public String toString() {return "("+x+","+y+")"; }
  public int dimension() { return 2;}
  
  public int getCartesian(int i) {
  	if(i==0) return x;
  	return y;
  } 
  public void setCartesian(int i, int x) {
  	if(i==0) this.x=x;
  	else this.y=x;
  }
  
  public int sqrLen()
  {
	  return squareDistance(this);
  }

  public void setOrigin() {
	  	this.x=0;
	  	this.y=0;
	  }
    
  public GridPoint_2 minus(GridPoint_2 b){
  	return new GridPoint_2(b.x-x, 
  						b.y-y);
  }
  
  public GridPoint_2 sum(GridVector_2 v) {
	  	return new GridPoint_2(this.x+v.x,
	  						this.y+v.y);  	
  }
  
  public long cross(GridPoint_2 p)
  {
	  return x * p.y - y * p.x;
  }
  
  public double crossD(GridPoint_2 p)
  {
	  return x * p.y - y * p.x;
  }
  public long cross(GridPoint_2 a, GridPoint_2 b) 
  { 
	  return minus(a).cross(minus(b)); 
  }
  public double crossD(GridPoint_2 a, GridPoint_2 b) 
  { 
	  return minus(a).crossD(minus(b)); 
  }

/**
 *  Compare two points (lexicographic order on coordinates)
 * @param o the point to compare
 */
  public int compareTo(GridPoint_2 o) {
	  if (o instanceof GridPoint_2) {	  
		  GridPoint_2 p = (GridPoint_2) o;
		  if(this.x<p.x)
			  return -1;
		  if(this.x>p.x)
			  return 1;
		  if(this.y<p.y)
			  return -1;
		  if(this.y>p.y)
			  return 1;
		  return 0;
	  }
	  throw new RuntimeException ("Method compareTo: comparing GridPoint_2 with object of type " + o.getClass());  	
  }

}





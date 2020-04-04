import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import Jcg.geometry.PointCloud_2;
import Jcg.geometry.Point_2;
//import jdg.graph.Node;

/**
 * Main class providing tools for computing a polygon with minimal (maximal) area, whose vertices
 * are given as input point cloud (there are no interior points). <br>
 * 
 * @author Luca Castelli Aleardi (2020)
 *
 */
public class OptimalPolygon {
	
	/** Input point cloud: the set of vertices of the final polygon: an array of size 'n' */
	GridPoint_2[] points;
	
	/** Reverse-search mapping to find index of a given GridPoint_2 */
	HashMap<GridPoint_2,Integer> pointHash;
	
	/** Convex hull of input point cloud */
	int[] hullPolygon;

    /**
     * Initialize the input of the program <br>
     * 
     * Particularly, computes and stores convex hull of points and reverse dictionary GridPoint_2 -> index <br>
     * 
     * @param points  array of GridPoint_2
     */
    public OptimalPolygon(GridPoint_2[] points) {
    	this.points=points;
    	this.pointHash = new HashMap<GridPoint_2,Integer>();
    	
    	PointCloud_2 points_;
    	points_ = new PointCloud_2();
    	
    	int i = 0;
    	for(GridPoint_2 p : points)
    	{
    		points_.add(new Point_2(p.x, p.y));
    		pointHash.put(points[i], i);
    		i++;
    	}
    	PointCloud_2 hull = new ConvexHull().computeConvexHull(points_);
    	this.hullPolygon = new int[hull.size()-1];
    	// Fill Convex hull polygon
    	i = 0;
    	for(Point_2 p : hull.listOfPoints())
    	{
    		if(i == hull.size()-1)
    			break;
    		this.hullPolygon[i] = pointHash.get(new GridPoint_2(p));
    		i++;
    	}
    }
    
    /**
     * Return the area of the convex hull of the input points. <br>
     * 
     * @return area of convex hull
     */
    public float computeAreaConvexHull() {
    	return computeArea(this.hullPolygon);
    }

    /**
     * Return the area of the polygon (given as a permutation of the input points).
     * Uses shoelace formula to compute area in O(n) <br>
     * 
     * @param polygon  an array storing a permutation of the input points
     * @return area of polygon
     */
    public long computeArea(int[] polygon) {
    	
    	long area = 0;
    	int n = polygon.length;
    	
    	for (int i = 0; i < n; i++)
    	{
    		// Conversion en long pour éviter les erreurs numériques
    		GridPoint_2 p = points[polygon[i]];
    		GridPoint_2 q = points[polygon[(i+1)%n]];
    		long px=p.x;
    		long py=p.y;
    		long qx=q.x;
    		long qy=q.y;
    		area+=(px*qy)-(qx*py);;		
    	}
      
        // Return absolute value 
        return Math.abs(area / 2); 
    	
    }
        
    /**
     * Swaps two variables (must be primitive type) <br>
     * 
     * @param a
     * @param b  2nd argument must be passed as a=b
     * @return useless return
     */
    public int swap(int a, int b) 
    { 
    	return a;
    }
    
    /**
     * Returns sign of a long variable <br>
     * 
     * @param x
     * @return -1/0/1
     */
    public int sgn(long x) 
    {
    	if(x>0) return 1;
    	if(x<0) return -1;
    	return 0;
    	
    }
    
    public boolean inter1(int a, int b, int c, int d) {
    	if (a > b)
            swap(a, a=b);
        if (c > d)
            swap(c, c=d);
        return Math.max(a, c) <= Math.min(b, d);
    }
        
    public boolean intersect(Segment s, ArrayList<GridPoint_2> polygon)
    {
    	int n = polygon.size();
    	for(int i = 0; i < n; i++)
    		//If 100 000 : use intersect2
    		if(intersect(s, new Segment(polygon.get(i), polygon.get((i + 1) % n), i)))
    			return true;
    	return false;
    }
    /**
     * Check whether two segments intersect (besides from sharing a common point) <br>
     * 
     * @param s1  Segment
     * @param s2  Segment
     * 
     * @return true if they intersect
     */  
    
    public boolean intersect2(Segment s1, Segment s2)
    {
    	GridPoint_2 a=s1.p;
    	GridPoint_2 b=s1.q;
    	GridPoint_2 c=s2.p;
    	GridPoint_2 d=s2.q;
    	
    	double v1x=a.x;
    	double v1y=a.y;
    	double v2x=b.x;
    	double v2y=b.y;
    	double v3x=c.x;
    	double v3y=c.y;
    	double v4x=d.x;
    	double v4y=d.y;
                	
    	double alphaA=v2y-v1y;
    	double betaA=v1x-v2x;
    	double gammaA=-(alphaA*v1x+betaA*v1y);
//        double calcul1=(alphaA*v3x+betaA*v3y+gammaA)*(alphaA*v4x+betaA*v4y+gammaA);
        if ((alphaA*v3x+betaA*v3y+gammaA)*(alphaA*v4x+betaA*v4y+gammaA)>=0) return false;
        
        //Premier segment de part et d'autre : on fait l'autre test
        double alphaB=v4y-v3y;
        double betaB=v3x-v4x;
        double gammaB=-(alphaB*v3x+betaB*v3y);	
//        double calcul2=(alphaB*v1x+betaB*v1y+gammaB)*(alphaB*v2x+betaB*v2y+gammaB);
        if ((alphaB*v1x+betaB*v1y+gammaB)*(alphaB*v2x+betaB*v2y+gammaB)>=0) return false;
       
        return true;
    }
    
    // Ne pas utiliser sur des instances avec des nombres importants...
    public boolean intersect(Segment s1, Segment s2)
    {
    	GridPoint_2 a=s1.p;
    	GridPoint_2 b=s1.q;
    	GridPoint_2 c=s2.p;
    	GridPoint_2 d=s2.q;
    	
    	// Extremities always intersect in polygon segments
    	if(a==c || a==d || b==c || b==d) return false;
    	
    	if (c.cross(a, d) == 0 && c.cross(b, d) == 0)
            return inter1(a.x, b.x, c.x, d.x) && inter1(a.y, b.y, c.y, d.y);
        return sgn(a.cross(b, c)) != sgn(a.cross(b, d)) &&
               sgn(c.cross(d, a)) != sgn(c.cross(d, b));
    }
   
    /**
     * Check whether a point is in the interior of a polygon or not (boundary is exterior) by ray-casting 
     * from test point and counting intersections. <br>
     * 
     * This is an implementation of Franklin's Point Inclusion in Polygon (PNPOLY) Algorithm which runs in O(n) where n is the size of polygon. <br>
     * 
     * @param point  GridPoint_2 to check
     * @param polygon  array of vertex indexes of a polygon
     * 
     * @return true if in interior
     */    
    public boolean pointInPolygon(GridPoint_2 point, int[] polygon)
    {
	    int i, j;
	    boolean contains = false;
	  
	    int nvert = polygon.length;
	    for (i = 0, j = nvert-1; i < nvert; j = i++) 
	    {
		    GridPoint_2 p_i = points[polygon[i]];
		    GridPoint_2 p_j = points[polygon[j]];
		if ((p_i.y > point.y) != (p_j.y > point.y) && (point.x < (p_j.x - p_i.x) * (point.y - p_i.y) / (p_j.y - p_i.y) + p_i.x))
			contains = !contains;
	    }
	    return contains;
    }
    
    /**
     * Checks if a polygon has interior points <br>
     * 
     * Runs in O(n_polygon * (n - n_polygon)) <br>
     * 
     * @param polygon
     */
    public boolean hasInteriorPoints(int[] polygon)
    {
    	
    	// Search only in non-polygon points: Construction in O(n_polygon)
    	HashSet<Integer> set=new HashSet<>();
    	for(int i : polygon)
    		set.add(i);
    	
    	for(GridPoint_2 p : points) // O((n - n_polygon) * n_polygon) 
    	{
    		if(!set.contains(pointHash.get(p)) && pointInPolygon(p, polygon))
    		{
        		return true;
    		}
    	}
       	return false;		
    }

    /**
     * Check whether the input polygon is valid <br>
     * 
     * @param polygon  an array storing a permutation of the input points
     * @return true if polygon passes by all points, if it's a valid permutation and if it doesn't contain self intersections
     */
    public boolean checkValidity(int[] polygon) {
    	
    	int n = this.points.length;
    	
    	if(polygon.length == n)
    	{
    		// Check ALLDIFFERENT(polygon)
    		HashSet<Integer> vertices = new HashSet<>();
    		for (int i : polygon)
    			vertices.add(i);
        	if(vertices.size() != n)
        	{
        		return false;
        	}
    	}
    	else 
    	{
    		if(hasInteriorPoints(polygon))
    		{
        		return false;
    		}
    	}  	
    	 	    	
    	// Naive intersection check in O(n^2)
    	n = polygon.length;
    	Segment[] segments = new Segment[n];
		for(int i = 0; i < n; i++)
			segments[i] = new Segment(points[polygon[i]], points[polygon[(i + 1) % n]], i);
    	
		for(int i = 0; i < n; i++)
		{
			for(int j = 0; j < n; j++)
    		{
				if(i != j && intersect2(segments[i], segments[j]))
    			{
    				return false;
    			}
    		}
		}
    	return true;
		   	
    }
    
    public int[] toIndexArray(ArrayList<GridPoint_2> polygon)
    {
    	int[] polygon_array = new int[polygon.size()];
    	for(int i = 0; i < polygon.size(); i++)
    		polygon_array[i] = this.pointHash.get(polygon.get(i));
    	return polygon_array;
    }
    
    public double areaTriangle(GridPoint_2 p1, GridPoint_2 p2, GridPoint_2 p3)
    {
    	return (p1.crossD(p2, p3))/2;
    }

    /**
     * Main function that computes a simple polygon of minimal area (whose vertices are exactly the input points).<br>
     * 
     * @return  an array of size 'n' storing the computed polygon as a permutation of point indices
     */
    public int[] computeMinimalAreaPolygon() {
    	System.out.print("Computing a simple polygon of minimal area: ");
    	long startTime=System.nanoTime(), endTime; // for evaluating time performances  	

    	int n = points.length;
    	
    	ArrayList<GridPoint_2> S = new ArrayList<>();
    	ArrayList<GridPoint_2> P = new ArrayList<>();
    	for(GridPoint_2 p : points)
    		S.add(p);
    	

    	// Start with random greedy triangle
    	GridPoint_2 p0 = S.remove(ThreadLocalRandom.current().nextInt(0, S.size()));
    	P.add(p0);
    	
    	HashMap<Double, GridPoint_2> distances = new HashMap<>();
    	for(GridPoint_2 p : S)
    		distances.put(p0.distanceFrom(p), p);
    	TreeMap<Double, GridPoint_2> sorted = new TreeMap<>(distances);
    	Set<Entry<Double, GridPoint_2>> mappings = sorted.entrySet();
    	int i = 0;
    	for(Entry<Double, GridPoint_2> mapping : mappings){
    		if(i==2)
    			break;
    		GridPoint_2 p = mapping.getValue();
    		P.add(p);
    		S.remove(p);
    		i++;
    	}
    	
    	
    	// Greedy minimal area triangle
    	int not_ok=0; // On s'autorise un nombre maximum d'itération pour éviter les boucles sans fin
    	while(S.size() != 0 && (not_ok<2*n))
    	{
    		int idx = ThreadLocalRandom.current().nextInt(0, S.size());
    		p0 = S.get(idx);
    		
    		double min_area = Double.MAX_VALUE;
    		
    		int n_p = P.size();
    		int q = -1;
    		int r = -1;
    		for(int p_i = 0; p_i < n_p; p_i++)
			{
    			double area_i = areaTriangle(P.get(p_i), p0, P.get((p_i + 1) % n_p));
    			ArrayList<GridPoint_2> P_i = (ArrayList<GridPoint_2>) P.clone();
    			P_i.add((p_i + 1) % n_p, p0);
    			if(Math.abs(area_i) < min_area && checkValidity(toIndexArray(P_i)))
				{
    				min_area = area_i;
    				q = p_i;
    				r = (p_i + 1) % n_p;
				}
    			P_i.clear();
			}
    		
    		if(q != -1)
    		{
    			P.add(r, p0);
    			S.remove(idx);
    		}
    		else {
    			not_ok++;
    		}
    	}
			
			

    	
    	int[] polygon = toIndexArray(P);
    	
    	// END ALGO
    	endTime=System.nanoTime();
        double duration=(double)(endTime-startTime)/1000000000.;
    	System.out.println("Elapsed time: "+duration+" seconds");
    	
    	return polygon; 
    }

    /**
     * Main function that computes a simple polygon of maximal area (whose vertices are exactly the input points).<br>
     * 
     * @return  an array of size 'n' storing the computed polygon as a permutation of point indices
     */
    public int[] computeMaximalAreaPolygon() {
    	System.out.print("Computing a simple polygon of maximal area: ");
    	
    	long startTime=System.nanoTime(), endTime; // for evaluating time performances
    	
    	ArrayList<GridPoint_2> S = new ArrayList<>();
    	ArrayList<GridPoint_2> P = new ArrayList<>();
    	HashSet<Integer> hullIndexes = new HashSet<Integer>();
    	
    	for(int i : hullPolygon)
    	{
    		P.add(points[i]);
    		hullIndexes.add(i);
    	}
    	
    	for(int i = 0; i < points.length; i++) {
    		if(!hullIndexes.contains(i))
    			S.add(points[i]);
    	}
    	    	
    	// Delete minimal triangle area at each iteration
    	while(S.size() != 0)
    	{
    		int n_p = P.size();
    		int q = -1;
    		int r = -1;
    		int s = -1;
    		double min_area = Double.MAX_VALUE;

    		for(int s_i = 0; s_i < S.size(); s_i++)
    		{
        		for(int p_i = 0; p_i < n_p; p_i++)
        		
            	{
        			GridPoint_2 p0 = S.get(s_i);
        			Segment s1 = new Segment(p0, P.get(p_i),0);
        			Segment s2 = new Segment(p0, P.get((p_i + 1) % n_p), 0);
        			double area_i = areaTriangle(P.get(p_i), p0, P.get((p_i + 1) % n_p));
        			
        			if(Math.abs(area_i) < min_area && !intersect(s1, P) && ! intersect(s2, P))
    				{
        				min_area = area_i;
        				q = p_i;
        				r = (q + 1) % n_p;
        				s = s_i;
    				}
            	}
    		}
        	P.add(r, S.remove(s));
    	}
			
    	int[] polygon = toIndexArray(P);
    	
    	// END ALGO
    	endTime=System.nanoTime();
        double duration=(double)(endTime-startTime)/1000000000.;
    	System.out.println("Elapsed time: "+duration+" seconds");
    	
    	return polygon; 
    }

}

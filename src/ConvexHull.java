import java.util.*;

import Jcg.geometry.*;
import Jcg.geometry.kernel.*;
import Jcg.convexhull2d.ConvexHull_2;

/**
 * @author Luca Castelli Aleardi, Ecole Polytechnique (INF562)
 * 
 * Implementation of Andrew's algorithm for the computation of 2d convex hulls
 */
public class ConvexHull implements ConvexHull_2 {
	
	/** Predicates used to perform geometric computations and tests (approximate, exact or filtered computations)*/
	GeometricPredicates_2 predicates;

	/**
	 * Set the choice of geometric predicates
	 */
	public ConvexHull() {
		//this.predicates=new FilteredPredicates_2();
		this.predicates=new ApproximatePredicates_2();
		//this.predicates=new ExactPredicates_2();
	}
	
	class SortPointsByCoordinates implements Comparator<Point_2> {
		
		public int compare(Point_2 p1, Point_2 p2) {
			return p1.compareTo(p2);
		}
	}
	
	/**
	 * Compute the upper Hull of the point set
	 * 
	 * @param sortedPoints a list of points already sorted (according to a given order)
	 * 
	 * @return the ordered list of points on the upper Hull
	 */
	private ArrayList<Point_2> computeUpperHull(ArrayList<Point_2> sortedPoints) {
    	
		ArrayList<Point_2> u_hull = new ArrayList<Point_2>();
    	
    	for(int i = 0; i < sortedPoints.size(); i++)
    	{  		
    		Point_2 v = sortedPoints.get(i);
    		int n_u = u_hull.size();
    		while(n_u >= 2 && !predicates.isCounterClockwise(u_hull.get(n_u - 1), u_hull.get(n_u - 2), v))
    		{
    			u_hull.remove(n_u - 1);
    			n_u = u_hull.size();
    		}
    		u_hull.add(u_hull.size(),v);   		
    	}
    	
    	return u_hull;
    	
    }

	/**
	 * Compute the lower Hull of the point set
	 * 
	 * @param sortedPoints a list of points already sorted (according to a given order)
	 * 
	 * @return the ordered list of points on the lower Hull
	 */
    private ArrayList<Point_2> computeLowerHull(ArrayList<Point_2> sortedPoints) {
    	
    	ArrayList<Point_2> u_hull = new ArrayList<Point_2>();
    	
    	for(int i = 0; i < sortedPoints.size(); i++)
    	{  		
    		Point_2 v = sortedPoints.get(i);
    		int n_u = u_hull.size();
    		while(n_u >= 2 && (
    						predicates.isCounterClockwise(u_hull.get(n_u - 1), u_hull.get(n_u - 2), v) 
    						|| predicates.liesOn(u_hull.get(n_u - 1), u_hull.get(n_u - 2), v)
    						))
    		{
    			u_hull.remove(n_u - 1);
    			n_u = u_hull.size();
    		}
    		u_hull.add(u_hull.size(),v);   		
    	}
    	
    	return u_hull;
    }
    
    
	/**
	 * Compute the convex hull of the input point set
	 * 
	 * @param points
	 * 					a point cloud (points are not sorted)
	 * 
	 * @return the ordered set of points on the convex hull
	 */
    public PointCloud_2 computeConvexHull(PointCloud_2 points) {
    	
    	ArrayList<Point_2> sortedPoints=(ArrayList<Point_2>)points.listOfPoints();	
    	Collections.sort(sortedPoints,new SortPointsByCoordinates());
    	
    	ArrayList<Point_2> upperHull=computeUpperHull(sortedPoints);
    	ArrayList<Point_2> lowerHull=computeLowerHull(sortedPoints);
    	
    	//for(int i = lowerHull.size() - 1; i >= 0; i--)
    	for(int i = lowerHull.size() - 2; i >= 0; i--)
    		upperHull.add(lowerHull.get(i));
    	    	
    	return new PointCloud_2(upperHull);
    }

}
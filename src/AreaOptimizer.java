import processing.core.PApplet;

	/**
	 * Main program that takes as input a 2D point cloud (with even integer coordinates)
	 * and computes a simple polygons with minimal (and maximal) area.
	 * 
	 * @author Luca Castelli Aleardi (Ecole Polytechnique, 2020)
	 *
	 */
public class AreaOptimizer {

	public static void main(String[] args) {
		System.out.println("Tools for the \"INF562 final evaluation\"");
		if(args.length<1) {
			System.out.println("Error: one argument required: input file storing 2D integer points");
			System.exit(0);
		}
		
		String inputFile=args[0]; // input file name
		GridPoint_2[] points=PointCloud_IO.read(inputFile);
		if(args.length==2)
		{
			int[] drawingBounds=PointCloud_IO.getBoundingBox(points); // get the bounding box [0..xmax]x[0..ymax]
			
			// set the input parameters for 2D rendering
			PointCloudViewer.sizeX=600; // setting canvas width (number of pixels)
			PointCloudViewer.sizeY=600; // setting canvas height (pixels)
			PointCloudViewer.inputPoints=points; // set the input points for rendering in the PApplet
			PointCloudViewer.drawingWidth=Math.max(drawingBounds[0], drawingBounds[1]); // setting the width of the drawing area (square area)
			PointCloudViewer.drawingHeight=Math.max(drawingBounds[0], drawingBounds[1]);  // setting the height of the drawing area (square area)
			
			String outputFile=args[1];
			int[] optimal = PointCloud_IO.readPolygon(outputFile);
			OptimalPolygon op=new OptimalPolygon(points);
			boolean isValidMax=op.checkValidity(optimal); 
			double area=op.computeArea(optimal);
			double areaCH=op.computeAreaConvexHull(); // compute the area of the convex hull (this function MUST BE IMPLEMENTED)
			System.out.println("Area of the Convex Hull: "+areaCH);
			if(isValidMax==false)
				System.out.println("The maximal polygon is not valid (or not defined)");
			System.out.println("Area of the optimal polygon: "+area);
			
			System.out.println("\tSCORE:"+area/areaCH);
			PointCloudViewer.optimalPolygon=optimal; // set the polygon to be rendered
			if(PointCloudViewer.optimalPolygon==null)
				System.out.println("Warning: the polygon is not defined");
			PApplet.main(new String[] { "PointCloudViewer" }); // launch the Processing viewer*/
			
		}
		else {
			 // load input points from text file
			int[] drawingBounds=PointCloud_IO.getBoundingBox(points); // get the bounding box [0..xmax]x[0..ymax]
			
			// set the input parameters for 2D rendering
			PointCloudViewer.sizeX=600; // setting canvas width (number of pixels)
			PointCloudViewer.sizeY=600; // setting canvas height (pixels)
			PointCloudViewer.inputPoints=points; // set the input points for rendering in the PApplet
			PointCloudViewer.drawingWidth=Math.max(drawingBounds[0], drawingBounds[1]); // setting the width of the drawing area (square area)
			PointCloudViewer.drawingHeight=Math.max(drawingBounds[0], drawingBounds[1]);  // setting the height of the drawing area (square area)
			
			System.out.println("\n--- Starting computations ---");
			// initialize the main class for computing optimal polygons
			OptimalPolygon op=new OptimalPolygon(points);
			
			double areaCH=op.computeAreaConvexHull(); // compute the area of the convex hull (this function MUST BE IMPLEMENTED)
			System.out.println("Area of the Convex Hull: "+areaCH);
	
			// Minimal polygon
			int[] minimal=op.computeMinimalAreaPolygon(); // compute the polygon of minimal area (this function MUST BE IMPLEMENTED)
			boolean isValidMin=op.checkValidity(minimal); // check whether the polygon is valid (this function MUST BE IMPLEMENTED)
			double areaMin=op.computeArea(minimal); // compute the area of the polygon (this function MUST BE IMPLEMENTED)
			if(isValidMin==false)
				System.out.println("The minimal polygon is not valid (or not defined)");
			System.out.println("Area of the minimal polygon: "+areaMin);
	
			// Maximal polygon
			int[] maximal=op.computeMaximalAreaPolygon(); // compute the polygon of maximal area (this function MUST BE IMPLEMENTED)
			boolean isValidMax=op.checkValidity(maximal); 
			double areaMax=op.computeArea(maximal); 
			if(isValidMax==false)
				System.out.println("The maximal polygon is not valid (or not defined)");
			System.out.println("Area of the maximal polygon: "+areaMax);
	
			System.out.println("\n --- end of computations ---");
			// write the minimal/maximal polygons computed by your program to a text file
			PointCloud_IO.write(minimal, inputFile.replace("instance", "min")); // the output file has the same prefix of the corresponding instance
			PointCloud_IO.write(maximal, inputFile.replace("instance", "max")); // the output file has the same prefix of the corresponding instance
			
			// uncomment the line below to show in a Processing frame the 2D point cloud together with a polygon (minimal or maximal)
			PointCloudViewer.optimalPolygon=maximal; // set the polygon to be rendered
			if(PointCloudViewer.optimalPolygon==null)
				System.out.println("Warning: the polygon is not defined");
			PApplet.main(new String[] { "PointCloudViewer" }); // launch the Processing viewer*/
		}

	}

}

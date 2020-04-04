public class Segment implements Comparable<Segment>{

	public GridPoint_2 p;
	public GridPoint_2 q;
	public int id;
	
	public Segment(GridPoint_2 p, GridPoint_2 q, int id)
	{
		this.p=p;
		this.q=q;
		this.id=id;
	}
	
	public double get_y(double x)
	{
		if(Math.abs(p.getX() - q.getX()) < 0)
			return (double) p.getY();
		return p.getY() + (q.getY() - p.getY())*(x - p.getX())/(q.getX() - p.getX());
	}
	
	public int minX()
	{
		return Math.min(p.x, q.x);
	}
	
	public int maxX()
	{
		return Math.max(p.x, q.x);
	}
	
	@Override
	public int compareTo(Segment other)
	{
		double x = Math.max(Math.min(this.p.x, this.q.x), Math.min(other.p.x, other.q.x));
	    return this.get_y(x) < other.get_y(x) ? 1 : -1;
	}
	

}
import java.util.HashMap;

public class Vertex 
{

	public int i;
	public int airDistance;
	public int distance;
	public Vertex cameFrom;
	public HashMap<Vertex,Integer> outgoingEdges;
	public Boolean visited;
	
	public Vertex(int I, int Air)
	{
		this.i = I;
		this.airDistance = Air;
		this.distance = Integer.MAX_VALUE;
		this.outgoingEdges = new HashMap<Vertex,Integer>();
		this.visited = false;
		this.cameFrom = null;
	}
	
}

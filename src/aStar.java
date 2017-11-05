import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;

public class aStar
{
	//comparator for the priority queue the uses the sum of the airDistance heuristic and the usual distance.
	private static class aStarComparator implements Comparator<Vertex>
	{
	    public int compare(Vertex a, Vertex b)
	    {
	        return a.airDistance+a.distance>b.airDistance+b.distance ? 1 : a.airDistance+a.distance == b.airDistance+b.distance ? 0 : 1 ;
	    }
	}
	static aStarComparator theComparator = new aStarComparator();

//	private static class dijkstraComparator implements Comparator<Vertex>
//	{
//	    public int compare(Vertex a, Vertex b)
//	    {
//	        return a.distance>b.distance ? 1 : a.distance == b.distance ? 0 : 1 ;
//	    }
//	}
//	static dijkstraComparator theComparator = new dijkstraComparator();

	//method for creating the path and calculating path length
	//starts from the end node and works back to the start by asking where it came from
	public static String pathBuilder(Vertex start, Vertex end)
	{
		StringBuilder path = new StringBuilder();
		Vertex parent = end;
		 do{
			 path.insert(0, parent.i+" ");
			 parent = parent.cameFrom;
		 }while (parent != start);

		 return path.toString();
	}


	public static void main(String[] args) throws IOException
    {
	 	//Open Output file
		File idFile = new File("output.txt");
		FileWriter output = new FileWriter(idFile);
		//Open Input file
	    FileInputStream fstream = new FileInputStream("input.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		String NST_string;
		while ((NST_string = br.readLine()) != null)
		{
			//extracts N s and t from the input
			String[] NST_list=NST_string.split(" ");
			int N=Integer.parseInt(NST_list[0]);
			int S=Integer.parseInt(NST_list[1]);
			int T=Integer.parseInt(NST_list[2]);

			//creates each vertex using the air distances and stores in an array
			String airD_string = br.readLine();
			String[] airD_list=airD_string.split(" ");
			Vertex[] vList = new Vertex[N];
			for(int i=0; i<N; i++)
			{
				vList[i] = new Vertex(i, Integer.parseInt(airD_list[i]));
			}

			//populates each vertex with it's outgoing edges
			String edgeString;
			while (!(edgeString = br.readLine()).equals("-1 -1 -1"))
			{
				String[] edgeList=edgeString.split(" ");
				int x = Integer.parseInt(edgeList[0]);
				int y = Integer.parseInt(edgeList[1]);
				int d = Integer.parseInt(edgeList[2]);
				vList[x].outgoingEdges.put(vList[y], d);
			}

			PriorityQueue<Vertex> frontier = new PriorityQueue<Vertex>(10,theComparator);

			Vertex start = vList[S];
			Vertex end = vList[T];
			frontier.add(start);
			start.distance = 0;
			while (!frontier.isEmpty())
			{
				 //next node to be searched
				 Vertex current = frontier.poll();
				 current.visited = true;

				 // current.airDistance+current.distance never overestimates distance to end so if this condition is true
				 // all paths of length equal to or less than the current shortest path have been considered
				 if (current.airDistance+current.distance > end.distance)
				 {
					 break;
				 }
				 //finds adjacent vertices to current
				 for (Map.Entry<Vertex,Integer> edge : current.outgoingEdges.entrySet())
				 {
					 Vertex nextVertex = edge.getKey();
					 if (!nextVertex.visited)
					 {
						 //the distance from the current vertex
						 int newDistance = current.distance+edge.getValue();

						 //if the distances are the same then there are two paths of equal length to the vertex
						 if (newDistance == nextVertex.distance)
						 {

							 String oldPath = pathBuilder(start, nextVertex.cameFrom);
							 String newPath = pathBuilder(start, current);
							 //compares the two paths lexicographically and chooses the lowest
							 if (newPath.compareTo(oldPath) < 0)
							 {
								 nextVertex.cameFrom = current;
							 }
						 }

						//if the new distance is lower than the old, performs edge relaxation.
						 else if (newDistance < nextVertex.distance)
						 {
							 nextVertex.distance = newDistance;
							 nextVertex.cameFrom = current;
						 }
						 //adds the the vertex to the queue to be explored
						 frontier.add(nextVertex);
					 }
				 }
			}
			//builds the path and prints to output.
			String path = pathBuilder(start, end);
			output.write(path);
			output.write(Integer.toString(end.distance));
			output.write("\n");

		}
		output.close();
		br.close();
    }
}

/**
 * Represents the given information as a graph. This class only has the applicable methods needed
 * for a postively weighted graph and Dijkstra's algorithm
 */
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.ArrayList;


public class GraphDijkstra {
     public static final double INFINITY = Double.MAX_VALUE;
     private Map<String,Vertex> vertexMap = new HashMap<String,Vertex>( );
     public PriorityQueue<DuplicatePath> shortestMultiple = new PriorityQueue<>();


     // Used to signal violations of preconditions for
    // various shortest path algorithms.

    /**
     * Add a new edge to the graph.
     */
    public void addEdge( String sourceName, String destName, double cost )
    {
        Vertex v = getVertex( sourceName );
        Vertex w = getVertex( destName );
        v.adj.add( new Edge( w, cost ) );
    }

    /**
     * Used to change the cost between 2 nodes. This accounts traffic reports
     * @param sourceName Start vertex
     * @param destName Destination vertex
     * @param cost Cost of edge
     * @param traffic Traffic report
     */
    public void changeEdge(String sourceName, String destName, double cost, String traffic) {
        Vertex v = getVertex(sourceName);
        Vertex w = getVertex(destName);
    
        // Find the correct index of the existing edge
        int index = -1;
        for (int i = 0; i < v.adj.size(); i++) {
            if (v.adj.get(i).dest.equals(w)) { // Compare edges based on target
                index = i;
                break;
            }
        }
    
        // If the edge exists, update its cost
        if (index != -1) {
            double newCost = cost;
            if (traffic.equalsIgnoreCase("RB")) {
                newCost = cost * 100;
            } else if (traffic.equalsIgnoreCase("RW")) {
                newCost = cost * 3;
            }
            v.adj.get(index).cost = newCost; // Update cost directly
        } else {
            System.out.println("Edge does not exist");
            // Handle the case where the edge doesn't exist, e.g., throw an exception or log a message
        }
    }

    /**
     * Driver routine to handle unreachables and print total cost.
     * It calls recursive routine to print shortest path to
     * destNode after a shortest path algorithm has run.
     */
    public String printPath( String destName )
    {
        String path = ""; 
        Vertex w = vertexMap.get(destName);
        if( w == null )
            return "";
        else if( w.dist == INFINITY )
            return "";
        else
        {
            path = printPath( w );
        }
        return path;
    }

     /**
     * If vertexName is not present, add it to vertexMap.
     * In either case, return the Vertex.
     */
    private Vertex getVertex( String vertexName )
    {
        Vertex v = vertexMap.get( vertexName );
        if( v == null )
        {
            v = new Vertex( vertexName );
            vertexMap.put( vertexName, v );
        }
        return v;
    }

     /**
     * Recursive routine to print shortest path to dest
     * after running shortest path algorithm. The path
     * is known to exist.
     */
    private String printPath( Vertex dest )
    {
        if( dest.prev != null )
        {
            String path = printPath(dest.prev);
            return path + " " + dest.name;
        }
        else return dest.name;
    }
    
     /**
     * Initializes the vertex output info prior to running
     * any shortest path algorithm.
     */
    private void clearAll( )
    {
        for( Vertex v : vertexMap.values( ) )
            v.reset( );
    }

     /**
     * Single-source weighted shortest-path algorithm. (Dijkstra) 
     * using priority queues based on the binary heap
     */
    public void dijkstra( String startName )
    {
        PriorityQueue<Path> pq = new PriorityQueue<Path>( );



        Vertex start = vertexMap.get( startName );
        //breaks from the method if the given vertex is null
        if( start == null){
            return;
        }


        clearAll( );
        clearMultiple();
        pq.add( new Path( start, 0 ) ); start.dist = 0;
        
        int nodesSeen = 0;
        while( !pq.isEmpty( ) && nodesSeen < vertexMap.size( ) )
        {
            Path vrec = pq.remove( );
            Vertex v = vrec.dest;

            if( v.scratch != 0 )  // already processed v
                continue;
                
            v.scratch = 1;
            nodesSeen++;


            for( Edge e : v.adj )
            {
                Vertex w = e.dest;
                double cvw = e.cost;
                
                if( cvw < 0 )
                    throw new GraphException( "Graph has negative edges" );
                //consider multiple runs of dijkstra's
                //in the end, only consider multiples of the shortest path
                    
                if( w.dist > v.dist + cvw )
                {
                    w.dist = v.dist +cvw;
                    w.prev = v;
                    pq.add( new Path( w, w.dist ) );
                    
                }
                //adds dupplicates (same cost, start and end vertices) to a priority queue used in SimulatorTwo to determine "multiple solutions"
                else if((w.dist == v.dist + cvw)){
                    DuplicatePath d = new DuplicatePath(w.dist, printPath(w));
                    if (!shortestMultiple.contains(d)){
                        shortestMultiple.add(d);
                    }                   
                }
            }
        }
    }

    public int getSize(){
        return vertexMap.size();
    }

    public double getDist(String destination){
        Vertex w = vertexMap.get(destination);
        return w.dist;
    }

    //returns vertex after run of Dijkstra
    public Vertex getVertexMap(String destname){
        Vertex vertex = vertexMap.get(destname);
        return vertex;
    }

    public void clearMultiple(){
        shortestMultiple.clear();
    }

}

// Used to signal violations of preconditions for
// various shortest path algorithms.
class GraphException extends RuntimeException
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GraphException( String name )
    {
        super( name );
    }
}

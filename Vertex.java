// Represents a vertex in the graph.
import java.util.LinkedList;
import java.util.List;

public class Vertex implements Comparable<Vertex> {
    public String     name;   // Vertex name...might change to int seeing that vertices are numbered
    public List<Edge> adj;    // Adjacent vertices
    public double     dist;   // Cost
    public Vertex     prev;   // Previous vertex on shortest path
    public int        scratch;// Extra variable used in algorithm

    public Vertex(String nm )
      { name = nm; adj = new LinkedList<Edge>( ); reset( ); }

    public void reset( )
    //  { dist = Graph.INFINITY; prev = null; pos = null; scratch = 0; }    
    { dist = GraphDijkstra.INFINITY; 
      prev = null; 
      scratch = 0; }

    public Vertex (Vertex v){
      this.name = v.name;
      this.adj = v.adj;
      this.dist = v.dist;
      this.prev = v.prev;
      this.scratch = v.scratch;
    }

    @Override
    public int compareTo(Vertex other){
      return dist < other.dist ? -1 : dist > other.dist ? 1 : 0;
    }

    public String toString(){
      return name;
    }
      
   // public PairingHeap.Position<Path> pos;  // Used for dijkstra2 (Chapter 23)
}

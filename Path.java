// Represents an entry in the priority queue for Dijkstra's algorithm.
public class Path implements Comparable<Path> {
    public Vertex     dest;   // w
    public double     cost;   // d(w)
    
    public Path( Vertex d, double c )
    {
        dest = d;
        cost = c;
    }

    public Path deepCopy(){
        return new Path(dest, cost);
    }

    @Override
    public int compareTo( Path rhs )
    {
        double otherCost = rhs.cost;
        if (cost < otherCost) {
            return -1;
        } else if (cost > otherCost) {
            return 1;
        } else {
            // If costs are equal, compare based on other criteria
            // For example, you could compare based on destination vertex
            return 0; // Assuming Vertex also implements Comparable
        }
    }

    @Override
    public String toString(){
        return "Cost: " + cost; 
    }
    
}

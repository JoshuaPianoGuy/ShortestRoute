// Represents an edge in the graph.
public class Edge {
    public Vertex     dest;   // Second vertex in Edge
    public double     cost;   // Edge cost
    
    public Edge( Vertex d, double c )
    {
        dest = d;
        cost = c;
    }

    public Vertex getTarget(){
        return this.dest;
    }

    public boolean equals(Vertex other){
        int result = this.dest.compareTo(other);
        if (result == 0) return true;
        return false;
    }

    public void setCost(double newCost){
        this.cost = newCost;
    }


}

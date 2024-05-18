/**
 * Class stores a cost associated with a path, as well as the String representation of that path 
 */
public class DuplicatePath implements Comparable<DuplicatePath> {
    private double cost;
    private String path;

    public DuplicatePath(double c, String p){
        this.cost = c;
        this.path = p;
    }

    public double getCost(){
        return this.cost;
    }

    public String getPath(){
        return this.path;
    }

    /**
     * This compares the first number in a path. Whichever number is smaller is the path
     * that comes first, starting from that number
     */
    @Override
    public int compareTo(DuplicatePath other) {
        String[] numbers1 = this.path.split(" ");
        String[] numbers2 = other.path.split(" ");
        
        int minLength = Math.min(numbers1.length, numbers2.length);
        for (int i = 0; i < minLength; i++) {
            int num1 = Integer.parseInt(numbers1[i]);
            int num2 = Integer.parseInt(numbers2[i]);
            
            // Compare integers
            if (num1 < num2) {
              return -1; // path1 (current path) comes first
            } else if (num1 > num2) {
              return 1; // path2 (other path) comes first
            }
        }
        return numbers1.length - numbers2.length;
    }

    @Override
    public boolean equals(Object o){
        DuplicatePath other = (DuplicatePath)o;
        return this.path.equals(other.getPath());
    }

    public String toString(){
        return this.cost + " " + this.path;
    }
    
}

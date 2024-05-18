import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.StringTokenizer;


public class SimulatorOne {
    public static void main(String[] args){
            Scanner keyboard = new Scanner(System.in);
            int graphSize = Integer.parseInt(keyboard.nextLine());
            GraphDijkstra graph = new GraphDijkstra();

            //adds verices to the graph with costs between edges
            //seeing that the array of vertices is used, each line (vertex) is dealt with sequentially
            for (int i = 0; i < graphSize; i ++){
                //splits each line
                String[] vertexAdj = keyboard.nextLine().split(" ");
                //starts at 1 because 0 (start vertex) is ignored
                int vertexRelate = 1;
                int edgeCost = vertexRelate + 1;
                //number of adjacent vertices
                int outDegree = (vertexAdj.length - 1) /2;
                for (int j = 0; j < outDegree; j++){                   
                    //source, destination and cost
                    graph.addEdge(vertexAdj[0], vertexAdj[vertexRelate], Double.parseDouble(vertexAdj[edgeCost])); 
                    vertexRelate += 2;
                    edgeCost = vertexRelate + 1;                           
                }
            }

            //gets the shops
            Integer.parseInt(keyboard.nextLine());
            String[] shops = keyboard.nextLine().split(" ");

            //gets the clients
            Integer.parseInt(keyboard.nextLine());
            String[] clients = keyboard.nextLine().split(" ");
            keyboard.close();

            //shortest cost associated with a destination vertex
            PriorityQueue<DuplicatePath> duplicatePathsTaxi = new PriorityQueue<DuplicatePath>();
            PriorityQueue<DuplicatePath> duplicatePathsShop = new PriorityQueue<DuplicatePath>();
            double multipleCostTaxi = 0;
            double multipleCostShop = 0;
            String pathDuplicate = "";
            int multipleCount = 0;
            //keeps track of the shortest path
            double minPathTaxi = graph.INFINITY;
            double minPathShop = graph.INFINITY;
            String minCostPathTaxi = "";
            String minCostPathShop = "";
            boolean help = true;
            
            //double lowestCost = Double.MAX_VALUE;


            for (String call: clients){
                minPathTaxi = graph.INFINITY;
                minPathShop = graph.INFINITY;
                minCostPathTaxi = "";
                minCostPathShop = "";
                multipleCostTaxi = 0;
                multipleCostShop = 0;
                pathDuplicate = "";
                graph.clearMultiple();
                
                System.out.println("client " + call);
                for(int i = 0; i < shops.length; i++){
                    //pick up
                    graph.dijkstra(shops[i]);
                    if (graph.getVertexMap(call) != null){
                        if(minPathTaxi == graph.getVertexMap(call).dist && minPathTaxi != graph.INFINITY){
                            multipleCostTaxi = graph.getVertexMap(call).dist;
                            pathDuplicate = graph.printPath(call);
                            duplicatePathsTaxi.add(new DuplicatePath(multipleCostTaxi, pathDuplicate)); 
                        }
                        if(minPathTaxi > graph.getVertexMap(call).dist){
                            minPathTaxi = graph.getVertexMap(call).dist;
                            minCostPathTaxi = graph.printPath(call);
                        }
                    }
                   
                }  
                //loop 
               
                if (minPathTaxi != graph.INFINITY){
                    duplicatePathsTaxi.add(new DuplicatePath(minPathTaxi, minCostPathTaxi));
                }
                 int multipleTaxiSize = 0;
                 String[] taxiNum = minCostPathTaxi.split(" ");
                 String start = taxiNum[0];
                 String dest = taxiNum[taxiNum.length - 1];
                 for (DuplicatePath d :graph.shortestMultiple){
                    String[] dPath = d.getPath().split(" ");
                    if (d.getCost() == minPathTaxi && dPath[0].equals(start) && dPath[dPath.length - 1].equals(dest)){
                        multipleTaxiSize++;
                    }
                 }
    
                //drop-off
                graph.clearMultiple();             
                pathDuplicate = "";           
                for (int i = 0; i < shops.length; i++){
                    graph.dijkstra(call);
                    if (graph.getVertexMap(shops[i]) != null){
                      if((minPathShop == graph.getVertexMap(shops[i]).dist && minPathShop != graph.INFINITY))  {
                        multipleCostShop = graph.getVertexMap(shops[i]).dist;
                        pathDuplicate = graph.printPath(shops[i]);
                        duplicatePathsShop.add(new DuplicatePath(multipleCostShop, pathDuplicate));        
                      }
                       
                    if(minPathShop > graph.getVertexMap(shops[i]).dist){
                        minPathShop = graph.getVertexMap(shops[i]).dist;
                        minCostPathShop = graph.printPath(shops[i]);
                    }
                }
                
                }  
               
                if (minPathShop != graph.INFINITY){
                    duplicatePathsShop.add(new DuplicatePath(minPathShop, minCostPathShop));
                }
                int multipleShopSize = 0;  
                String[] shopNum = minCostPathShop.split(" ");
                start = shopNum[0];
                dest = shopNum[shopNum.length - 1];
                for(DuplicatePath d : graph.shortestMultiple){
                    String[] dPath = d.getPath().split(" ");
                    if(d.getCost() == minPathShop && dPath[0].equals(start) && dPath[dPath.length - 1].equals(dest)){
                        multipleShopSize++;
                    }
                }

                if (duplicatePathsShop.size() > 0 && duplicatePathsTaxi.size() > 0){
                    if (multipleTaxiSize > 0){
                        String[] taxiMultPath = minCostPathTaxi.split(" ");
                        System.out.println("taxi " + taxiMultPath[0]);
                        System.out.println("multiple solutions cost " + (int)minPathTaxi);
                        
                    }
                    else if (minPathTaxi != graph.INFINITY){
                        for (DuplicatePath d : duplicatePathsTaxi){
                            if (d.getCost() == minPathTaxi && !(d.getPath().isEmpty())){                       
                                    String[] num = d.getPath().split(" ");
                                    System.out.println("taxi " + num[0]);
                                    System.out.println(d.getPath());
                            } 
                        }
                    }
                  
                    if (multipleShopSize > 0){
                        String[] num = minCostPathShop.split(" ");

                        System.out.println("shop " + num[num.length - 1]);
                        System.out.println("multiple solutions cost " + (int)minPathShop);
                    }     
                    else if (minPathShop != graph.INFINITY){
                        for (DuplicatePath d : duplicatePathsShop){
                            if (d.getCost() == minPathShop && !(d.getPath().isEmpty())){
                                String num[] = d.getPath().split(" ");
                                System.out.println("shop " + num[num.length - 1]);
                                System.out.println(d.getPath());
                            }
                        }
                    }    
                }
                
                else System.out.println("cannot be helped");;
                
                duplicatePathsTaxi.clear();
                duplicatePathsShop.clear();
                graph.clearMultiple();
                multipleCount = 0;
            }
        }

    }
    


        

/**
 * SimulatorTwo is an extension of SimulatorOne, taking into account realism such as
 * the maintanence of taxis, traffic reports and other compeitors at certain stops.
 * 
 * @author Joshua Diegaardt (DGRJOS001)
 * 5 April 2024
 */

import java.util.PriorityQueue;
import java.util.Scanner;

public class SimulatorTwo {
    public static void main(String[] args){
        Scanner keyboard = new Scanner(System.in);
        int graphSize = Integer.parseInt(keyboard.nextLine());
        GraphDijkstra graph = new GraphDijkstra();

            //adds initial vertices to the graph with costs between edges
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

            //QnQ shops
            Integer.parseInt(keyboard.nextLine());
            String[] shopQnQ = keyboard.nextLine().split(" ");
            
            //Shopfite shops
            Integer.parseInt(keyboard.nextLine());
            String[] shopFite = keyboard.nextLine().split(" ");
            
            //QnQ clients
            Integer.parseInt(keyboard.nextLine());
            String[] clientsQnQ = keyboard.nextLine().split(" ");

            //Shopfite clients
            Integer.parseInt(keyboard.nextLine());
            String[] clientsShopFite = keyboard.nextLine().split(" ");

            int numTraffic = Integer.parseInt(keyboard.nextLine());
            /*Traffic report: if Road Block (RB), multiply initial cost by 100 and if Road Works (RW) multiply initial cost by 3*/
            for (int i = 0; i < numTraffic; i++){
                String[] traffic = keyboard.nextLine().split(" ");
                graph.changeEdge(traffic[0], traffic[1], Double.parseDouble(traffic[2]), traffic[3]);
            }

            //QnQ loop handles the clients and taxis of QnQ stores
            System.out.println("QnQ clients and taxis");
            PriorityQueue<DuplicatePath> QnQPickUp = new PriorityQueue<>();
            PriorityQueue<DuplicatePath> QnQDropOff = new PriorityQueue<>();
            double minCostPickup = Double.MAX_VALUE;
            double minCostDropOff = Double.MAX_VALUE;
            String minPathPickUp = "";
            String minPathDropOff = "";
            double multiplePickUp = 0;
            double multipleDropOff = 0;
            String pathDuplicate = "";

            for(String call : clientsQnQ){
                minCostPickup = Double.MAX_VALUE;
                minCostDropOff = Double.MAX_VALUE;
                minPathPickUp = "";
                minPathDropOff = "";
                multiplePickUp = 0;
                multipleDropOff = 0;
                pathDuplicate = "";
                graph.clearMultiple();

                System.out.println("client " + call);
                for (int i = 0; i < shopQnQ.length; i++){
                    //this loop finds the shortest path from any QnQ shop to the given QnQ client
                    //if there are multiple shortest costs from different stores, then all are printed
                    graph.dijkstra(shopQnQ[i]);                
                    if (graph.getVertexMap(call) != null){
                        //checks for duplicate shortest costs from different stores
                        if (minCostPickup == graph.getVertexMap(call).dist && minCostPickup != Double.MAX_VALUE){
                            multiplePickUp = graph.getVertexMap(call).dist;
                            pathDuplicate = graph.printPath(call);
                            QnQPickUp.add(new DuplicatePath(multiplePickUp, pathDuplicate));
                        }
                        //determines the lowest cost and path from each store to the given client
                        if (minCostPickup > graph.getVertexMap(call).dist){
                            minCostPickup = graph.getVertexMap(call).dist;
                            minPathPickUp = graph.printPath(call);
                        }
                    }
                }
                //adds to a priority queue if the shortest cost value has been updated, meaning that there
                //is a shortest cost, and therefore path
                if(minCostPickup != Double.MAX_VALUE){
                    QnQPickUp.add(new DuplicatePath(minCostPickup, minPathPickUp));
                }
                int QnQmultiplePickUp = 0;
                String[] QnQtaxiPath = minPathPickUp.split(" ");
                String start = QnQtaxiPath[0];
                String dest = QnQtaxiPath[QnQtaxiPath.length - 1];
                //checks for duplicates where the shortest costs are the same, as well as the start and destination vertices
                //this is used to determine if there are "multiple solutions"
                for (DuplicatePath d :graph.shortestMultiple){
                    String[] dPath = d.getPath().split(" ");
                    if (d.getCost() == minCostPickup && dPath[0].equals(start) && dPath[dPath.length - 1].equals(dest)){
                        QnQmultiplePickUp++;
                    }
                }

                //drop off
                graph.clearMultiple();
                pathDuplicate = "";
                for (int i =  0; i < shopQnQ.length; i++){
                    graph.dijkstra(call);
                    if (graph.getVertexMap(shopQnQ[i]) != null){
                        //determines duplicates client to store
                        if (minCostDropOff == graph.getVertexMap(shopQnQ[i]).dist && minCostDropOff != Double.MAX_VALUE){
                            multipleDropOff = graph.getVertexMap(shopQnQ[i]).dist;
                            pathDuplicate = graph.printPath(shopQnQ[i]);
                            QnQDropOff.add( new DuplicatePath(multipleDropOff, pathDuplicate));
                        }
                        //determines minimum cost and path from the client to a QnQ store
                        if (minCostDropOff > graph.getVertexMap(shopQnQ[i]).dist){
                            minCostDropOff = graph.getVertexMap(shopQnQ[i]).dist;
                            minPathDropOff = graph.printPath(shopQnQ[i]);
                        }
                    }
                }
                //there is a lowest cost and, therefore, a shortest path
                if (minCostDropOff != Double.MAX_VALUE){
                    QnQDropOff.add(new DuplicatePath(minCostDropOff, minPathDropOff));
                }

                int QnQmultipleDropOff = 0;
                String[] QnQshopPath = minPathDropOff.split(" ");
                start = QnQshopPath[0];
                dest = QnQshopPath[QnQshopPath.length - 1];
                //used to determine duplicates with the same shortest cost and start and end vertices
                for(DuplicatePath d: graph.shortestMultiple){
                    String[] dPath = d.getPath().split(" ");
                    if (d.getCost() == minCostDropOff && dPath[0].equals(start) && dPath[dPath.length - 1].equals(dest)){
                        QnQmultipleDropOff++;
                    }
                }

                //checks if there is a shortest path from both a shop to a client and also from a client to a shop
                if (QnQPickUp.size() > 0 && QnQDropOff.size() > 0){
                    //multiple solutions found
                    if (QnQmultiplePickUp > 0){
                        String[] QnQPickUpMultPath = minPathPickUp.split(" ");
                        System.out.println("taxi " + QnQPickUpMultPath[0]);
                        System.out.println("multiple solutions cost " + (int)minCostPickup);
                    }
                    //same lowest cost but different start and end vertices
                    else if(minCostPickup != Double.MAX_VALUE){
                        for (DuplicatePath d: QnQPickUp){
                            String[] numPickUp = d.getPath().split(" ");
                            System.out.println("taxi " + numPickUp[0]);
                            System.out.println(d.getPath() + " cost: " + minCostPickup);
                        }
                    }
                    //multiple solutions found
                    if (QnQmultipleDropOff > 0){
                        String[] QnQDropOffMultPath = minPathDropOff.split(" ");
                        System.out.println("shop " + QnQDropOffMultPath[QnQDropOffMultPath.length - 1]);
                        System.out.println("multiple solutions " + (int)minCostDropOff);
                    }
                    //same lowest cost but different start and end vertices
                    else if (minCostDropOff != Double.MAX_VALUE){
                        for (DuplicatePath d : QnQDropOff){
                            String[] numDropOff = d.getPath().split(" ");
                            System.out.println("shop " + numDropOff[numDropOff.length - 1]);
                            System.out.println(d.getPath() + " cost: " + minCostDropOff);
                        }
                    }
                    //if the total cost of the trip exceeds 1 000, it is not feasible as the taxi will be unable to pick up a client and drop them off without facing issues
                    if (minCostPickup + minCostDropOff > 1000){
                        System.out.println("This trip exceeds 1 000 and is not feasible");
                    }
                }
                //this occurs if there is not path from a store to a client and also from a client to a store
                else System.out.println("cannot be helped");
                
                QnQPickUp.clear();
                QnQDropOff.clear();
                graph.clearMultiple();
                System.out.println();
            }

                 //ShopFite loop handles the clients and taxis of ShopFite stores
                System.out.println("ShopFite clients and taxis");
                PriorityQueue<DuplicatePath> SFPickUp = new PriorityQueue<>();
                PriorityQueue<DuplicatePath> SFDropOff = new PriorityQueue<>();
                minCostPickup = Double.MAX_VALUE;
                minCostDropOff = Double.MAX_VALUE;
                minPathPickUp = "";
                minPathDropOff = "";
                multiplePickUp = 0;
                multipleDropOff = 0;
                pathDuplicate = "";
    
                for(String call : clientsShopFite){
                    minCostPickup = Double.MAX_VALUE;
                    minCostDropOff = Double.MAX_VALUE;
                    minPathPickUp = "";
                    minPathDropOff = "";
                    multiplePickUp = 0;
                    multipleDropOff = 0;
                    pathDuplicate = "";
                    graph.clearMultiple();
    
                    System.out.println("client " + call);
                    //this loop finds the shortest path from any ShopFite shop to the given ShopFite client
                    //if there are multiple shortest costs from different stores, then all are printed
                    for (int i = 0; i < shopFite.length; i++){
                        graph.dijkstra(shopFite[i]);                    
                        if (graph.getVertexMap(call) != null){
                            //checks for duplicate shortest costs from different stores
                            if (minCostPickup == graph.getVertexMap(call).dist && minCostPickup != Double.MAX_VALUE){
                                multiplePickUp = graph.getVertexMap(call).dist;
                                pathDuplicate = graph.printPath(call);
                                SFPickUp.add(new DuplicatePath(multiplePickUp, pathDuplicate));
                            }
                            //determines the lowest cost and path
                            if (minCostPickup > graph.getVertexMap(call).dist){
                                minCostPickup = graph.getVertexMap(call).dist;
                                minPathPickUp = graph.printPath(call);
                            }
                        }
                    }
                     //adds to a priority queue if the shortest cost value has been updated, meaning that there
                    //is a shortest cost, and therefore path
                    if(minCostPickup != Double.MAX_VALUE){
                        SFPickUp.add(new DuplicatePath(minCostPickup, minPathPickUp));
                    }

                    int SFmultiplePickUp = 0;
                    String[] SFtaxiPath = minPathPickUp.split(" ");
                    String start = SFtaxiPath[0];
                    String dest = SFtaxiPath[SFtaxiPath.length - 1];
                    //checks for duplicates where the shortest costs are the same, as well as the start and destination vertices
                    //this is used to determine if there are "multiple solutions"
                    for (DuplicatePath d : graph.shortestMultiple){
                        String[] dPath = d.getPath().split(" ");
                        if (d.getCost() == minCostPickup && dPath[0].equals(start) && dPath[dPath.length - 1].equals(dest)){
                            SFmultiplePickUp++;
                        }
                    }
    
                    //drop off
                    graph.clearMultiple();
                    pathDuplicate = "";
                    for (int i =  0; i < shopFite.length; i++){
                        graph.dijkstra(call);
                        if (graph.getVertexMap(shopFite[i]) != null){
                            //determines duplicates from client to store
                            if (minCostDropOff == graph.getVertexMap(shopFite[i]).dist && minCostDropOff != Double.MAX_VALUE){
                                multipleDropOff = graph.getVertexMap(shopFite[i]).dist;
                                pathDuplicate = graph.printPath(shopFite[i]);
                                SFDropOff.add( new DuplicatePath(multipleDropOff, pathDuplicate));
                            }
                            //determines minimum cost and path from the client to a ShopFite store
                            if (minCostDropOff > graph.getVertexMap(shopFite[i]).dist){
                                minCostDropOff = graph.getVertexMap(shopFite[i]).dist;
                                minPathDropOff = graph.printPath(shopFite[i]);
                            }
                        }
                    }
                    //there is a lowest cost and, therefore, a shortest path
                    if (minCostDropOff != Double.MAX_VALUE){
                        SFDropOff.add(new DuplicatePath(minCostDropOff, minPathDropOff));
                    }
    
                    int SFmultipleDropOff = 0;
                    String[] SFshopPath = minPathDropOff.split(" ");
                    start = SFshopPath[0];
                    dest = SFshopPath[SFshopPath.length - 1];
                    //used to determine duplicates with the same shortest cost and start and end vertices
                    for(DuplicatePath d: graph.shortestMultiple){
                        String[] dPath = d.getPath().split(" ");
                        if (d.getCost() == minCostDropOff && dPath[0].equals(start) && dPath[dPath.length - 1].equals(dest)){
                            SFmultipleDropOff++;
                        }
                    }
                    
                    //checks if there is a shortest path from both a shop to a client and also from a client to a shop
                    if (SFPickUp.size() > 0 && SFDropOff.size() > 0){
                        //multiple solutions found
                        if (SFmultiplePickUp > 0){
                            String[] SFPickUpMultPath = minPathPickUp.split(" ");
                            System.out.println("taxi " + SFPickUpMultPath[0]);
                            System.out.println("multiple solutions cost " + (int)minCostPickup);
                        }
                        //same lowest cost but different start and end vertices
                        else if(minCostPickup != Double.MAX_VALUE){
                            for (DuplicatePath d: SFPickUp){
                                String[] numPickUp = d.getPath().split(" ");
                                System.out.println("taxi " + numPickUp[0]);
                                System.out.println(d.getPath() + " cost: " + minCostPickup);
                            }
                        }
                        //multiple solutions found
                        if (SFmultipleDropOff > 0){
                            String[] SFDropOffMultPath = minPathDropOff.split(" ");
                            System.out.println("shop " + SFDropOffMultPath[SFDropOffMultPath.length - 1]);
                            System.out.println("multiple solutions " + (int)minCostDropOff);
                        }
                        //same lowest cost but different start and end vertices
                        else if (minCostDropOff != Double.MAX_VALUE){
                            for (DuplicatePath d : SFDropOff){
                                String[] numDropOff = d.getPath().split(" ");
                                System.out.println("shop " + numDropOff[numDropOff.length - 1]);
                                System.out.println(d.getPath() + " cost: " + minCostDropOff);
                            }
                        }
                        //if the total cost of the trip exceeds 1 000, it is not feasible as the taxi will be unable to pick up a client and drop them off without facing issues
                        if (minCostPickup + minCostDropOff > 1000){
                            System.out.println("This trip exceeds 1 000 and is not feasible");
                            }
                    }
                    //this occurs if there is not path from a store to a client and also from a client to a store
                    else System.out.println("cannot be helped");

                    SFPickUp.clear();
                    SFDropOff.clear();
                    graph.clearMultiple();
                    System.out.println();
                }
            }
    }


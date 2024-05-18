# ShortestRoute
This is a university assignment where you are given a series of nodes, and need to find the shortest path.

# # # It takes in input one line at a time.

The scenario is a cheap taxi service where there taxis at some nodes and clients and other nodes. Given an a text file with the nodes of
taxis and the nodes of clients, the program will output the shortest path from a taxi to a given client, and then from that client to a taxi at a shop.

At the top of the text file are the number of nodes. Below that line are the number of taxi nodes, with each taxi node below that. Below this is the number of clients, with each client node below that.

Each line of the text file has at least a start node, adjacent node and cost of the edge between those nodes. There can be multiple adjacent nodes and costs associated with a start node in a line of the text file. An example would be:
5 //total number of nodes
0 4 15 //node 0 is neighbours with node 4 and the edge between them is 15
1 0 14 2 7 3 23 // node 1 is neighbours with node 0, and the edge cost is 14. node 1 is neighbours with node 2 and the edge cost is 7. node 1
is neighbours with node 3 and the edge cost is 23
2 0 7  //same as before
3 1 23 4 16 //same as before
4 2 15 3 9 //same as before
2 //taxi nodes
0 3  //nodes 0 and 3 are taxi nodes
3 //number of client nodes
1 4 2 //nodes 1, 4 and 2 are client nodes

The expected output:
client 1 //first callout, follwed by client 4 and then client 2
taxi 3 //the taxi where the shortest path starts
3 1
shop 0  //the nearest shop that results in the shortest path
multiple solutions cost 14 // the paths 1 0 and 1 2 0 both result in 14
client 4 // second callout is client 4
taxi 0 //the taxi where the shortest path starts
0 4 //the path
shop 3 //the neares shop from node 4
4 3
client 2 //client 2
taxi 0 //one of the taxis where the shortest path starts and has the same drop-off spot
0 4 2 // the path
taxi 3 //one of the taxis where the shortest path starts and has the same drop-off spot
3 1 2
shop 0 // from either the path starting at 0 or at 3, the closest store is at node 0
2 0 //

SimulatorOne.java does these shortest path calculations. An importnant note is that Dijkstra's algorithm is used, so only positive weights are allowed.

In SimulatorTwo, there are additions that cater to real world environments. The first addition is
that there are different stores that only cater towards their customers. The addition separates store
and client vertices into QnQ and ShopFite store vertices, and QnQ and ShopFite client vertices. Only 
QnQ clients can be picked up by taxis from QnQ stops and taken to QnQ stores, and only clients from ShopFite
can be picked up by taxis from ShopFite stops and taken to ShopFite stores. After the graph is populated, the
number of QnQ stores, ShopFite stores, QnQ clients and ShopFite clienst are given in this order. The type of 
client would depend on the store to where they wanted to go. 

Additionally, traffic reports are taken into account. There are initial edge costs between vertices,
but these change when there are changes in traffic. This addition takes road blocks and road work into 
account, which update the edge cost between affected vertices. The traffic report is added at the end of 
the input, beginning with the number of reports, followed by the affected routes (in the form: start destination cost incident)
and the incident (RB for roadblock) and (RW for road work). If there is a road block, the initial cost
is multiplied by 10 which makes that route unusable, and if there is road work then the initial cost is multiplied by 3,
which would slow drivers down and discourage these routes. If there is a false alarm that is neither RB nor RW, 
the initial cost remains unchanged. The change are entered line-by-line for each edge (start destination cost).

Finally, there is an addition that informs the driver if a route is not feasible. Seeing that this is a "very cheap taxi
service," the case can be made that these stores would want to preserve these taxis as long as possible, so they
would need to be maintained whenever a cost of 1 000 has been travelled. If the total cost of a trip (pick-up +
drop-off) exceeds 200, then the stores are informed that this trip is not feasible as it would jeopradise the
condition of the taxi. Taxis are maintained at stores, so the shortest cost from a store (pick-up) to another
store (drop-off) should be less than 1 000 in order for the trip to be taken.

Here is a modified example of the format, from the example in the assignment. When using inputs, only the
numbers are used.
5    //number of vertices
0 4 15  //start destination cost
1 0 14 2 7 3 23 //start destination cost
2 0 7 //start destination cost
3 1 23 4 16 //start destination cost
4 2 15 3 9 //start destination cost
1 //number of QnQ stores
0 //QnQ stores
1 //number of ShopFite stores
3 //ShopFite stores
2 //number of QnQ clients
1 4 //QnQ clients
1 //number of ShopFite clients
2 //ShopFite clients
2 //number of incidents from traffic reports
0 4 15 rb //start destination initial cost road block
2 0 7 rw //start destination initial cost road work 
3 4 16 false alarm //edge cost remains unchanged 

The expected output:
QnQ clients and taxis
client 1
taxi 0
0 4 3 1 cost: 1532.0
shop 0
1 0 cost: 14.0
This trip exceeds 1 000 and is not feasible

client 4
taxi 0
0 4 cost: 1500.0
shop 0
4 2 0 cost: 36.0
This trip exceeds 1 000 and is not feasible

ShopFite clients and taxis
client 2
taxi 3
3 1 2 cost: 30.0
shop 3
2 0 4 3 cost: 1530.0
This trip exceeds 1 000 and is not feasible




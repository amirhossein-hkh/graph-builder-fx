package graphbuilder;

import java.io.Serializable;
import java.util.ArrayList;

public class Graph implements Serializable {
    private ArrayList<Edge> edges;
    private ArrayList<Node> nodes;

    public Graph(ArrayList<Edge> edges, ArrayList<Node> nodes){
        this.edges = edges;
        this.nodes = nodes;
    }
}

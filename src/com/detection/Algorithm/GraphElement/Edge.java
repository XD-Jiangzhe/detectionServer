package com.detection.Algorithm.GraphElement;

import java.io.Serializable;

public class Edge implements Serializable {

    private static final long serialVersionUID = 7189677614448124729L;

    /*the graph to which the edge belongs*/
    public Graph graph;

    /*the source origin of the edge*/
    public Node source;

    /*the target destination of the edge*/
    public Node target;

    /*the label of this edge*/
    int label;

    Edge(Graph g, Node source, Node target, int label) {
        /** Writen by  : Huanran Wang
         *  Created on : 2018.7.20 at 19:36.
         *  Description: In Edge we will creates new edge. */

        this.graph = g;
        this.source = source; // store source
        source.outEdges.add(this); // update edge list at source
        this.target = target; // store target
        target.inEdges.add(this); // update edge list at target
        this.label = label;
    }
}

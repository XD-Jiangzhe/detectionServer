package com.detection.Algorithm.GraphElement;

import java.io.Serializable;
import java.util.ArrayList;

public class Graph implements Serializable {

    private static final long serialVersionUID = -1702620197933766002L;

    //orgb 图的 name 和 familyname 都是类的名称
    //org 的图的名称是test ，类型是 "null"
    /*name of the graph*/
    public String name;

    private String familyname;//图的家族名称

    /*list of all nodes*/
    public ArrayList<Node> nodes = new ArrayList<>();

    /*list of all edges*/
    public ArrayList<Edge> edges = new ArrayList<>();

    //孤立点
    public ArrayList<Integer> iso_list = new ArrayList<>();

    //邻接矩阵
    /*stores graph structure as adjacency matrix (-1: not adjacent, >=0: the edge label)*/
    private int[][] adjacencyMatrix;

    /* indicates if the adjacency matrix needs an update*/
    private boolean adjacencyMatrixUpdateNeeded = true;

    public Graph(String name, String familyname) {
        this.familyname = familyname;
        this.name = name;
    }

    public void addEdge(int sourceId, int targetId, int label) {

        this.addEdge(nodes.get(sourceId), nodes.get(targetId), label);
    }

    private void addEdge(Node source, Node target, int label) {

        edges.add(new Edge(this, source, target, label));
        adjacencyMatrixUpdateNeeded = true;
    }

    public void addNode(int id, int label, String name) {

        nodes.add(new Node(this, id, label, name));
        adjacencyMatrixUpdateNeeded = true;
    }

    @SuppressWarnings("unused")
    public ArrayList<Integer> getIso_list() {
        return iso_list;
    }

    public void setIso_list(ArrayList<Integer> iso_list) {

        this.iso_list = iso_list;
    }

    @SuppressWarnings("unused")
    public void printGraph() {
        /** Writen by  : Huanran Wang
         *  Created on : 2018.7.20 at 19:26.
         *  Description: In printGraph we will print the graph. */

        int[][] a = getAdjacencyMatrix();
        int k = a.length;

        System.out.print("   ");
        for (Node n : nodes) {
            System.out.format("%3d", n.id);
        }
        System.out.println("\n");
        for (int i = 0; i < k; i++) {
            System.out.format("%3d", i);
            for (int j = 0; j < k; j++) {
                System.out.format("%3d", a[i][j]);
            }
            System.out.println("\n");
        }
    }

    public int[][] getAdjacencyMatrix() {
        /** Writen by  : Huanran Wang
         *  Created on : 2018.7.20 at 19:26.
         *  Description: In getAdjacencyMatrix we will get the adjacency matrix Reconstruct it if it needs an update. */

        if (adjacencyMatrixUpdateNeeded) {
            int k = nodes.size();
            adjacencyMatrix = new int[k][k]; // node size may have changed
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < k; j++) {
                    adjacencyMatrix[i][j] = -1;
                }
            }
            for (Edge e : edges) {
                // label must bigger than -1
                adjacencyMatrix[e.source.id][e.target.id] = e.label;
            }
            adjacencyMatrixUpdateNeeded = false;
        }
        return adjacencyMatrix;
    }

    @SuppressWarnings("unused")
    public String getFamilyname() {

        return familyname;
    }

    @SuppressWarnings("unused")
    public void setFamilyname(String familyname) {

        this.familyname = familyname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
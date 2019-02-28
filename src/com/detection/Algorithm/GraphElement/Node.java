package com.detection.Algorithm.GraphElement;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("ALL")
public class Node implements Serializable {

    private static final long serialVersionUID = 1527548695455516243L;

    public ArrayList<Edge> outEdges = new ArrayList<>();
    public ArrayList<Edge> inEdges = new ArrayList<>();


    public Graph graph; //点所在的图
    public int id; // 点的id
    private int label; // for semantic feasibility checks
    public String name;
    public int indegree = 0;
    public int outdegree = 0;

    public int max_succ_in = 0;//后继节点的最大入度
    public int max_pred_in = 0;//前驱节点的最大入度
    public int max_succ_out = 0;//后继节点的最大出度
    public int max_pred_out = 0;//前驱节点的最大出度

    public int sum_pred_in = 0;
    public int sum_succ_in = 0;
    public int sum_pred_out = 0;
    public int sum_succ_out = 0;

    public Node(Graph g, int id, int label, String name) {
        graph = g;
        this.id = id;
        this.label = label;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        Node param = (Node) obj;
        return name == param.name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIndegree() {
        return indegree;
    }

    public void setIndegree(int indegree) {
        this.indegree = indegree;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public int getMax_pred_in() {
        return max_pred_in;
    }

    public void setMax_pred_in(int max_pred_in) {
        this.max_pred_in = max_pred_in;
    }

    public int getMax_pred_out() {
        return max_pred_out;
    }

    public void setMax_pred_out(int max_pred_out) {
        this.max_pred_out = max_pred_out;
    }

    public int getMax_succ_in() {
        return max_succ_in;
    }

    public void setMax_succ_in(int max_succ_in) {
        this.max_succ_in = max_succ_in;
    }

    public int getMax_succ_out() {
        return max_succ_out;
    }

    public void setMax_succ_out(int max_succ_out) {
        this.max_succ_out = max_succ_out;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOutdegree() {
        return outdegree;
    }

    public void setOutdegree(int outdegree) {
        this.outdegree = outdegree;
    }
}

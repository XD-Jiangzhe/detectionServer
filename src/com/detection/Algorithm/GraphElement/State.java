package com.detection.Algorithm.GraphElement;

import com.detection.Algorithm.GraphElement.Edge;
import com.detection.Algorithm.GraphElement.Graph;
import com.detection.Algorithm.GraphElement.Node;

import java.io.*;
import java.util.HashSet;

public class State {

    /*org 的节点，如果没有匹配则是-1，如果有匹配则是orgb 的index*/
    public  int[] core_1;

    /*orgb 的节点，如果没有匹配则是-1*/
    public  int[] core_2;

    //存放对应节点的深度
    /* stores for each ORG node the depth in the search tree at which it entered "T_1 in" or the mapping ("-1" indicates that the node is not
     * part of the set)*/
    public int[] in_1;

    /*stores for each ORGB node the depth in the search tree at which it entered "T_2 in" or the mapping ("-1" indicates that the node is not
     * part of the set)*/
    public int[] in_2;

    /*stores for each ORG node the depth in the search tree at which it entered "T_1 out" or the mapping ("-1" indicates that the node is not
     * part of the set)*/
    public int[] out_1;

    /*stores for each query graph node the depth in the search tree at which it entered "T_2 out" or the mapping ("-1" indicates that the node
     * is not part of the set)*/
    public int[] out_2;

    /*nodes that not yet in the partial mapping, that are the destination of branches start from ORG*/
    public HashSet<Integer> T1in;

    /*nodes that not yet in the partial mapping, that are the origin of branches end into ORG*/
    public HashSet<Integer> T1out;

    /*nodes that not yet in the partial mapping, that are the destination of branches start from ORGB*/
    public HashSet<Integer> T2in;

    /*nodes that not yet in the partial mapping, that are the origin of branches end into ORGB*/
    public HashSet<Integer> T2out;

    /* unmapped nodes in ORG*/
    public HashSet<Integer> unmapped1;

    /*unmapped nodes in ORGB*/
    public HashSet<Integer> unmapped2;

    /* current depth of the search tree*/
    public int depth = 0;

    public boolean matched = false;

    public Graph org;
    public Graph orgb;

    public State(Graph org, Graph orgb)
    {
        this.org = org;
        this.orgb = orgb;

        int orgSize = org.nodes.size();
        int orgbSize = orgb.nodes.size();

        T1in = new HashSet<>(orgSize * 2);
        T1out = new HashSet<>(orgSize * 2);
        T2in = new HashSet<>(orgbSize * 2);
        T2out = new HashSet<>(orgbSize * 2);

        unmapped1 = new HashSet<>(orgSize * 2);
        unmapped2 = new HashSet<>(orgbSize * 2);

        core_1 = new int[orgSize];
        core_2 = new int[orgbSize];

        in_1 = new int[orgSize];
        in_2 = new int[orgbSize];
        out_1 = new int[orgSize];
        out_2 = new int[orgbSize];

        /*initialize values ("-1" means no mapping / not contained in the set) initially, all sets are empty and no nodes are mapped*/
        for (int i = 0; i < orgSize; i++) {
            core_1[i] = -1;
            in_1[i] = -1;
            out_1[i] = -1;
            unmapped1.add(i);
        }
        for (int i = 0; i < orgbSize; i++) {
            core_2[i] = -1;
            in_2[i] = -1;
            out_2[i] = -1;
            unmapped2.add(i);
        }
    }


    public void backtrack(int targetNodeIndex, int queryNodeIndex) {
        /** Writen by  : Huanran Wang
         *  Created on : 2018.7.20 at 19:30.
         *  Description: In backtrack we will remove the match of (targetNodeIndex, queryNodeIndex) for backtrack. */

        core_1[targetNodeIndex] = -1;
        core_2[queryNodeIndex] = -1;
        unmapped1.add(targetNodeIndex);
        unmapped2.add(queryNodeIndex);

        for (int i = 0; i < core_1.length; i++) {
            if (in_1[i] == depth) {
                in_1[i] = -1;
                T1in.remove(i);
            }
            if (out_1[i] == depth) {
                out_1[i] = -1;
                T1out.remove(i);
            }
        }
        for (int i = 0; i < core_2.length; i++) {
            if (in_2[i] == depth) {
                in_2[i] = -1;
                T2in.remove(i);
            }
            if (out_2[i] == depth) {
                out_2[i] = -1;
                T2out.remove(i);
            }
        }

        // put orgIndex and orgbIndex back into Tin and Tout sets if necessary
        if (inT1in(targetNodeIndex)) {
            T1in.add(targetNodeIndex);
        }
        if (inT1out(targetNodeIndex)) {
            T1out.add(targetNodeIndex);
        }
        if (inT2in(queryNodeIndex)) {
            T2in.add(queryNodeIndex);
        }
        if (inT2out(queryNodeIndex)) {
            T2out.add(queryNodeIndex);
        }

        depth--;
    }

    public Boolean inT1in(int nodeId) {

        return core_1[nodeId] == -1 && in_1[nodeId] > -1;
    }

    public Boolean inT1out(int nodeId) {

        return core_1[nodeId] == -1 && out_1[nodeId] > -1;
    }

    public Boolean inT2in(int nodeId) {

        return core_2[nodeId] == -1 && in_2[nodeId] > -1;
    }

    public Boolean inT2out(int nodeId) {

        return core_2[nodeId] == -1 && out_2[nodeId] > -1;
    }

    private Boolean inM1(int nodeId) {

        return core_1[nodeId] > -1;
    }

    private Boolean inM2(int nodeId) {

        return core_2[nodeId] > -1;
    }

    public Boolean inN1Tilde(int nodeId) {

        return core_1[nodeId] == -1 && in_1[nodeId] == -1 && out_1[nodeId] == -1;
    }

    public Object deepCopy() throws IOException, ClassNotFoundException {

        ByteArrayOutputStream bop = new ByteArrayOutputStream();

        ObjectOutputStream oos = new ObjectOutputStream(bop);

        oos.writeObject(this);

        ByteArrayInputStream bis = new ByteArrayInputStream(bop.toByteArray());

        ObjectInputStream ois = new ObjectInputStream(bis);

        return ois.readObject();
    }

    public void extendMatch(int orgIndex, int orgbIndex) {
        /** Writen by  : Huanran Wang
         *  Created on : 2018.7.20 at 19:30.
         *  Description: In extendMatch we will add a new match (orgIndex, orgbIndex) to the state. */

        core_1[orgIndex] = orgbIndex;
        core_2[orgbIndex] = orgIndex;
        unmapped1.remove(orgIndex);
        unmapped2.remove(orgbIndex);
        T1in.remove(orgIndex);
        T1out.remove(orgIndex);
        T2in.remove(orgbIndex);
        T2out.remove(orgbIndex);

        depth++; // move down one level in the search tree

        Node orgNode = org.nodes.get(orgIndex);
        Node orgbNode = orgb.nodes.get(orgbIndex);

        for (Edge e : orgNode.inEdges) {
            if (in_1[e.source.id] == -1) { // if the node is not in T1in or
                // mapping
                in_1[e.source.id] = depth;
                if (!inM1(e.source.id)) {
                    T1in.add(e.source.id);
                }
            }
        }

        for (Edge e : orgNode.outEdges) {
            if (out_1[e.target.id] == -1) { // if the note is not in T1out or
                // mapping
                out_1[e.target.id] = depth;
                if (!inM1(e.target.id)) {
                    T1out.add(e.target.id);
                }
            }
        }

        for (Edge e : orgbNode.inEdges) {
            if (in_2[e.source.id] == -1) { // if the note is not in T2in or
                // mapping
                in_2[e.source.id] = depth;
                if (!inM2(e.source.id)) {
                    T2in.add(e.source.id);
                }
            }
        }

        for (Edge e : orgbNode.outEdges) {
            if (out_2[e.target.id] == -1) { // if the note is not in T2out or
                // mapping
                out_2[e.target.id] = depth;
                if (!inM2(e.target.id)) {
                    T2out.add(e.target.id);
                }
            }
        }
    }

    public Boolean inN2Tilde(int nodeId) {

        return core_2[nodeId] == -1 && in_2[nodeId] == -1 && out_2[nodeId] == -1;
    }

    @SuppressWarnings("unused")
    public Boolean inT1(int nodeId) {

        return inT1in(nodeId) || inT1out(nodeId);
    }

    @SuppressWarnings("unused")
    public Boolean inT2(int nodeId) {

        return inT2in(nodeId) || inT2out(nodeId);
    }

}

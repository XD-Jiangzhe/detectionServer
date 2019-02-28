package com.detection.Algorithm.detectAlgorithm;

import com.detection.Algorithm.GraphElement.Edge;
import com.detection.Algorithm.GraphElement.Graph;
import com.detection.Algorithm.GraphElement.Node;
import com.detection.Algorithm.GraphElement.State;
import javafx.util.Pair;

import java.util.ArrayList;

public class vf2 {

    //检测的接口
    public static  boolean  vf2MatchSerializable(Graph org, Graph orgb)
    {
        State state = new State(org, orgb);
        if(org.nodes.size() < orgb.nodes.size() ||org.edges.size() <orgb.edges.size())
        {
            return false;
        }
        matchRecursive(state, org, orgb, org.iso_list, orgb.iso_list);
        return state.matched;
    }

    public static  boolean matchRecursive(State state, Graph org, Graph orgb, ArrayList<Integer> org_iso_list, ArrayList<Integer> orgb_iso_list)
    {
        if (state.depth >= 0.8 * (orgb.nodes.size() - orgb.iso_list.size()))
        {
            state.matched = true; // found a match
            //剪枝策略
            return true;
        }else
        {
            ArrayList<Pair<Integer, Integer>> candidatePairs = genCandidatePairs(state, org, orgb, org_iso_list, orgb_iso_list);
            for (Pair<Integer, Integer> entry : candidatePairs) {

                // extend the state
                state.extendMatch(entry.getKey(), entry.getValue());
                if (matchRecursive(state, org, orgb, org_iso_list, orgb_iso_list)) {
                    return true;
                }

                // remove the match added before
                state.backtrack(entry.getKey(), entry.getValue());
            }
        }
        return false;
    }

    public static ArrayList<Pair<Integer, Integer>> genCandidatePairs(State state, Graph org, Graph orgb, ArrayList<Integer> org_iso_list, ArrayList<Integer> orgb_iso_list)
    {
        ArrayList<Pair<Integer, Integer>> pairList = new ArrayList<>();
        if (!state.T1in.isEmpty() && !state.T2in.isEmpty() && state.T1in.size() >= state.T2in.size())
        {
            int j = -1;
            for (int i : state.T2in) {
                j = Math.max(i, j);
            }
            for (int i : state.T1in) {
                if (!org_iso_list.contains(i) && !orgb_iso_list.contains(j)) {
                    if (checkForDegree(org, orgb, i, j)) {
                        if (checkFeasibility(state, i, j)) {

                            pairList.add(new Pair<>(i, j));
                        }
                    }
                }
            }
            return pairList;
        }else if (!state.T1out.isEmpty() && !state.T2out.isEmpty() && state.T1out.size() >= state.T2out.size())
        {
            /*Generate candidates from T1out and T2out if they are not empty*/
            int j = -1;
            for (int i : state.T2out) {
                j = Math.max(i, j);
            }
            for (int i : state.T1out) {
                if (!org_iso_list.contains(i) && !orgb_iso_list.contains(j)) {
                    if (checkForDegree(org, orgb, i, j)) {
                        if (checkFeasibility(state, i, j)) {

                            pairList.add(new Pair<>(i, j));
                        }
                    }
                }
            }
            return pairList;
        }else
        {
            // Generate from all unmapped nodes
            int j = -1;
            for (int i : state.unmapped2) {
                if (!orgb_iso_list.contains(i)) {
                    j = Math.max(i, j);
                }
            }
            for (int i : state.unmapped1) {
                if (!org_iso_list.contains(i) && !orgb_iso_list.contains(j)) {
                    if (checkForDegree(org, orgb, i, j)) {
                        if (checkFeasibility(state, i, j)) {
                            pairList.add(new Pair<>(i, j));
                        }
                    }
                }
            }
            return pairList;
        }
    }

    private static boolean checkForDegree(Graph org, Graph orgb, int i, int j)
    {
        /** create by  : Huanran Wang
         *  Created on 2018.7.20 at 18:44.
         *  description: In checkForDegree we will  */


        Node orgb_node = orgb.nodes.get(j);
        Node org_node = org.nodes.get(i);

        if (org_node.outdegree >= orgb_node.outdegree && org_node.indegree >= orgb_node.indegree) {

            if (org_node.max_pred_in >= orgb_node.max_pred_in && org_node.max_pred_out >= orgb_node.max_pred_out && org_node.max_succ_in >= orgb_node.max_succ_in && org_node.max_succ_out >= orgb_node.max_succ_out) {
                return orgb_node.sum_pred_in <= org_node.sum_pred_in && orgb_node.sum_pred_out <= org_node.sum_pred_out && orgb_node.sum_succ_in <= org_node.sum_succ_in && orgb_node.sum_succ_out <= org_node.sum_succ_out;
            }
        }
        return false;
    }


    public static  Boolean checkFeasibility(State state, int orgNodeIndex, int orgbNodeIndex) {
        /** Writen by  : Huanran Wang
         *  Created on : 2018.7.20 at 19:19.
         *  Description: In checkFeasibility we will check the feasibility of adding this match. */

        // Predecessor Rule and Successor Rule
        if (!checkPredAndSucc(state, orgNodeIndex, orgbNodeIndex)) {
            return false;
        }

        // In Rule and Out Rule
        if (!checkInAndOut(state, orgNodeIndex, orgbNodeIndex)) {
            return false;
        }

        // New Rule
        return checkNew(state, orgNodeIndex, orgbNodeIndex);
    }


    private static Boolean checkPredAndSucc(State state, int orgNodeIndex, int orgbNodeIndex) {
        /** Writen by  : Huanran Wang
         *  Created on : 2018.7.20 at 18:50.
         *  Description: In checkPredAndSucc we will check the predecessor rule and successor rule It ensures the  consistency of the partial
         *  matching. */

        //要检测的两个点
        Node orgbNode = state.orgb.nodes.get(orgbNodeIndex);
        Node orgNode = state.org.nodes.get(orgNodeIndex);
        int[][] orgAdjacency = state.org.getAdjacencyMatrix();
        int[][] orgbAdjacency = state.orgb.getAdjacencyMatrix();
        int flagin = 0, flagout = 0;

        /*predecessors of ORG node.*/
        //点的前驱
        for (Edge e : orgNode.inEdges) {
            if (state.core_1[e.source.id] > -1)//若org 的入边 的源点a已经匹配了，则a匹配的orgb中的点b 要和 待检测的点互连
            {
                if (orgbAdjacency[state.core_1[e.source.id]][orgbNodeIndex] != -1) {
                    break;
                }
                flagin = 2;
            }
        }
        //点的出边
        for (Edge e : orgNode.outEdges) {
            if (state.core_1[e.target.id] > -1) {
                if (orgbAdjacency[orgbNodeIndex][state.core_1[e.target.id]] != -1) {
                    break;
                }
                flagout = 2;
            }
        }
        if (flagin == 2 || flagout == 2) {
            return false;
        }

        /* Predecessor Rule For all mapped predecessors of the ORGB node, there must exist corresponding
         * predecessors of ORG node.*/
        for (Edge e : orgbNode.inEdges) {
            if (state.core_2[e.source.id] > -1) {//若该点已经匹配了，且匹配的点是org 的点，该点必须和 考核的org的点相连
                if (orgAdjacency[state.core_2[e.source.id]][orgNodeIndex] == -1) {
                    return false; // not such edge in ORG
                }
                // break;
            }
        }

        /* Successsor Rule For all mapped successors of the query node, there must exist corresponding successors
         * of the target node.*/
        for (Edge e : orgbNode.outEdges) {
            if (state.core_2[e.target.id] > -1) {
                if (orgAdjacency[orgNodeIndex][state.core_2[e.target.id]] == -1) {
                    return false; // not such edge in ORG
                }
                // break;
            }
        }
        return true;
    }


    private static boolean checkInAndOut(State state, int orgNodeIndex, int orgbNodeIndex) {
        /** Writen by  : Huanran Wang
         *  Created on : 2018.7.20 at 19:12.
         *  Description: In checkInAndOut we will check the in rule and out rule This prunes the search tree using. */

        Node orgNode = state.org.nodes.get(orgNodeIndex);
        Node orgbNode = state.orgb.nodes.get(orgbNodeIndex);
        int orgPredCnt = 0, orgSucCnt = 0;
        int orgbPredCnt = 0, orgbSucCnt = 0;

        //规则3 和4
         /*In Rule The number predecessors/successors of the ORG node that are in T1in must be larger than or equal
          to those of the ORGB node that are in T2in.*/
         //T1in 交 Pred 的个数
        for (Edge e : orgNode.inEdges) {
            if (state.inT1in(e.source.id)) {
                orgPredCnt++;
            }
        }
        for (Edge e : orgNode.outEdges) {
            if (state.inT1in(e.target.id)) {
                orgSucCnt++;
            }
        }
        for (Edge e : orgbNode.inEdges) {
            if (state.inT2in(e.source.id)) {
                orgbPredCnt++;
            }
        }
        for (Edge e : orgbNode.outEdges) {
            if (state.inT2in(e.target.id)) {
                orgbSucCnt++;
            }
        }
        if (orgPredCnt < orgbPredCnt || orgSucCnt < orgbSucCnt) {
            return false;
        }

        orgPredCnt = 0;
        orgSucCnt = 0;
        orgbPredCnt = 0;
        orgbSucCnt = 0;

        /* Out Rule The number predecessors/successors of the target node that are in T1out must be larger than or
         * equal to those of the query node that are in T2out.*/
        for (Edge e : orgNode.inEdges) {
            if (state.inT1out(e.source.id)) {
                orgPredCnt++;
            }
        }
        for (Edge e : orgNode.outEdges) {
            if (state.inT1out(e.target.id)) {
                orgSucCnt++;
            }
        }
        for (Edge e : orgbNode.inEdges) {
            if (state.inT2out(e.source.id)) {
                orgbPredCnt++;
            }
        }
        for (Edge e : orgbNode.outEdges) {
            if (state.inT2out(e.target.id)) {
                orgbSucCnt++;
            }
        }

        return orgPredCnt >= orgbPredCnt && orgSucCnt >= orgbSucCnt;
    }


    private static boolean checkNew(State state, int orgNodeIndex, int orgbNodeIndex) {
        /** Writen by  : Huanran Wang
         *  Created on : 2018.7.20 at 18:48.
         *  Description: In checkNew we will check the new rule This prunes the search tree using 2-look-ahead. */

        Node orgNode = state.org.nodes.get(orgNodeIndex);
        Node orgbNode = state.orgb.nodes.get(orgbNodeIndex);
        int orgPredCnt = 0, orgSucCnt = 0;
        int orgbPredCnt = 0, orgbSucCnt = 0;

        /* In Rule The number predecessors/successors of the ORG node that are in T1in must be larger than or equal
         * to those of the ORGB node that are in T2in. */
        for (Edge e : orgNode.inEdges) {
            if (state.inN1Tilde(e.source.id)) {
                orgPredCnt++;
            }
        }
        for (Edge e : orgNode.outEdges) {
            if (state.inN1Tilde(e.target.id)) {
                orgSucCnt++;
            }
        }
        for (Edge e : orgbNode.inEdges) {
            if (state.inN2Tilde(e.source.id)) {
                orgbPredCnt++;
            }
        }
        for (Edge e : orgbNode.outEdges) {
            if (state.inN2Tilde(e.target.id)) {
                orgbSucCnt++;
            }
        }
        return orgPredCnt >= orgbPredCnt && orgSucCnt >= orgbSucCnt;
    }
}

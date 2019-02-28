package com.detection.Algorithm.detectAlgorithm.parallelDetection;

import com.detection.Algorithm.GraphElement.Graph;
import com.detection.Algorithm.GraphElement.State;
import com.detection.Algorithm.detectAlgorithm.vf2;
import javafx.util.Pair;


import java.util.ArrayList;

/*
    实现每个最小单位的图匹配，即将候选分成若干份后，
    继续对划分后的候选进行匹配
 */
public class parallelRecursiveTask {

    private boolean keep = true;

    public parallelRecursiveTask(boolean keep) {
        this.keep = keep;
    }

    public boolean isKeep() {
        return keep;
    }

    public void setKeep(boolean keep) {
        this.keep = keep;
    }


    public boolean parallelRecursive(State state, Graph org, Graph orgb, ArrayList<Integer> org_iso_list, ArrayList<Integer> orgb_iso_list) {
        //子任务递归

        if (isKeep()) {
            if (state.depth >= 0.2 * (orgb.nodes.size() - orgb.iso_list.size())) {
                state.matched = true; // found a match
                return true;
            } else {
                ArrayList<Pair<Integer, Integer>> candidatePairs = vf2.genCandidatePairs(state, org, orgb, org_iso_list, orgb_iso_list);

                // System.out.println(" check " + orgb.name + state.depth + " " + candidatePairs.size());
                for (Pair<Integer, Integer> entry : candidatePairs) {

                    // extend the state
                    state.extendMatch(entry.getKey(), entry.getValue());

                    if (parallelRecursive(state, org, orgb, org_iso_list, orgb_iso_list)) {
                        return true;
                    }
                    // remove the match added before
                    state.backtrack(entry.getKey(), entry.getValue());
                }
            }
        }
        return false;
    }


}

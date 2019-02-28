package com.detection.Algorithm.detectAlgorithm.parallelDetection;

import com.detection.Algorithm.GraphElement.Graph;
import com.detection.Algorithm.GraphElement.State;
import com.detection.Algorithm.detectAlgorithm.vf2;
import com.fourinone.MigrantWorker;
import com.fourinone.WareHouse;
import javafx.util.Pair;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/*
    该类为最底层的处理匹配的工人类
 */
public class DetectWorker extends MigrantWorker {
    private State state;
    private Graph org;
    private Graph orgb;
    private List<Pair<Integer, Integer>> candidatePairs;
    private String worker_name;
    private parallelRecursiveTask parallelRecursiveTask;

    public DetectWorker(){}
    public DetectWorker(ArrayList<Pair<Integer, Integer>> candidatePair, State state, Graph org, Graph orgb, PrintWriter log_writer, String worker_name) {

        this.state = state;
        this.org = org;
        this.orgb = orgb;
        candidatePairs = candidatePair;
        worker_name = worker_name;
    }


    private void read_Task(WareHouse wh) {
        MyTask task = (MyTask) wh.getObj("task");
        org = task.getOrg();
        orgb = task.getOrgb();
        parallelRecursiveTask = task.getParallelRecursiveTask();
        state = task.getCurrent_state();
        candidatePairs = task.getCandidatePairs();
        worker_name = task.taskname;
    }


    /*
        每个最低层工人所做的 候选集合的匹配
     */
    public WareHouse doTask(WareHouse wh) {

        read_Task(wh);

        if (state.depth == orgb.nodes.size()) {
            state.matched = true;// found a match
            return new WareHouse("result", "succ");
        } else {
            for (Pair<Integer, Integer> entry : candidatePairs) {
                if (parallelRecursiveTask.isKeep()) {
                    if (vf2.checkFeasibility(state, entry.getKey(), entry.getValue())) {
                        // extend the state
                        state.extendMatch(entry.getKey(), entry.getValue());
                        if (parallelRecursiveTask.parallelRecursive(state, org, orgb, org.iso_list, orgb.iso_list)) {
                            // 子任务并行
                            // System.out.println(worker_name + " found!");
                            return new WareHouse("result", "succ");
                        } else if (!parallelRecursiveTask.isKeep()) {
                            return new WareHouse("result", "nokeep");
                        } else {
                            state.backtrack(entry.getKey(), entry.getValue());
                        }
                    }
                } else {
                    // System.out.println(worker_name + " failed!!");
                    return new WareHouse("result", "nokeep");
                }
            }
            // System.out.println(worker_name + " is done.");
            return new WareHouse("result", "fail");
        }
    }
}

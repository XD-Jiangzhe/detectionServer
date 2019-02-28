package com.detection.Algorithm.detectAlgorithm.parallelDetection;

import com.detection.Algorithm.GraphElement.Graph;
import com.detection.Algorithm.GraphElement.State;
import javafx.util.Pair;

import java.util.List;

//将跑最小的单位的任务进行封装
public class MyTask
{
    private List<Pair<Integer, Integer>> candidatePairs;
    public Graph org;
    public Graph orgb;
    String taskname;
    private State current_state;
    private parallelRecursiveTask parallelRecursiveTask;

    public MyTask(List<Pair<Integer, Integer>> candidatePairs,
                  Graph org, Graph orgb,
                  String taskname, State current_state,
                  parallelRecursiveTask parallelRecursiveTask) {

        this.candidatePairs = candidatePairs;
        this.org = org;
        this.orgb = orgb;
        this.taskname = taskname;
        this.current_state = current_state;
        this.parallelRecursiveTask = parallelRecursiveTask;
    }

    public List<Pair<Integer, Integer>> getCandidatePairs() {
        return candidatePairs;
    }

    public void setCandidatePairs(List<Pair<Integer, Integer>> candidatePairs) {
        this.candidatePairs = candidatePairs;
    }

    public Graph getOrg() {
        return org;
    }

    public void setOrg(Graph org) {
        this.org = org;
    }

    public Graph getOrgb() {
        return orgb;
    }

    public void setOrgb(Graph orgb) {
        this.orgb = orgb;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public State getCurrent_state() {
        return current_state;
    }

    public void setCurrent_state(State current_state) {
        this.current_state = current_state;
    }

    public com.detection.Algorithm.detectAlgorithm.parallelDetection.parallelRecursiveTask getParallelRecursiveTask() {
        return parallelRecursiveTask;
    }

    public void setParallelRecursiveTask(com.detection.Algorithm.detectAlgorithm.parallelDetection.parallelRecursiveTask parallelRecursiveTask) {
        this.parallelRecursiveTask = parallelRecursiveTask;
    }
}

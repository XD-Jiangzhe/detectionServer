package com.detection.Algorithm.detectAlgorithm.parallelDetection;

import com.detection.Algorithm.GraphElement.Graph;
import com.detection.Algorithm.GraphElement.State;
import com.fourinone.MigrantWorker;
import com.fourinone.WareHouse;


public class FamilyWorker extends MigrantWorker {

    private static final long serialVersionUID = 591128300737335680L;
    public Graph org;
    public Graph orgb;
    private String detective_name;


    FamilyWorker(String name, Graph org)
    {
        this.detective_name = name;
        this.org = org;
    }

    @Override
    protected WareHouse doTask(WareHouse wareHouse) {

        boolean result_flag= false;

        orgb = (Graph) wareHouse.getObj("task");
        result_flag = match_parallel_vf2(org, orgb, detective_name);
        if(result_flag)
        {
            return new WareHouse("result", orgb.name);
        }
        else {
            return new WareHouse("result", "");
        }
    }


    private boolean match_parallel_vf2(Graph org, Graph orgb, String detective_name) {
        State state = new State(org, orgb);
        detectorContractor detectorContractor = new detectorContractor();
        //构建一个匹配的工头，用来分发给最低层的工人，他们需要进行的候选集匹配
        //该类不仅是一个工人，也是一个工头，用来向底层的detecWorker 分发 匹配候选的任务

        if (org.nodes.size() < orgb.nodes.size() || org.edges.size() < orgb.edges.size()) {
            return false;
        }

        detectorContractor.setState(state);
        detectorContractor.set_graph(org, orgb);
        detectorContractor.setDetective_name(detective_name);

        if ((boolean) detectorContractor.giveTask(null).getObj("result")) {
            state.matched = true;
        }
        // detect_dctor.exit();
        return state.matched;
    }
}

package com.detection.Algorithm.detectAlgorithm.parallelDetection;

import com.detection.Algorithm.GraphElement.Graph;
import com.detection.Algorithm.GraphElement.State;
import com.detection.Algorithm.detectAlgorithm.vf2;
import com.fourinone.Contractor;
import com.fourinone.WareHouse;
import com.fourinone.WorkerLocal;
import javafx.util.Pair;

import java.util.ArrayList;

/*
    继续向下分发任务,即每个 org图 匹配的承包人，指定指定个工作线程，然后向其中添加检测的任务
 */
public class detectorContractor extends Contractor {

    private State state;
    private Graph org;
    private Graph orgb;
    private String detective_name;


    @Override
    public WareHouse giveTask(WareHouse wareHouse) {

        int worker_num = 4; //设置工人数目
        ArrayList<Pair<Integer, Integer>> candidatePairs =
                vf2.genCandidatePairs(state, org, orgb,
                        org.iso_list, orgb.iso_list);
        ArrayList<parallelRecursiveTask> recursiveTasks = new ArrayList<>();
        for (int i = 0; i < worker_num; i++) {
            recursiveTasks.add(new parallelRecursiveTask(true));
        }

        int splitNum = candidatePairs.size()/worker_num;
        WareHouse[] tasks = new WareHouse[worker_num];  //存放每个工人要完成的任务的队列
        WareHouse[] results = new WareHouse[worker_num];


        for (int i = 0; i < worker_num; i++) {
            try {
                if(i < worker_num-1)
                {
                    tasks[i] = new WareHouse("task", new MyTask(
                            candidatePairs.subList(i*splitNum, (i+1)*splitNum),
                            org, orgb, "worker"+i,
                            (State) state.deepCopy(), recursiveTasks.get(i)));
                }
                else{
                    tasks[i] = new WareHouse("task", new MyTask(
                            candidatePairs.subList(i*splitNum, candidatePairs.size()),
                            org, orgb, "worker"+i,
                            (State) state.deepCopy(), recursiveTasks.get(i)));
                }

            }catch (Throwable e)
            {
                System.out.println(e.getMessage());
            }
        }

        WorkerLocal[] wks = getLocalWorkers(worker_num);
        for (int j = 0; j < worker_num; j++) {
            wks[j].setWorker(new DetectWorker());
        }
        //启动任务
        for (int j = 0; j < wks.length; j++) {
            results[j] = wks[j].doTask(tasks[j]);
        }

        String flag = "";
        for (int j = 0; j < worker_num; ) {
            for (int i = 0; i < worker_num; i++) {
                if (results[i] != null && results[i].getStatus() == WareHouse.READY) {
                    if (results[i].getObj("result").equals("succ")) {
                        for (int k = 0; k < worker_num; k++) {
                            if (k != i) {
                                recursiveTasks.get(k).setKeep(false);
                                flag = "succ";
                                break;
                            }
                        }
                        if (flag.equals("succ")) {
                            break;
                        }
                    }
                    results[i] = null;
                    j++;
                }
            }
            if (flag.equals("succ")) {
                break;
            }
        }

        if (flag.equals("succ")) {
            return new WareHouse("result", true);
        } else {
            return new WareHouse("result", false);
        }
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getDetective_name() {
        return detective_name;
    }

    public void setDetective_name(String detective_name) {
        this.detective_name = detective_name;
    }

    public void set_graph(Graph org, Graph orgb) {

        this.org = org;
        this.orgb = orgb;
    }
}

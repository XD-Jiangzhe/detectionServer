package com.detection.Algorithm.detectAlgorithm.parallelDetection;

import com.detection.Algorithm.GraphElement.Graph;
import com.fourinone.Contractor;
import com.fourinone.WareHouse;
import com.fourinone.WorkerLocal;

import java.util.ArrayList;

public class familyContractor extends Contractor {

    private ArrayList<Graph> orgb_list = new ArrayList<>();
    public Graph org;


    @Override
    public WareHouse giveTask(WareHouse wareHouse) {

        WareHouse[] tasks = new WareHouse[orgb_list.size()];//任务列表
        ArrayList<String>result_list = new ArrayList<>();

        Graph task_orgb;
        int workerNum = 8;

        WorkerLocal[] wks = getLocalWorkers(workerNum);    // 8 个本地工人线程
        for (int i = 0; i < wks.length; i++) {
            wks[i].setWorker(new FamilyWorker("Detective" + i, org));   //创建8 个工人
        }

        for (int i = 0; i < orgb_list.size(); i++) {
            task_orgb = orgb_list.get(i);
            tasks[i] = new WareHouse("task", task_orgb);
        }

        WareHouse[] result = doTaskCompete(wks, tasks); //分配tasks 给 工人列表

        for (WareHouse aResult : result) {
            if (!aResult.getObj("result").equals("")) {
                result_list.add((String) aResult.getObj("result"));
            }
        }

        return new WareHouse("malicious list", result_list);
    }


    public ArrayList<Graph> getOrgb_list() {
        return orgb_list;
    }

    public void setOrgb_list(ArrayList<Graph> orgb_list) {
        this.orgb_list = orgb_list;
    }

    public Graph getOrg() {
        return org;
    }

    public void setOrg(Graph org) {
        this.org = org;
    }
}

package com.detection.Algorithm.detectAlgorithm;

import com.detection.Algorithm.GraphElement.Graph;
import com.detection.Algorithm.detectAlgorithm.parallelDetection.familyContractor;

import java.util.ArrayList;

import static com.detection.Algorithm.Util.buildGraphFromTransfer.DatatransferFile2Graph;
import static com.detection.Algorithm.Util.buildGraphFromTransfer.DatatransferString2Graph;
import static com.detection.Algorithm.Util.generateORGBList.loadGraphFromSpeMatrix;
import static com.detection.Algorithm.detectAlgorithm.vf2.vf2MatchSerializable;

public class startParallelDetection {

    /**
     * 整个并行匹配的接口，输入是 org 和 orgb 的list
     * 输出 是可能的所有的种类
     * @param transferText
     * @param orgb_list
     */
    public static ArrayList<String> IstartParallelDetection(String transferText, ArrayList<Graph> orgb_list)
    {
        Graph org = DatatransferString2Graph(transferText);
        ArrayList<String> possible_family_list = IParallelDetection(org, orgb_list);

        return possible_family_list;
    }


    public static ArrayList<String> IParallelDetection(Graph org, ArrayList<Graph> orgb_list){

        familyContractor familyContractor = new familyContractor();
        //整个任务的工头

        ArrayList<String> possible_family_list = new ArrayList<>();
        familyContractor.setOrgb_list(orgb_list);
        familyContractor.setOrg(org);
        possible_family_list =
                (ArrayList<String>)familyContractor.giveTask(null).getObj("malicious list");
        if(possible_family_list.isEmpty())
        {
            System.out.println("app is benign");
        }
        else
        {
            System.out.println("app may belong to the following "+ possible_family_list.size()
                    + " families :\n"
                    + possible_family_list
            );
        }
        return possible_family_list;
    }


    public static void main(String[] args) {

        ArrayList<Graph> orgb_list = new ArrayList<>();
        ArrayList<String> possible_family_list = new ArrayList<>();

        Graph org = DatatransferFile2Graph("./Test/raw_transfer/App1.txt");
        orgb_list = loadGraphFromSpeMatrix("./orgbFils");

        familyContractor familyContractor = new familyContractor();

        familyContractor.setOrgb_list(orgb_list);
        familyContractor.setOrg(org);
        possible_family_list =
                (ArrayList<String>)familyContractor.giveTask(null).getObj("malicious list");
        if(possible_family_list.isEmpty())
        {
            System.out.println("app is benign");
        }
        else
        {
            System.out.println("app may belong to the following "+ possible_family_list.size()
                    + " families :\n"
                    + possible_family_list
            );
        }

    }

}

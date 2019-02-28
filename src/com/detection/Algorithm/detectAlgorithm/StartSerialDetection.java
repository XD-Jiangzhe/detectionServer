package com.detection.Algorithm.detectAlgorithm;


import com.detection.Algorithm.GraphElement.Graph;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.detection.Algorithm.Util.buildGraphFromTransfer.DatatransferFile2Graph;
import static com.detection.Algorithm.Util.generateORGBList.loadGraphFromSpeMatrix;
import static com.detection.Algorithm.detectAlgorithm.vf2.vf2MatchSerializable;

public class StartSerialDetection {

    private static final Logger LOGGER = Logger.getLogger(StartSerialDetection.class.getName());

    /**
     * 开始先写串行的算法
     * @param args
     */
    public static void main(String[] args) {

        ArrayList<Graph> orgb_list = new ArrayList<>();
        ArrayList<String> possible_family_list = new ArrayList<>();

        Graph graph = DatatransferFile2Graph("./Test/raw_transfer/App1.txt");
        orgb_list = loadGraphFromSpeMatrix("./orgbFils");

        for(Graph orgb : orgb_list)
        {
            boolean result = vf2MatchSerializable(orgb, graph);
            possible_family_list.add(orgb.name);
        }

        if(possible_family_list.isEmpty())
        {
            LOGGER.log(Level.ALL, "app is benign" );
        }
        else
        {
            LOGGER.log(Level.ALL, "app may belong to the following " + possible_family_list.size()
                + "families : " + possible_family_list);
        }

    }
}

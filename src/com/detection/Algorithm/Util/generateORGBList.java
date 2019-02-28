package com.detection.Algorithm.Util;

import com.detection.Algorithm.GraphElement.Edge;
import com.detection.Algorithm.GraphElement.Graph;
import com.detection.Algorithm.GraphElement.Node;

import java.awt.font.GraphicAttribute;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class generateORGBList {

    private static final Logger LOGGER = Logger.getLogger(generateORGBList.class.getName());

    public static ArrayList<Graph> loadGraphFromSpeMatrix(String dirPath)
    {
        ArrayList<Graph> graphs = new ArrayList<>();

        try {
            File orgFilePath = new File(dirPath);
            if (orgFilePath.isDirectory()) {
                for (String filename : orgFilePath.list())
                {
                    Path path = Paths.get(dirPath, filename);
                    read_spematrix(graphs, path.toString());
                }
            }
        }
        catch (Throwable e){
            LOGGER.log(Level.FINE, "has en error {0}", e.getMessage());
        }
        return graphs;
    }


    private static void read_spematrix(ArrayList<Graph> graphSet, String fileObsolutePath) throws FileNotFoundException {
        /** Writen by  : Huanran Wang
         *  Created on : 2018.7.20 at 19:56.
         *  Description: In read_spematrix we will read special matrix from files. */
        /*将文件转成矩阵*/
        Graph graph = null;
        Scanner scanner = new Scanner(Paths.get(fileObsolutePath).toFile());

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.equals("")) {
            } else if (line.startsWith("t")) {
                if (graph != null) {
                    graph.setIso_list(get_isolated_nodes(graph));
                    initdegree(graph);
                    graphSet.add(graph);
                }
                if (!line.split(" ")[2].equals("-1")) {
                    graph = new Graph(line.split(" ")[3], line.split(" ")[4]);
                }
            } else if (line.startsWith("v")) {

                String[] lineSplit = line.split(" ");
                String nodeLabel = lineSplit[2];
                String nodeName = lineSplit[3];
                int nodeId = Integer.parseInt(lineSplit[1]);
                graph.addNode(nodeId, Integer.parseInt(nodeLabel), nodeName);
            } else if (line.startsWith("e")) {
                String[] lineSplit = line.split(" ");
                int sourceId = Integer.parseInt(lineSplit[1]);
                int targetId = Integer.parseInt(lineSplit[2]);
                int edgeLabel = Integer.parseInt(lineSplit[3]);

                graph.addEdge(sourceId, targetId, edgeLabel);
                graph.nodes.get(sourceId).outdegree++;
                graph.nodes.get(targetId).indegree++;
                if (graph.nodes.get(targetId).indegree > graph.nodes.get(sourceId).max_succ_in) {
                    graph.nodes.get(sourceId).max_succ_in = graph.nodes.get(targetId).indegree;
                }
                if (graph.nodes.get(targetId).outdegree > graph.nodes.get(sourceId).max_succ_out) {
                    graph.nodes.get(sourceId).max_succ_out = graph.nodes.get(targetId).outdegree;
                }
                if (graph.nodes.get(sourceId).indegree > graph.nodes.get(targetId).max_pred_in) {
                    graph.nodes.get(targetId).max_pred_in = graph.nodes.get(sourceId).indegree;
                }
                if (graph.nodes.get(sourceId).outdegree > graph.nodes.get(targetId).max_pred_out) {
                    graph.nodes.get(targetId).max_pred_out = graph.nodes.get(sourceId).outdegree;
                }
            }
        }

        scanner.close();
    }


    private static void initdegree(Graph graph) {
        /** Writen by  : Huanran Wang
         *  Created on : 2018.7.20 at 19:56.
         *  Description: In initdegree we will init the sum degree for every node. */

        for (Node node : graph.nodes) {

            for (Edge inedge : node.inEdges) {
                node.sum_pred_in += inedge.source.indegree;
                node.sum_pred_out += inedge.source.outdegree;
            }

            for (Edge outedge : node.outEdges) {
                node.sum_pred_in += outedge.source.indegree;
                node.sum_pred_out += outedge.source.outdegree;
            }
        }
    }

    public static ArrayList<Integer> get_isolated_nodes(Graph graph) {

        ArrayList<Integer> isolated_nodes = new ArrayList<>();
        int[][] matrix = graph.getAdjacencyMatrix();
        int length = graph.nodes.size();
        int k, l;
        for (int i = 0; i < length; i++) {
            k = 0;
            for (int j = 0; j < length; j++) {
                if (matrix[i][j] == 0) {
                    k = 1;
                    break;
                }
            }
            if (k == 0) {
                isolated_nodes.add(i);
            }
        }
        for (int j = 0; j < length; j++) {
            l = 0;
            for (int i = 0; i < length; i++) {
                if (matrix[i][j] == 0) {
                    l = 1;
                    break;
                }
            }
            if (l == 1) {
                if (isolated_nodes.contains(j)) {
                    isolated_nodes.remove(isolated_nodes.indexOf(j));
                }
            }
        }
        return isolated_nodes;
    }
}

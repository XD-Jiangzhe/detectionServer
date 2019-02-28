package com.detection.Algorithm.Util;

import com.detection.Algorithm.GraphElement.Graph;
import com.sun.corba.se.impl.protocol.giopmsgheaders.IORAddressingInfo;
import com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import sun.rmi.runtime.Log;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class buildGraphFromTransfer {


    private static final int STATUS_BEGIN = 0;
    private static final int STATUS_FOLLOWED = 1;
    private static final int STATUS_FOLLOWING = 2;


    public static void main(String[] args)
    {
        Graph graph = DatatransferFile2Graph("./Test/raw_transfer/App1.txt");

    }



    /**
     * 在hashmap 中添加类名和编号，并且返回下一个编号
     * @param name2index
     * @param name
     * @param index
     * @return
     */
    public static int addIfNotContain(HashMap<String, Integer> name2index, String name, int index)
    {
        if(!name2index.containsKey(name))
        {
            name2index.put(name, index);
            return index+1;
        }
        return index;

    }


    public static Graph DatatransferFile2Graph(String filepath)
    {
        try {
            File DataTransfer = new File(filepath);
            BufferedReader reader = new BufferedReader(new FileReader(DataTransfer));
            ArrayList<String> classNames = new ArrayList<>();
            String graphRawInfo = reader.readLine();

            return DatatransferString2Graph(graphRawInfo);
        }catch (Throwable e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }


    /**
     * transfer file 转 graph
     * @param graphRawInfo
     * @return
     */
    public static Graph DatatransferString2Graph(String graphRawInfo)
    {
        try {
            //存放名字 和 id
            HashMap<String, Integer> classNameMap = new HashMap<>();
            short[][] matrix = String2Matrix(graphRawInfo, classNameMap);

            for (short[] aMatrix : matrix) {
                for (int j = 0; j < matrix.length; j++) {
                    System.out.print(aMatrix[j] + " ");
                }
                System.out.println();
            }

            System.out.println(classNameMap.size());
            return matrixToGraph(matrix, classNameMap);
        }catch (Throwable e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }


    /**
     * 从string中构建出矩阵
     */
    public static short[][] String2Matrix(String  fileContext, HashMap<String, Integer> classNameMap)
    {
        String[] graph_info_list = fileContext.split("#");
//        PrintWriter writer = new PrintWriter("./Test/raw_transfer/test.txt");

        String className = null;
        String attachName = null;//被依赖的类的名字
        String refClassName = null;//依赖的类的名字

        int classIndex = 0;//每个类的编号
        int status = STATUS_BEGIN;


        short[][] matrixTemp = new short[1000][1000];

        for (int i = 0; i < graph_info_list.length; i++)
        {
           String lineContent = graph_info_list[i];
           switch (status)
           {
               case STATUS_BEGIN:
                   //在一开始的时候
                   if(lineContent.startsWith("class"))
                   {
                       className = lineContent.split(" ", 2)[1].split("\\$")[0];
                       classIndex = addIfNotContain(classNameMap, className, classIndex);
                        ++i;//class的下一层都是地址
                   }
                   else if(lineContent.equals("Referees by Type"))
                   {
                       // Referees by type A means A point to the given class
                       status = STATUS_FOLLOWED;
                   }
                   else if(lineContent.equals("Referrers by Type"))
                   {
                       // Referrers by type A means point to A
                       status = STATUS_FOLLOWING;
                   }
                   break;
               case STATUS_FOLLOWED:
                   if (lineContent.startsWith("class ") || lineContent.equals("Package"))
                   {
                       i--;
                       status = STATUS_BEGIN;
                   } else if (lineContent.equals("Referrers by Type"))
                   //classname 依赖的类
                   {
                       status = STATUS_FOLLOWING;
                   } else
                   {
                       attachName = lineContent.split("\\$")[0];
                       classIndex = addIfNotContain(classNameMap, attachName, classIndex);
                       ++i;
                       refClassName = lineContent;
                       //矩阵上为1
                       matrixTemp[classNameMap.get(attachName)][classNameMap.get(className)] =1;
                   }
                   break;

               case STATUS_FOLLOWING:
                   if (lineContent.startsWith("class ") || lineContent.equals("Package")) {
                       i--;
                       status = STATUS_BEGIN;
                   }else if (lineContent.equals("Referees by Type"))
                   {
                       status = STATUS_FOLLOWED;
                   } else {
                       attachName = lineContent.split("\\$")[0];
                       classIndex = addIfNotContain(classNameMap, attachName, classIndex);

                       i++;
                       refClassName = lineContent;
                       matrixTemp[classNameMap.get(className)][classNameMap.get(attachName)] = 1;
                   }
                   break;
               default:
                   break;
           }

//           writer.println(lineContent);

        }
        short[][] matrix = new short[classNameMap.size()][classNameMap.size()];
        for (int i = 0; i < classNameMap.size(); i++)
        {
            System.arraycopy(matrixTemp[i], 0, matrix[i],0 , classNameMap.size());
        }

        return matrix;

    }

    public static Graph matrixToGraph(short[][] matrix, HashMap<String, Integer> hashMap)
    {
        Graph graph = new Graph("test", "null");
        for(String key : hashMap.keySet())
        {
            graph.addNode(hashMap.get(key), 0, key);
        }

        for(int i=0; i<hashMap.size(); ++i)
        {
            for(int j=0; j<hashMap.size(); ++j)
            {
                if(matrix[i][j] == 1)
                {
                    int sourceId = i;
                    int targetId = j;
                    graph.addEdge(i, j, 0);
                    graph.nodes.get(sourceId).outdegree++;
                    graph.nodes.get(targetId).indegree++;

                    //更新每个节点的后继和前驱节点的最大出入度
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
        }

        return graph;
    }


    public  static void matrix2SpercialMatrix(short[][] matrix, String filepath, String app_name, ArrayList<String> name_list) throws IOException {

        try {

            File special_matrix_file = new File(filepath);
            FileWriter fw = new FileWriter(special_matrix_file, true);
            BufferedWriter output = new BufferedWriter(fw);
            if (!special_matrix_file.exists()) {
                special_matrix_file.createNewFile();
            }
            output.write("t # " + 0 + " " + app_name.replaceAll(".txt", "") + " " + "unknown" + "\n");
            for (int i = 0; i < name_list.size(); i++) {
                output.write("v " + i + " 0 " + name_list.get(i) + "\n");
            }
            for (int i = 0; i < name_list.size(); i++) {
                for (int j = 0; j < name_list.size(); j++) {
                    if (matrix[i][j] == 1) {
                        output.write("e " + i + " " + j + " 0\n");
                    }
                }
            }
            output.close();
            fw.close();
        }catch (Throwable e)
        {
            System.out.println(e.getMessage());
        }
    }

}

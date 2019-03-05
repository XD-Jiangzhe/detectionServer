package com.detection.Algorithm.Util;

import com.detection.Algorithm.GraphElement.Graph;
import com.detection.UtilLs.MyLogging;

import java.io.*;
import java.util.ArrayList;

import static com.detection.Algorithm.Util.buildGraphFromTransfer.DatatransferFile2Graph;

public class ReadOrgsFromLocal {

    public static void main(String[] args) {

        ArrayList<Graph> orgs = readOrgSFromLocalDir("./orgb");
        MyLogging.logger.info(orgs.size());

    }

    public static ArrayList<Graph> readOrgSFromLocalDir(String dirpath)
    {
        File orgsDir = new File( dirpath);
        ArrayList<Graph> orgs = new ArrayList<>();

        if(orgsDir.isDirectory())
        {
            File[] files = orgsDir.listFiles();
            for(File file : files)
            {
                //读取每个file，这里每个file是一个matrix
                try{
                    Graph graph = DatatransferFile2Graph(file.getAbsolutePath());
                    orgs.add(graph);
                }catch (Throwable e)
                {
                    MyLogging.logger.error(e.getMessage());
                }

            }
        }
        return orgs;
    }


    public static short[][] readMatrixFromFile(File file) throws FileNotFoundException ,IOException
    {
        InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
        BufferedReader br = new BufferedReader(reader);
        String line = "";
        ArrayList<String> filecontext = new ArrayList<>();

        while(line != null)
        {
            line = br.readLine();
            filecontext.add(line);
        }
        return readMatrixFromStringList(filecontext);
    }

    public static short[][] readMatrixFromStringList(ArrayList<String> context)
    {
        int height = context.size();
        int length = height>0? context.get(0).length() : 0;
        short[][] matrix = new short[height][length];

        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < length ; j++)
            {
                matrix[i][j] = Short.valueOf(String.valueOf(context.get(i).charAt(j)));
            }
        }
        return matrix;
    }



}

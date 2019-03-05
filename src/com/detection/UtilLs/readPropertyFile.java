package com.detection.UtilLs;

import com.sun.imageio.plugins.common.InputStreamAdapter;
import com.sun.javafx.image.IntPixelGetter;
import javafx.util.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Properties;

public class readPropertyFile {

    public static  Properties propertiesFactory(String filename)
    {
        try {
            Properties properties = new Properties();
            InputStreamReader ir = new InputStreamReader(new FileInputStream(new File(filename)));
            properties.load(ir);
            return properties;
        }catch (Throwable e)
        {
            return null;
        }
    }

    public static HashMap<String, String> propertiesFile2HashMap(String filename)
    {
        HashMap<String ,String> result = new HashMap<>();
        Properties properties = propertiesFactory(filename);

        if(properties != null)
        {
            for (String key : properties.stringPropertyNames())
            {
                String value = properties.getProperty(key);
                result.put(key, value);
            }
        }
        return result;
    }

    public static HashMap<String, String> malware2Name = propertiesFile2HashMap("./malware.properties");

    public static void main(String[] args) {
        Properties properties = propertiesFactory("./malware.properties");
        for(String key: malware2Name.keySet())
        {
            String value = malware2Name.get(key);
            System.out.println(key +" " + value);
        }
    }

}

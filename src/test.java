import javafx.util.Pair;

import javax.swing.text.html.HTMLDocument;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class test {

    public static int addHashMap(HashMap<String ,Integer> m, String s, int t)
    {
        m.put(s, t);
        return t+1;
    }

    public static void main(String[] args) {

        int index = 0;
        HashMap<String, Integer> m = new HashMap<>();
        for (int i = 0; i <10; i++) {
            index = addHashMap(m, String.valueOf(i), index);
        }

        Iterator<Map.Entry<String, Integer>> iterator = m.entrySet().iterator();
        while(iterator.hasNext())
        {
            Map.Entry<String, Integer> entry = iterator.next();
            System.out.println(entry.getKey() + " " +  entry.getValue());
        }
    }
}

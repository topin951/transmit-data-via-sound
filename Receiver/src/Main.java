import topin.CharMap;
import topin.Coder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {



    public static void main(String[] args) {


        /*for(int i; i <CharMap.map.size(); i++) {
            Map.Entry<Integer, String> entry = CharMap.map.entrySet().iterator().next();
            int key = entry.getKey();
            String value = entry.getValue();
        }*/

        /*String bits = "00011111010101111001";

        List<String> list = getParts(bits, 4);
        for(int i = 0; i<list.size(); i++)
        {
            String part = list.get(i);
        }*/

        Coder coder = new Coder();
        coder.start();

    }

   /* private static List<String> getParts(String string, int partitionSize) {
        List<String> parts = new ArrayList<>();
        int len = string.length();
        for (int i=0; i<len; i+=partitionSize)
        {
            parts.add(string.substring(i, Math.min(len, i + partitionSize)));
        }
        return parts;
    }*/

}

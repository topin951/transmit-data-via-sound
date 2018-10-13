import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


class Main {

    public static void main(String[] args) {

        //StartSound Main
        try {
            startSound(4175, 1.5);
            //sleep(500);
            startSound(3100, 0.3);
            String encodeString = fileRead();
            double key[] = getKeys(encodeString);

            double duration;
            for (int i = 0; i < key.length; i++) {
                System.out.println(key[i] + " = " + CharMap.map.get((int)key[i]));
                duration = 0.16;
                startSound(key[i], duration);
                startSound(3100, 0.21);
               // sleep(5);
            }
            startSound(4200, 2);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static List<String> getParts(String string, int partitionSize) {
        List<String> parts = new ArrayList<>();
        int len = string.length();
        for (int i=0; i<len; i+=partitionSize)
        {
            parts.add(string.substring(i, Math.min(len, i + partitionSize)));
        }
        return parts;
    }

    public static double[] getKeys(String str) {
        List<String> parts = new ArrayList<>();
        parts = getParts(str, 4);
        double[] keys = new double[str.length()/4];
        for (int i = 0; i < parts.size(); i++) {
            char e = str.charAt(i);
            //String x = Character.toString(e);
            keys[i] = getKey(CharMap.map,  parts.get(i));
        }
        return keys;
    }

    public static <T, E> Set<T> getKeysByValue(Map<T, E> map, E value) {
        return map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    public static <K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static double[] tone(double hz, double duration) {
        int n = (int) (StdAudio.SAMPLE_RATE * duration);
        double[] a = new double[n + 1];
        for (int i = 0; i <= n; i++) {
            a[i] = Math.sin(2 * Math.PI * i * hz / StdAudio.SAMPLE_RATE);
        }
        return a;
    }

    public static void startSound(double key, double duration) {
        double[] a = tone(key, duration);
        StdAudio.play(a);
    }

    public static void sleep(double time) {
        try {
            TimeUnit.MILLISECONDS.sleep((long) time);
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public static String fileRead() {
        try {
            Path path = Paths.get("r4_to");
            byte[] fileContents =  Files.readAllBytes(path);
            System.out.println(fileContents);

            String bits = "";
            for(int i = 0;i<fileContents.length;i++)
            {
                String s1 = String.format("%8s", Integer.toBinaryString(fileContents[i] & 0xFF)).replace(' ', '0');
                //System.out.println(s1);
                bits += s1;
            }
            //System.out.println(bits);
            return bits;
        }
        catch(Exception e) {
            e.getStackTrace();
        }
        return null;
    }
}
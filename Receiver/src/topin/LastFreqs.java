package topin;

import java.util.ArrayList;

public class LastFreqs {

    public static int[] freqs = new int[10];

    private static int current = 0;

    public static void add(Integer integer)
    {
        if(current == 10)
            current = 0;

        freqs[current] = integer;

        current++;
    }

    public static int last()
    {
        return freqs[0];
    }
}

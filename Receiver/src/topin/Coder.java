package topin;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Base64;

public class Coder {

    public static final int HOLD = 5;

    boolean started = false;

    private int currentFrqency;



    private boolean isStartFreqency()
    {
        return checkFreq(4175);
    }

    private boolean isEndFreqency()
    {
        return checkFreq(4200);
    }

    private boolean checkFreq(int freq)
    {
        int exHold = HOLD+1;
        return (currentFrqency >= freq-exHold && currentFrqency <= freq+exHold);
    }

    public final void start()
    {


        AudioInput ai= new AudioInput();
        int lastFreq = -1;

        boolean ended = false;


        String all = "";

        int nothing = 0;
        int syn = 0;

        while(true){
            ai.readPcm();
            ai.byteToDouble();
            int freq = (int) ai.findFrequency();
            currentFrqency = freq;
            //if(ended)
            if(currentFrqency == 0)
                continue;;
            //System.out.println("Found: " + currentFrqency + " Hz");

            // System.out.println(nothing);
            if(nothing > 50 && started) {
                //System.out.println("\nend");
                //started = false;
            }

            int last = LastFreqs.last();
            String lastChar = null;
            LastFreqs.add(currentFrqency);

            /*if(currentFrqency == 4175)
            {
                syn++;
                continue;
            }*/

            if(isStartFreqency() && !started) {
                System.out.println("start");
                started = true;
            }

            if(isEndFreqency() && started) {
                System.out.println("\nend");
                started = false;

                //System.out.println(all);
                analyze(all);

                System.exit(-1);
            }

            if(!started)
                continue;

            String s = CharMap.map.get(freq);
            //String s = CharMap.find(freq);

            if(s == null)
            {
                nothing++;
            }

            /*if(lastChar == "-" && s == "-")
            {
                continue;
            }*/


            //if(lastChar == null)
            //    System.out.print(" ");

            lastChar = s;

            //if(LastFreqs.last())

            if(s != null)
                all = all+s;


            if(s != null)
                System.out.print(s);

        }
    }

    private void analyze(String all)
    {
        char last = '-';
        char current = '-';
        String output = "";
        int groupSize = 0;
        for(int i = 0; i < all.length(); i++)
        {
            if(i > 0)
                last = current;

            if(groupSize > 11)
            {
                output += current;
                groupSize = 0;
            }

            current = all.charAt(i);

            if(last != current) {
                if(groupSize > 1 || i == 0) {
                    output += current;
                    groupSize = 0;
                }
            } else {
                groupSize++;
            }

        }




        String ex = "";


        last = '-';
        current = '-';
        boolean started = false;
        for(int i = 0; i < all.length(); i++)
        {
            if(i > 0)
                last = current;

            if(!started && all.charAt(i) == '-')
            {
                continue;
            } else
                started = true;


            current = all.charAt(i);

            if(i == 0)
            {
                /*ex += all.charAt(0);
                ex += all.charAt(1);
                ex += all.charAt(2);
                ex += all.charAt(3);*/
            } else {

                if ((current != '-' && last == '-') && (all.charAt(i-2) == '-')) {
                    ex += all.charAt(i);
                    ex += all.charAt(i + 1);
                    ex += all.charAt(i + 2);
                    ex += all.charAt(i + 3);
                }
            }

/*
            if((current == '-' && last != '-') || (i == all.length()-1 && last != '-')) {
                ex += last1;
                ex += last2;
                ex += last3;
                ex += last4;
            }*/
        }
        System.out.println(ex);


        try {
            DataOutputStream os = new DataOutputStream(new FileOutputStream("inout.dat"));
            byte[] bval = new BigInteger(ex, 2).toByteArray();
            os.write(bval);
            os.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

       /* try {
            byte[] asBytes = Base64.getDecoder().decode(output);
            System.out.println(new String(asBytes, "utf-8")); // And the output is: some string
        } catch (Exception e)
        {
            e.printStackTrace();
        }*/

    }

}

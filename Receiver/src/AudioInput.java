import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import topin.CharMap;

public class AudioInput {

    TargetDataLine  microphone;

    final int       audioFrames= 8192;  //power ^ 2

    final float     sampleRate= 8*1024;
    final int       bitsPerRecord= 16;
    final int       channels= 1;
    final boolean   bigEndian = true;
    final boolean   signed= true;

    byte            byteData[];     // length=audioFrames * 2
    double          doubleData[];   // length=audioFrames only reals needed for apache lib.
    AudioFormat     format;
    FastFourierTransformer transformer;

    public AudioInput () {

        byteData= new byte[audioFrames /16];  //two bytes per audio frame, 16 bits

        //doubleData= new double[audioFrames * 2];  // real & imaginary
        doubleData= new double[audioFrames];  // only real for apache

        transformer = new FastFourierTransformer(DftNormalization.STANDARD);

        System.out.print("Microphone initialization\n");
        format = new AudioFormat(sampleRate, bitsPerRecord, channels, signed, bigEndian);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format); // format is an AudioFormat object

        if (!AudioSystem.isLineSupported(info)) {
            System.err.print("isLineSupported failed");
            System.exit(1);
        }

        try {
            microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);
            System.out.print("Microphone opened with format: "+format.toString()+"\n");
            microphone.start();
        }catch(Exception ex){
            System.out.println("Microphone failed: "+ex.getMessage());
            System.exit(1);
        }

    }

    public int readPcm(){
        int numBytesRead=
                microphone.read(byteData, 0, byteData.length);
        if(numBytesRead!=byteData.length){
            System.out.println("Warning: read less bytes than buffer size");
            System.exit(1);
        }
        return numBytesRead;
    }


    public void byteToDouble(){
        ByteBuffer buf= ByteBuffer.wrap(byteData);
        buf.order(ByteOrder.BIG_ENDIAN);
        int i=0;

        while(buf.remaining()>2){
            short s = buf.getShort();
            doubleData[ i ] = (Short.valueOf(s)).doubleValue();
            ++i;
        }
        //System.out.println("Parsed "+i+" doubles from "+byteData.length+" bytes");
    }


    /*public double findFrequency(){
        double frequency;
        Complex[] cmplx= transformer.transform(doubleData, TransformType.FORWARD);
        double real;
        double im;
        double mag[] = new double[cmplx.length];

        for(int i = 0; i < cmplx.length; i++){
            real = cmplx[i].getReal();
            im = cmplx[i].getImaginary();
            mag[i] = Math.sqrt((real * real) + (im*im));
        }

        double peak = -1.0;
        int index=-1;
        for(int i = 0; i < cmplx.length; i++){
            if(peak < mag[i]){
                index=i;
                peak= mag[i];
            }
        }
        frequency = (sampleRate * index) / audioFrames;
        //System.out.print("Index: "+index+", Frequency: "+frequency+"\n");
        return frequency;
    }*/

    /*
     * Print the first frequency bins to know about the resolution we have
     */
    public void printFreqs(){
        for (int i=0; i<audioFrames/4; i++){
            System.out.println("bin "+i+", freq: "+(sampleRate*i)/audioFrames);
        }
    }

    public static void main(String[] args) {




        /*AudioInput ai= new AudioInput();
        int turns=100000;
        int lastFreq = -1;

        boolean started = false ;
        boolean ended = false;

        while(turns-- > 0){
            ai.readPcm();
            ai.byteToDouble();
            double freq = ai.findFrequency();
            //System.out.println(turns);
            //if(ended)
            //    System.out.println("Found: " + freq + " Hz");

            if(((int) freq >= 4170 && (int) freq <= 4180) && !started) {
                System.out.println("start");
                started = true;
            }

            if(((int) freq >= 4195 && (int) freq <= 4205) && started) {
                System.out.println("end");
                started = false;
                ended = true;
            }

            if(!started)
                continue;

            char c = '-';
            String s = CharMap.map.get((int) freq);

            if(lastFreq < 0)
            {
                lastFreq = (int) freq;
                continue;
            }

            if(lastFreq != (int) freq)
            {
                lastFreq = (int) freq;
                continue;
            }
            lastFreq = (int) freq;

            if(s != null)
                System.out.println(freq + " " + s);
        }*/
        //ai.printFreqs();
    }


}
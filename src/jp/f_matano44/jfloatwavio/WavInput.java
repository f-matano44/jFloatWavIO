package jp.f_matano44.jfloatwavio;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;


public class WavInput
{
    private int fs;     // sample rate
    private double[] x; // signal data
    private AudioFormat format;
    // ----------------------------------------------------------
    public int getFs(){ return this.fs; }       // getter of sampling rate
    public double[] getX(){ return this.x; }    // getter of signal date (double)
    public float[] getXf(){                     // getter of signal date (float)
        float[] retX = new float[this.x.length];
        for(int i=0; i<this.x.length; i++)
            retX[i] = (float)this.x[i];
        return retX;
    }


    public WavInput(String FILENAME)
    {
        File f = new File(FILENAME);
        byte[] sBuffer = null;
        AudioFormat sFormat = null;

        // import signal data
        try (var s = AudioSystem.getAudioInputStream(f))
        {
            sBuffer = s.readAllBytes();
            sFormat = s.getFormat();

            if(sFormat.getChannels() == 2)
                throw new Exception("jFloatWavIO is allowed monoral only..");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        // get signal data
        this.x = byte2double(sBuffer, sFormat.getSampleSizeInBits()/8, sFormat.isBigEndian());
        // get sampling rate
        this.fs = (int)sFormat.getSampleRate();
        // get format info
        this.format = sFormat;
    }


    private double[] byte2double(byte[] buf, int sampleSize, boolean isBigEndian)
    {
        List<Double> xBuffer = new ArrayList<Double>();

        return xBuffer.stream().mapToDouble(i -> i).toArray();
    }


    public void printAudioFormat()
    {
        System.out.println("Encoding: " + this.format.getEncoding());
        System.out.println("isBigEndian: " + this.format.isBigEndian());        
        System.out.println("Channels:" + this.format.getChannels());
        System.out.println("");
        System.out.println("SampleRate: " + this.format.getSampleRate());
        System.out.println("SampleSizeInBits: " + this.format.getSampleSizeInBits());
        System.out.println("");
        System.out.println("FrameRate: " + this.format.getFrameRate());
        System.out.println("FrameSize: " + this.format.getFrameSize());
    }
}

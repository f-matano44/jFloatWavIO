package jp.f_matano44.jfloatwavio;

import java.io.File;
import java.nio.ByteBuffer;

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
        float[] xf = new float[this.x.length];
        for(int i=0; i<this.x.length; i++)
            xf[i] = (float)this.x[i];
        return xf;
    }


    public WavInput(String FILENAME)
    {
        final File f = new File(FILENAME);
        byte[] sBytes = null;
        AudioFormat sFormat = null;

        // import wav file data
        try (var s = AudioSystem.getAudioInputStream(f))
        {
            sBytes = s.readAllBytes();
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
        this.x = byte2double(sBytes, sFormat.getSampleSizeInBits(), sFormat.isBigEndian());
        // get sampling rate
        this.fs = (int)sFormat.getSampleRate();
        // get format info
        this.format = sFormat;
    }


    private double[] byte2double(
        final byte[] byteArray, final int bitDepth, final boolean isBigEndian)
    {
        final int byteDepth = bitDepth / 8, sampleNum = byteArray.length / byteDepth;
        final int intIs4Bytes = 4;
        double[] doubleArray = new double[sampleNum];
        byte[] temp = new byte[intIs4Bytes];

        for(int i=0; i<byteArray.length; i++)
        {
            final int tPos = i % byteDepth, dPos = i / byteDepth;

            temp[tPos] = byteArray[i];
            if(tPos % byteDepth == byteDepth - 1)
            {
                for(int j=byteDepth; j<intIs4Bytes; j++)
                {
                    int check = (temp[byteDepth - 1] >> 7) & 1;
                    if(check == 0)  temp[j] = 0;
                    else            temp[j] = -1;
                }
                temp = getBigEndian(temp, isBigEndian);
                // byte to int
                doubleArray[dPos] = (double)ByteBuffer.wrap(temp).getInt();
                // int to double
                doubleArray[dPos] = (double)doubleArray[dPos] / Math.pow(2, bitDepth);
                //System.out.printf("%1.3f\n", doubleArray[dPos]);
            }
        }

        return doubleArray;
    }


    private byte[] getBigEndian(byte[] array, final boolean isBigEndian)
    {
        if(!isBigEndian)
        {
            final int tail = array.length - 1;
            for(int i=0; i<(array.length/2); i++)
            {
                byte temp;
                temp = array[i];
                array[i] = array[tail-i];
                array[tail-i] = temp;
            }
        }
        return array;
    }


    public void printAudioFormat()
    {
        System.out.println("Encoding: " + this.format.getEncoding());
        System.out.println("isBigEndian: " + this.format.isBigEndian());        
        System.out.println("Channels:" + this.format.getChannels());
        System.out.println("");
        System.out.println("SampleRate: " + this.format.getSampleRate());
        System.out.println("BitDepth: " + this.format.getSampleSizeInBits());
        System.out.println("");
        System.out.println("FrameRate: " + this.format.getFrameRate());
        System.out.println("FrameSize: " + this.format.getFrameSize());
    }
}

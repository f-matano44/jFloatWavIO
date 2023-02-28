package jp.f_matano44.jfloatwavio;

import java.io.*;
import java.nio.*;
import javax.sound.sampled.*;


public class WavIO
{
    // variables
    private AudioFormat format;
    private int fs;       // sample rate
    private double[][] x; // signal data
    // constants
    private static final int intIs4Bytes = 4;

    // getter
    public AudioFormat getFormat(){ return this.format; }
    public int getFs(){ return this.fs; }
    public double[][] getX(){ return this.x; }
    public float[][] getXf(){
        float[][] xf = new float[this.x.length][this.x[0].length];
        for(int i=0; i<this.x.length; i++)
            for(int j=0; j<this.x[i].length; j++)
                xf[i][j] = (float)this.x[i][j];
        return xf;
    }


    // constructor
    public WavIO(String FILENAME)
    {
        final File f = new File(FILENAME);
        byte[] sByteArray = null;
        byte[][] arraySeparatedByChannels = null;
        AudioFormat sFormat = null;
        int channels = 0, xLength = 0, nBits = 0;

        // import wav file data
        try (var s = AudioSystem.getAudioInputStream(f))
        {
            sByteArray = s.readAllBytes();
            sFormat = s.getFormat();

            channels = sFormat.getChannels();
            xLength = (sByteArray.length/channels)/(8 * sFormat.getSampleSizeInBits());
            nBits = sFormat.getSampleSizeInBits();

            // reject don't allowed files
            if(channels != 1 && channels != 2)
                throw new Exception("jFloatWavIO is allowed monoral or stereo only..");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        arraySeparatedByChannels = new byte[channels][sByteArray.length/channels];
        this.x = new double[channels][xLength];
        // separate byte data by channels (次はここを作る)
        arraySeparatedByChannels[0] = sByteArray;
        // get signal data
        for(int i=0; i<channels; i++)
            this.x[i] = byte2double(
                arraySeparatedByChannels[i], nBits, sFormat.isBigEndian()
            );
        // get sampling rate
        this.fs = (int)sFormat.getSampleRate();
        // get format info
        this.format = sFormat;
    }


    // methods
    protected double[] byte2double(
        final byte[] byteArray, final int nBits, final boolean isBigEndian)
    {
        final int
            nBytes = nBits / 8,
            sampleNum = byteArray.length / nBytes;
        double[] doubleArray = new double[sampleNum];
        byte[] temp = new byte[intIs4Bytes];

        for(int i=0; i<byteArray.length; i++)
        {
            final int
                tPos = i % nBytes,
                dPos = i / nBytes;

            temp[tPos] = byteArray[i];
            if(tPos % nBytes == nBytes - 1)
            {
                // preProcess
                temp = getBigEndian(temp, isBigEndian);
                temp = fillArray(temp, nBytes);
                // byte to int
                doubleArray[dPos] = (double)ByteBuffer.wrap(temp).getInt();
                // int to double
                doubleArray[dPos] = (double)doubleArray[dPos] / Math.pow(2, nBits);
            }
        }

        return doubleArray;
    }


    protected byte[] getBigEndian(byte[] array, final boolean isBigEndian)
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


    protected byte[] fillArray(byte[] array, int nBytes)
    {
        final int 
            fillDigit = intIs4Bytes-nBytes,
            fillNum = (array[fillDigit] >> 7) & 1;
        for(int i=0; i<fillDigit; i++)
        {
            if(fillNum == 0)
                array[i] = 0;
            else
                array[i] = -1;
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

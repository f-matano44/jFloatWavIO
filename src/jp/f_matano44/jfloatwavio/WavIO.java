package jp.f_matano44.jfloatwavio;

import java.io.*;
import java.nio.*;
import javax.sound.sampled.*;


public class WavIO
{
    // member variables
    private AudioFormat format;
    private double[][] x; // signal data
    // constants
    private static final int intIs4Bytes = 4;


    // getter
    public AudioFormat getFormat(){ return this.format; }
    public double[][] getX(){ return this.x; }
    public float[][] getXf(){
        float[][] xf = new float[this.x.length][this.x[0].length];
        for(int i=0; i<this.x.length; i++)
            for(int j=0; j<this.x[i].length; j++)
                xf[i][j] = (float)this.x[i][j];
        return xf;
    }


    // -----------------------------------------------------------------------
    // constructor (from array)
    public WavIO(final AudioFormat f, double[]... x) throws Exception
    {
        final String exceptionString = 
        "\njFloatWavIO can't read this signal." +
        "\nPlease check for AudioFormat..";

        this.x = x;
        this.format = f;

        // reject don't allowed files
        if(!isFormatOK(this.format))
            throw new Exception(exceptionString);
    }


    // constructor (from file)
    public WavIO(final String FILENAME) throws Exception
    {
        final File f = new File(FILENAME);
        byte[] sBytes = null;
        byte[][] sBytesSeparatedByChannels = null;

        // import wav file data
        final var s = AudioSystem.getAudioInputStream(f);
        final String exceptionString = 
            "\njFloatWavIO can't open this file." +
            "\nPlease check for file format..";

        sBytes = s.readAllBytes();
        this.format = s.getFormat();

        // reject don't allowed files
        if(!isFormatOK(this.format))
            throw new Exception(exceptionString);

        // byte -> double
        sBytesSeparatedByChannels = this.separateByChannels(sBytes);
        this.x = this.byte2double(sBytesSeparatedByChannels);
    }


    // -----------------------------------------------------------------------
    private boolean isFormatOK(AudioFormat f)
    {
        final int channels, nBits;

        channels = f.getChannels();
        nBits = f.getSampleSizeInBits();

        if(
            f.getEncoding() == AudioFormat.Encoding.PCM_SIGNED &&
            (channels == 1 || channels == 2) &&
            (nBits == 8 || nBits == 16 || nBits == 24 || nBits == 32)
        )
            return true;
        else
            return false;
    }


    private byte[][] separateByChannels(final byte[] byteArray)
    {
        final int
            channels = this.format.getChannels(),
            frameSize = this.format.getFrameSize(),
            sampleSize = frameSize / channels;
        int[] pos = new int[channels];
        byte[][] sepByte = new byte[channels][byteArray.length/channels];

        if(channels == 2)
        {
            for(int i=0; i<byteArray.length; i+=frameSize)
            {
                for(int j=0; j<sampleSize; j++)
                {
                    sepByte[0][pos[0]] = byteArray[i+j];
                    pos[0]++;
                }   
                for(int j=sampleSize; j<frameSize; j++)
                {
                    sepByte[1][pos[1]] = byteArray[i+j];
                    pos[1]++;
                }   
            }
        }
        else // channels == 1
        {
            sepByte[0] = byteArray;
        }

        return sepByte;
    }


    private double[][] byte2double(final byte[][] bArray)
    {
        final int SIGN = 1,
            channels = this.format.getChannels(),
            nBits = this.format.getSampleSizeInBits(),
            sampleSize = nBits / 8,
            sampleNum = bArray[0].length / sampleSize;
        byte[] temp = new byte[intIs4Bytes];
        double[][] dArray = new double[channels][sampleNum];

        for(int c=0; c<channels; c++)
        {
            for(int i=0; i<bArray[c].length; i++)
            {
                final int
                    tPos = i % sampleSize,
                    dPos = i / sampleSize;
    
                temp[tPos] = bArray[c][i];
                if(tPos % sampleSize == sampleSize - 1)
                {
                    // preProcess
                    temp = this.getBigEndian(temp);
                    temp = this.fillArray(temp);
                    // byte to int
                    dArray[c][dPos] = (double)ByteBuffer.wrap(temp).getInt();
                    // int to double
                    dArray[c][dPos] = (double)dArray[c][dPos] / Math.pow(2, nBits - SIGN);
                }
            }
        }

        return dArray;
    }


    private byte[] getBigEndian(byte[] array)
    {
        final boolean isBigEndian = this.format.isBigEndian();

        if(!isBigEndian)
        {
            final int tail = array.length - 1;

            for(int i=0; i<(array.length/2); i++)
            {
                byte temp = array[i];
                array[i] = array[tail-i];
                array[tail-i] = temp;
            }
        }

        return array;
    }


    private byte[] fillArray(byte[] array)
    {
        final int 
            nBits = this.format.getSampleSizeInBits(),
            sampleSize = nBits / 8,
            fillDigit = intIs4Bytes - sampleSize,
            fillNum = (array[fillDigit] >> 7) & 1;

        for(int i=0; i<fillDigit; i++)
        {
            if(fillNum == 0)
                array[i] = 0;
            else // fillNum == 1
                array[i] = -1;
        }

        return array;
    }


    // -----------------------------------------------------------------------
    // output to wav
    public void outputData(final String FILENAME)
    {
        // double -> int
        // int -> byte array
        // connect byte array
        // output
    }


    // -----------------------------------------------------------------------
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

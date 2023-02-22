package jp.f_matano44.jfloatwavio;

import java.nio.ByteBuffer;
import javax.sound.sampled.AudioFormat;

abstract class WavInput
{
    // variables
    protected AudioFormat format;
    protected int fs;       // sample rate
    protected double[][] x; // signal data
    // constants
    protected static final int intIs4Bytes = 4;


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

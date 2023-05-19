import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import jp.f_matano44.jfloatwavio.WavIO;

public class Test
{
    public static void main(String[] args)
    {
        AudioFormat input1Format, outputFormat;
        WavIO input1=null, output=null;
        double[] x, y;


        /* 
         * input wav file 
         * usage: WavIO WavIO_Obj = new WavIO(String FILENAME);
         */
        try{
            input1 = new WavIO("zundamon.wav");
        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }


        // get format
        input1Format = input1.getFormat();
        final float fs = input1Format.getSampleRate();
        final int nbits = input1Format.getSampleSizeInBits();
        final int frameSize = input1Format.getFrameSize();
        final float frameRate = input1Format.getFrameRate();


        /* 
         * get double array 
         * 
         * usage
         *  double[][]  signal = wavIO_Obj.getSignal();
         *  float[][]   signal = wavIO_Obj.getFloatSignal();
         * 
         * usage (static method)
         *  double[][]  signal = wavIO_Obj.getSignal();
         *  float[][]   signal = wavIO_Obj.getFloatSignal();
         */
        x = input1.getSignal()[0];
        y = WavIO.sGetSignal("metan.wav")[0];


        // set format
        final int outputChannels = 2;
        final float outputFs = fs;
        final int outputNbits = nbits;
        final int outputFrameSize = frameSize * outputChannels;
        final float outputframeRate = frameRate;
        final boolean bigEndian = false;
        outputFormat = new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED, outputFs, outputNbits, outputChannels,
            outputFrameSize, outputframeRate, bigEndian
        );


        /* 
         * WavIO object can be generated from signal. 
         * If not supported format, throws Exception.
         * This call requires that the arrays of argument are same length.
         * 
         * usage
         *  WavIO WavIO_Obj = new WavIO(AudioFormat format, double[]... signal);
         */
        try{
            output = new WavIO(outputFormat, x, y);
        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }


        /* 
         * print format info
         * usage: WavIO_Obj.printAudioFormat();
         */
        output.printAudioFormat();


        /* 
         * output wav file 
         *
         * usage
         *  wavIO_Obj.outputData(String FILENAME);
         * 
         * usage (static method)
         *  WavIO.outputData(String FILENAME, int nbits, double fs, double[]... signal);
         *      signal[0]: left
         *      signal[1]: right
        */
        try{
            output.outputData("helloworld.wav");
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Failed of writing file.");
        }
    }
}

import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import jp.f_matano44.jfloatwavio.WavIO;

/** test program. */
public class Test {
    /** main method of test program. */
    public static void main(String[] args) {
        WavIO input1 = null;
        WavIO output = null;

        /* 
         * input wav file 
         * usage: WavIO WavIO_Obj = new WavIO(String FILENAME);
         */
        try {
            input1 = new WavIO("zundamon.wav");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }


        /* get format */
        final AudioFormat input1Format = input1.getFormat();
        final float fs = input1Format.getSampleRate();
        final int nbits = input1Format.getSampleSizeInBits();


        /* 
         * get {float, double} array
         *      signal[0]: monoral or left
         *      signal[1]: right
         * 
         * usage
         *  double[][]  signal = wavIO_Obj.getSignal();
         *  float[][]   signal = wavIO_Obj.getFloatSignal();
         * 
         * usage (static method)
         *  double[][]  signal = WavIO.sGetSignal();
         *  float[][]   signal = WavIO.sGetFloatSignal();
         */
        final double[] x = input1.getSignal()[0];
        final double[] y = WavIO.sGetSignal("metan.wav")[0];


        // set format
        final int outputChannels = 2;
        final float outputFs = fs;
        final int outputNbits = nbits;
        final boolean signed = true; // This library is allowed PCM-SIGN only.
        final boolean bigEndian = false;
        final AudioFormat outputFormat = new AudioFormat(
            (float) outputFs, outputNbits, outputChannels, signed, bigEndian
        );


        /* 
         * WavIO object can be generated from signal. 
         * If not supported format, throws Exception.
         * This call requires that the arrays of argument are same length.
         * 
         * usage
         *  WavIO WavIO_Obj = new WavIO(AudioFormat format, double[]... signal);
         */
        try {
            output = new WavIO(outputFormat, x, y);
        } catch (Exception e) {
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
         *      signal[0]: left or mono
         *      signal[1]: right
        */
        try {
            output.outputData("output01.wav");
            WavIO.sOutputData("output02.wav", outputNbits, outputFs, x, y);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed of writing file.");
        }
    }
}

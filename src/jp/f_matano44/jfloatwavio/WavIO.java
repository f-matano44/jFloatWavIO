package jp.f_matano44.jfloatwavio;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

/** Java library for wav file as float. */
public class WavIO {
    /**
     * Retrieves the wave signal from the provided file and returns it as a 2D array of doubles.
     * This is a static method that can be used without instantiating the class.
     *
     * @param filename 
     *      The name of the file from which to read the wave signal.
     * @return
     *      A 2D array of doubles representing the wave signal.
     *      success: double[][]
     *      failure: null
     */
    public static double[][] sGetSignal(final String filename) {
        try {
            return new WavIO(filename).getSignal();
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Retrieves the AudioFormat from the specified file.
     * This is a static method that can be used without instantiating the class.
     *
     * @param filename
     *      The name of the file from which to retrieve the AudioFormat.
     * @return
     *      The AudioFormat extracted from the file.
     *      success: double[][]
     *      failure: null
     */
    public static AudioFormat sGetFormat(final String filename) {
        try {
            return new WavIO(filename).getFormat();
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Writes the provided signal data to a .wav file.
     * This is a static method that can be used without instantiating the class.
     *
     * @param filename The name of the output .wav file.
     * @param nbits The bit depth for the audio data.
     * @param fs The sampling rate of the audio data.
     * @param signal The audio signal data to be written.
     * 
     * @return status
     *       0: success
     *      -1: failure
     */
    public static int sOutputData(
        final String filename, final int nbits, final double fs,
        final double[]... signal
    ) {
        // フォーマットの設定
        final int channels = signal.length;
        final int frameSize = (nbits / 8) * channels;
        AudioFormat outputFormat = new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED, (float) fs, nbits, channels, 
            frameSize, (float) fs, false
        );

        try {
            new WavIO(outputFormat, signal).outputData(filename);
        } catch (Exception e) {
            return -1;
        }
        return 0;
    }


    // メンバ変数
    private final AudioFormat format;
    private final double[][] signal;
    // 定数
    private static final int intIs4Bytes = 4;


    /**
     * getter of audio format.
     *
     * @return AudioFormat
     */
    public AudioFormat getFormat() {
        return this.format;
    }


    /**
     * getter of signal data.
     *
     * @return signal data as double[][]
     */
    public double[][] getSignal() {
        double[][] x = new double[this.signal.length][];
        for (int i = 0; i < this.signal.length; i++) {
            x[i] = this.signal[i].clone();
        }
        return x;
    }


    /**
     * getter of signal data.
     *
     * @return signal data as float[][]
     */
    public float[][] getFloatSignal() {
        float[][] xf = new float[this.signal.length][this.signal[0].length];
        for (int i = 0; i < this.signal.length; i++) {
            for (int j = 0; j < this.signal[i].length; j++) {
                xf[i][j] = (float) this.signal[i][j];
            }
        }
        return xf;
    }


    /**
     * Constructs a new WavIO object from the provided signal array,
     * using the provided AudioFormat.
     *
     * @param f AudioFormat object specifying the data format of the audio data.
     * @param signal Array containing the audio signal data.
     *
     * @throws Exception if the provided AudioFormat "f" is not supported.
     */
    public WavIO(final AudioFormat f, double[]... signal)
        throws Exception {
        // ファイルフォーマットが異常な場合に例外を投げる
        if (!isFormatOK(f) || signal.length != f.getChannels()) {
            final String exceptionString = 
                "\njFloatWavIO can't read this signal."
                + "\nPlease check for AudioFormat...";
            throw new Exception(exceptionString);
        }

        final int arrayLength;
        final int channels = f.getChannels();
        // 配列長の決定 (長い方に揃える)
        if (channels == 2 && (signal[0].length < signal[1].length)) {
            arrayLength = signal[1].length;
        } else { 
            arrayLength = signal[0].length;
        }

        // メンバ変数にコピー
        this.signal = new double[channels][arrayLength];
        for (int i = 0; i < channels; i++) {
            for (int j = 0; j < signal[i].length; j++) {
                this.signal[i][j] = signal[i][j];
            }
        }
        this.format = f;
    }


    /**
     * Constructs a new WavIO object from the provided file name.
     *
     * @param filename the name of the file to be processed
     * @throws Exception 
     *      if an error occurs during file reading
     *      or provided file is not supported
     */
    public WavIO(final String filename) throws Exception {
        // wav の読み込み
        final File f = new File(filename);
        final var s = AudioSystem.getAudioInputStream(f);
        final String exceptionString = 
            "\njFloatWavIO can't open this file."
            + "\nPlease check for file format..";

        // ファイルフォーマットが異常な場合に例外を投げる
        this.format = s.getFormat();
        if (!isFormatOK(this.format)) {
            throw new Exception(exceptionString);
        }

        // byte -> double
        final byte[] sBytes = s.readAllBytes();
        final byte[][] sBytesSeparatedByChannels = separateByChannels(sBytes);
        this.signal = byte2double(sBytesSeparatedByChannels);
    }


    /** print audio format to System.out. */
    public void printAudioFormat() {
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


    // -----------------------------------------------------------------------
    private boolean isFormatOK(AudioFormat f) {
        final int channels = f.getChannels();
        final int nBits = f.getSampleSizeInBits();

        if (
            f.getEncoding() == AudioFormat.Encoding.PCM_SIGNED
                && (channels == 1 || channels == 2)
                && (nBits == 8 || nBits == 16 || nBits == 24 || nBits == 32)
        ) {
            return true;
        } else { 
            return false;
        }
    }


    private byte[][] separateByChannels(final byte[] byteArray) {
        final int channels = this.format.getChannels();
        final int frameSize = this.format.getFrameSize();
        final int sampleSize = frameSize / channels;
        final int[] pos = new int[channels];
        final byte[][] sepByte = new byte[channels][byteArray.length / channels];

        if (channels == 2) {
            for (int i = 0; i < byteArray.length; i += frameSize) {
                for (int j = 0; j < sampleSize; j++) {
                    sepByte[0][pos[0]] = byteArray[i + j];
                    pos[0]++;
                }   
                for (int j = sampleSize; j < frameSize; j++) {
                    sepByte[1][pos[1]] = byteArray[i + j];
                    pos[1]++;
                }   
            }
        } else { // channels == 1
            sepByte[0] = byteArray;
        }

        return sepByte;
    }


    private double[][] byte2double(final byte[][] bArray) {
        final int SIGN = 1;
        final int channels = this.format.getChannels();
        final int nBits = this.format.getSampleSizeInBits();
        final int sampleSize = nBits / 8;
        final int sampleNum = bArray[0].length / sampleSize;
        byte[] temp = new byte[intIs4Bytes];
        final double[][] dArray = new double[channels][sampleNum];

        for (int c = 0; c < channels; c++) {
            for (int i = 0; i < bArray[c].length; i++) {
                final int tPos = i % sampleSize;
                final int dPos = i / sampleSize;
    
                temp[tPos] = bArray[c][i];
                if (tPos % sampleSize == sampleSize - 1) {
                    // preProcess
                    temp = this.getBigEndian(temp);
                    temp = this.fillArray(temp);
                    // byte to int
                    dArray[c][dPos] = (double) ByteBuffer.wrap(temp).getInt();
                    // int to double
                    dArray[c][dPos] = (double) dArray[c][dPos] / Math.pow(2, nBits - SIGN);
                }
            }
        }

        return dArray;
    }


    private byte[] getBigEndian(byte[] array) {
        final boolean isBigEndian = this.format.isBigEndian();

        if (!isBigEndian) {
            final int tail = array.length - 1;
            for (int i = 0; i < (array.length / 2); i++) {
                final byte temp = array[i];
                array[i] = array[tail - i];
                array[tail - i] = temp;
            }
        }

        return array;
    }


    private byte[] fillArray(byte[] array) {
        final int nBits = this.format.getSampleSizeInBits();
        final int sampleSize = nBits / 8;
        final int fillDigit = intIs4Bytes - sampleSize;
        final int fillNum = (array[fillDigit] >> 7) & 1;

        for (int i = 0; i < fillDigit; i++) {
            if (fillNum == 0) {
                array[i] = 0;
            } else { // fillNum == 1
                array[i] = -1;
            }
        }

        return array;
    }


    /**
     * Output the member variable (double[][] signal) as a WAV file.
     *
     * @param filename 
     *      the name of the WAV file to be created
     * @throws IOException 
     *      if an I/O error occurs while writing the file
    */
    public void outputData(final String filename) throws IOException {
        final int SIGN = 1;
        final int channels = this.format.getChannels();
        final int nBits = this.format.getSampleSizeInBits();
        final int sampleSize = nBits / 8;
        final int doubleArrayLength = this.signal[0].length;
        final int[][] intSignal = new int[channels][doubleArrayLength];
        final byte[] conBytes = new byte[channels * doubleArrayLength * sampleSize];
        final byte[][]
            notConBytes = new byte[channels][doubleArrayLength * sampleSize];

        // double -> int
        for (int i = 0; i < channels; i++) {
            for (int j = 0; j < doubleArrayLength; j++) {
                intSignal[i][j] = (int) (this.signal[i][j] * Math.pow(2, nBits - SIGN));
            }
        }

        // int -> byte array
        for (int i = 0; i < channels; i++) {
            int bytePos = 0;
            for (int j = 0; j < doubleArrayLength; j++) {
                byte[] intToByte = 
                    ByteBuffer.allocate(4).putInt(intSignal[i][j]).array();
                byte[] buf = Arrays.copyOfRange(intToByte, 4 - sampleSize, 4);
                
                // if wanted little endian, convert to it.
                if (!this.format.isBigEndian()) {
                    final int tail = sampleSize - 1;
                    for (int k = 0; k < (sampleSize / 2); k++) {
                        final byte temp = buf[k];
                        buf[k] = buf[tail - k];
                        buf[tail - k] = temp;
                    }
                }
                
                // insert byte array
                for (int k = 0; k < buf.length; k++) {
                    notConBytes[i][bytePos] = buf[k];
                    bytePos++;
                }
            }
        }

        // connect byte array
        int connectPos = 0;
        for (int i = 0; i < doubleArrayLength * sampleSize; i += sampleSize) {
            for (int j = 0; j < channels; j++) {
                for (int k = 0; k < sampleSize; k++) {
                    conBytes[connectPos] = notConBytes[j][i + k];
                    connectPos++;
                }
            }
        }

        // output
        final InputStream is = new ByteArrayInputStream(conBytes); 
        final AudioInputStream ais = new AudioInputStream(is, this.format, conBytes.length);
        AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File(filename));
    }
}

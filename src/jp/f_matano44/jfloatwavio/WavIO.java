package jp.f_matano44.jfloatwavio;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;


/** Java library for wav file as float. */
public class WavIO {
    public static void main(String[] args) {
        System.out.println("jFloatWavIO @ Mozilla Public License 2.0");
        System.out.println("Copyright 2023 Fumiyoshi MATANO");
    }


    /**
     * Retrieves the wave signal from the provided file and returns it as a 2D array of doubles.
     * This is a static method that can be used without instantiating the class.
     *
     * @param filename 
     *      The name of the file from which to read the wave signal.
     * @return
     *      A 2D array of doubles representing the wave signal.
     *      <ul>
     *          <li> success: double[][]
     *              <ul>
     *                  <li> signal[0]: left or mono </li>
     *                  <li> signal[1]: right </li>
     *              </ul>
     *          </li>
     *          <li> failure: null </li>
     *      </ul>
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
     *      <ul>
     *          <li> success: AudioFormat </li>
     *          <li> failure: null </li>
     *      </ul>
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
     * Encoding: PCM_SIGNED
     * Endian: Little Endian
     *
     * @param filename The name of the output .wav file.
     * @param nbits The bit depth for the audio data.
     * @param fs The sampling rate of the audio data.
     * @param monoral The audio signal data to be written.
     * 
     * @return status
     *      <ul>
     *          <li>  0: success </li>
     *          <li> -1: failure </li>
     *      </ul>
     */
    public static int sOutputData(
        final String filename, final int nbits, final double fs,
        final double[] mono
    ) {
        return sOutputDataBody(filename, nbits, fs, mono);
    }


    /**
     * Writes the provided signal data to a .wav file.
     * This is a static method that can be used without instantiating the class.
     * Encoding: PCM_SIGNED
     * Endian: Little Endian
     *
     * @param filename The name of the output .wav file.
     * @param nbits The bit depth for the audio data.
     * @param fs The sampling rate of the audio data.
     * @param left The audio signal data to be written.
     * @param right The audio signal data to be written.
     * 
     * @return status
     *      <ul>
     *          <li>  0: success </li>
     *          <li> -1: failure </li>
     *      </ul>
     */
    public static int sOutputData(
        final String filename, final int nbits, final double fs,
        final double[] left, final double[] right
    ) {
        return sOutputDataBody(filename, nbits, fs, left, right);
    }


    private static int sOutputDataBody(
        final String filename, final int nbits, final double fs,
        final double[]... signal
    ) {
        try {
            final int channels = signal.length;
            final AudioFormat outputFormat = new AudioFormat(
                    (float) fs, nbits, channels, true, false
                );

            if (isFormatOK(outputFormat)) {
                new WavIO(outputFormat, signal).outputData(filename);
                return 0;
            } else {
                throw new UnsupportedAudioFileException();
            }
        } catch (Exception e) {
            return -1;
        }
    }


    // member variable
    private final AudioFormat format;
    private final double[][] signal;


    /**
     * getter of audio format.
     *
     * @return AudioFormat (Immutable)
     */
    public AudioFormat getFormat() {
        return this.format;
    }


    /**
     * getter of signal data.
     *
     * @return signal data as double[][]
     * <ul>
     *      <li> signal[0]: left or mono </li>
     *      <li> signal[1]: right </li>
     * </ul>
     */
    public double[][] getSignal() {
        final double[][] x = new double[this.signal.length][];
        for (int i = 0; i < this.signal.length; i++) {
            x[i] = this.signal[i].clone();
        }
        return x;
    }


    /**
     * getter of signal data.
     *
     * @return signal data as float[][]
     *      <ul>
     *          <li> signal[0]: left or mono </li>
     *          <li> signal[1]: right </li>
     *      </ul>
     */
    public float[][] getFloatSignal() {
        final float[][] xf = new float[this.signal.length][];
        for (int i = 0; i < this.signal.length; i++) {
            xf[i] = new float[this.signal[i].length];
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
     *      <ul>
     *          <li> signal[0]: left or mono </li>
     *          <li> signal[1]: right </li>
     *      </ul>
     *
     * @throws UnsupportedAudioFileException
     *      if the provided AudioFormat is not supported.
     * @throws IllegalArgumentException
     *      if AudioFormat and Signal channel counts don't match.
     */
    public WavIO(final AudioFormat f, final double[]... signal)
        throws UnsupportedAudioFileException, IllegalArgumentException {
        // ファイルフォーマットが異常な場合に例外を投げる
        if (!isFormatOK(f)) {
            throw new UnsupportedAudioFileException(
                "This format is unsupported."
            );
        } else if (signal.length != f.getChannels()) {
            throw new IllegalArgumentException(
                "AudioFormat and Signal channel counts don't match."
            );
        }

        // メンバ変数にコピー
        this.format = f;
        this.signal = adjustLength(signal);
    }


    /**
     * Constructs a new WavIO object from the provided file name.
     *
     * @param filename the name of the file to be processed
     * @throws IOException if an error occurs during file reading
     * @throws UnsupportedAudioFileException if Provided file is not supported
     */
    public WavIO(final String filename) 
        throws UnsupportedAudioFileException, IOException {
        // wav の読み込み
        final File f = new File(filename);
        final var ais = AudioSystem.getAudioInputStream(f);

        // ファイルフォーマットが異常な場合に例外を投げる
        this.format = ais.getFormat();
        if (!isFormatOK(this.format)) {
            throw new UnsupportedAudioFileException(
                "This format is unsupported."
            );
        }

        final int channels = this.format.getChannels();
        final int nBits = this.format.getSampleSizeInBits();
        final boolean isBigEndian = this.format.isBigEndian();
        // Separate by channels
        final byte[] notSepByteArray = ais.readAllBytes();
        final byte[][] sepByteArray = separateByChannels(notSepByteArray);
        // byte -> double
        final double[][] sig = new double[channels][];
        for (int index = 0; index < channels; index++) {
            sig[index] =
                Converter.byte2double(sepByteArray[index], nBits, isBigEndian);
        }
        this.signal = adjustLength(sig);
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
        // byte -> double
        final int channels = this.format.getChannels();
        final int nBits = this.format.getSampleSizeInBits();
        final boolean isBigEndian = this.format.isBigEndian();
        final byte[][] notConBytes = new byte[channels][];
        for (int i = 0; i < channels; i++) {
            notConBytes[i] = Converter.double2byte(this.signal[i], nBits, isBigEndian);
        }

        // connect byte array
        int connectPos = 0;
        final int sampleSize = nBits / 8;
        final int doubleArrayLength = this.signal[0].length;
        final byte[] conBytes = new byte[channels * doubleArrayLength * sampleSize];
        for (int i = 0; i < doubleArrayLength * sampleSize; i += sampleSize) {
            for (int j = 0; j < channels; j++) {
                for (int k = 0; k < sampleSize; k++) {
                    conBytes[connectPos] = notConBytes[j][i + k];
                    connectPos++;
                }
            }
        }

        // output
        final var is = new ByteArrayInputStream(conBytes); 
        final var ais = new AudioInputStream(is, this.format, conBytes.length);
        AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File(filename));
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


    // private methods -----------------------------------------------------------
    private static boolean isFormatOK(AudioFormat f) {
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


    public static double[][] adjustLength(final double[][] a) {
        final List<Integer> length = new ArrayList<>();
        for (final double[] array: a) {
            length.add(array.length);
        }
        final int maxLength = Collections.max(length);

        final double[][] ret = new double[a.length][maxLength];
        for (int i = 0; i < ret.length; i++) {
            System.arraycopy(a[i], 0, ret[i], 0, a[i].length);
        }
    
        return ret;
    }


    private byte[][] separateByChannels(final byte[] byteArray) {
        final int channels = this.format.getChannels();
        final int frameSize = this.format.getFrameSize();
        final int sampleSize = frameSize / channels;
        final int[] pos = new int[channels];
        final byte[][] sepByteArray = new byte[channels][byteArray.length / channels];

        if (channels == 2) {
            for (int i = 0; i < byteArray.length; i += frameSize) {
                for (int j = 0; j < sampleSize; j++) {
                    sepByteArray[0][pos[0]] = byteArray[i + j];
                    pos[0]++;
                }   
                for (int j = sampleSize; j < frameSize; j++) {
                    sepByteArray[1][pos[1]] = byteArray[i + j];
                    pos[1]++;
                }   
            }
        } else { // channels == 1
            sepByteArray[0] = byteArray;
        }

        return sepByteArray;
    }
}

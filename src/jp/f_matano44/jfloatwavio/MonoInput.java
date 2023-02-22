package jp.f_matano44.jfloatwavio;

import java.io.File;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;


public class MonoInput extends WavInput
{
    public int getFs(){ return super.fs; }          // getter of sampling rate
    public double[] getX(){ return super.x[0]; }    // getter of signal date (double)
    public float[] getXf(){                         // getter of signal date (float)
        float[] xf = new float[super.x.length];
        for(int i=0; i<super.x.length; i++)
            xf[i] = (float)super.x[0][i];
        return xf;
    }


    public MonoInput(String FILENAME)
    {
        final File f = new File(FILENAME);
        byte[] sByteArray = null;
        AudioFormat sFormat = null;
        int channels, xLength;

        // import wav file data
        try (var s = AudioSystem.getAudioInputStream(f))
        {
            sByteArray = s.readAllBytes();
            sFormat = s.getFormat();

            // reject don't allowed files
            if(sFormat.getChannels() != 1)
                throw new Exception("MonoInput is allowed monoral only..");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        channels = sFormat.getChannels();
        xLength = sByteArray.length/(8 * sFormat.getSampleSizeInBits());
        super.x = new double[channels][xLength];
        // get signal data
        super.x[0] = byte2double(sByteArray, sFormat.getSampleSizeInBits(), sFormat.isBigEndian());
        // get sampling rate
        super.fs = (int)sFormat.getSampleRate();
        // get format info
        super.format = sFormat;
    }
}

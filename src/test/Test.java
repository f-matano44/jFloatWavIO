import jp.f_matano44.jfloatwavio.*;

public class Test
{
    public static void main(String[] args)
    {
        // input wav file
        WavIO wav = new WavIO("helloworld.wav");

        // get double array
        double[][] x = wav.getX();

        // draw original waveform
        new DrawSignal("Signal", x);

        wav.printAudioFormat();
    }
}

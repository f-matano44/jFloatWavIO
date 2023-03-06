import jp.f_matano44.jfloatwavio.*;

public class Test
{
    public static void main(String[] args)
    {
        WavIO wav = new WavIO("helloworld.wav");
        double[][] x = wav.getX();

        new DrawSignal("Signal", x);

        wav.printAudioFormat();
    }
}

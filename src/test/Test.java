import jp.f_matano44.jfloatwavio.*;

public class Test
{
    public static void main(String[] args)
    {
        MonoInput wav = new MonoInput("helloworld.wav");
        double[] x = wav.getX();

        new DrawSignal("Signal", x, x, x);
        // wav.printAudioFormat();
    }
}

import jp.f_matano44.jfloatwavio.*;

public class Test
{
    public static void main(String[] args)
    {
        WavInput wav = new WavInput("helloworld.wav");

        wav.printAudioFormat();
    }
}

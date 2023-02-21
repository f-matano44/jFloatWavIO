import jp.f_matano44.jfloatwavio.WavInput;

public class Test
{
    public static void main(String[] args)
    {
        WavInput wav = new WavInput("helloworld.wav");
        double[] x = wav.getX();

        

        // wav.printAudioFormat();
    }
}

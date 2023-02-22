package jp.f_matano44.jfloatwavio;

import java.awt.*;
import javax.swing.*;

public class DrawSignal extends JFrame
{
    public DrawSignal(String titleString, double[] signal)
    {
        setTitle(titleString);
        setBackground(Color.WHITE);
        setSize(600, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // add graph panel
        getContentPane().add(this.new PanelClass(signal));
        // display panel
        setVisible(true);
    }


    private class PanelClass extends JPanel
    {
        private double[] x;
        public PanelClass(double[] x){ this.x = x; }

        @Override
        public void paint(Graphics g)
        {
            // canvas size
            final int frameWidth = 10;
            final Dimension d = getSize();
            final int canvasWidth = (int)d.getWidth() - (frameWidth*2);
            final int canvasHeight = (int)d.getHeight() - (frameWidth*2);
            // variable/constant for drawing
            final int 
                xAxis = 0, yAxis=1, startPoint=0, endPoint=1,
                xOffset = frameWidth,
                yOffset = frameWidth + canvasHeight / 2;
            int[][] pos = new int[2][2];
                pos[xAxis][endPoint] = xOffset;
                pos[yAxis][endPoint] = yOffset;
            final double ampSize = -0.9; // -1.0 ~ 0.0

            // clear frame
            g.clearRect(0, 0, (int)d.getWidth(), (int)d.getHeight());

            // if window is too narrow, display nothing.
            if(d.getWidth() < frameWidth*2 || d.getHeight() < frameWidth*2) return;

            // draw background
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(frameWidth, frameWidth, canvasWidth, canvasHeight);

            // draw signal
            g.setColor(Color.BLACK);
            for(int i=0; i<x.length; i++)
            {
                pos[xAxis][startPoint] = pos[xAxis][endPoint];
                pos[yAxis][startPoint] = pos[yAxis][endPoint];
                pos[xAxis][endPoint] = xOffset + (int)(canvasWidth * ((double)i / x.length));
                pos[yAxis][endPoint] = yOffset + (int)(x[i] * canvasHeight * ampSize);

                g.drawLine(
                    pos[xAxis][startPoint], pos[yAxis][startPoint],
                    pos[xAxis][endPoint], pos[yAxis][endPoint]
                );
            }
        }
    }
}

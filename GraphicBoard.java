import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class GraphicBoard extends JFrame{
    private  final Board board;
    public GraphicBoard(Board board){
        this.board=board;
        setSize(1500,1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }


    public void paint(Graphics g){
        paintHexagons(300.0, 200.0, g);
    }


    public double[] fillxArray(double top){
        double[] arr = new double[6];
        double val = 25*Math.pow(3, 0.5);

        arr[0]= top;
        arr[1]=top+val;
        arr[2]=top+val;
        arr[3]=top;
        arr[4]=top-val;
        arr[5]=top-val;

        return arr;
    }
    public double[] fillyArray(double top){
        double [] arr = new double[6];

        arr[0]=top;
        arr[1]=top+25;
        arr[2]=top+75;
        arr[3]=top+100;
        arr[4]=top+75;
        arr[5]=top+25;

        return arr;
    }
    public void drawRow(double initialxTop, double initialyTop, int numHex, Graphics g){
        Path2D.Double poly;
        double val = 50*Math.pow(3, 0.5);
        for(int i =0;i<numHex;i++){
            double []xarr= fillxArray(initialxTop+i*val);
            double []yarr= fillyArray(initialyTop);
            poly = new Path2D.Double();
            poly.moveTo(initialxTop+i*val, initialyTop);
            for(int j=1;j<6;j++){
                poly.lineTo(xarr[j], yarr[j]);
            }
            poly.closePath();
            Graphics2D g2d= (Graphics2D) g.create();
            g2d.draw(poly);
            g2d.dispose();
        }
    }

    public void paintHexagons(double initialx, double initaly, Graphics g){
        double xmod=25*Math.pow(3, 0.5), ymod=75;
        boolean isHalfway=false;
        int i=5;
        while (i!=4) {
            drawRow(initialx, initaly, i, g);

            if(i==9){
                isHalfway=true;
                xmod*=-1;
            }
            initialx-=xmod;
            initaly+=ymod;

            i = modifyi(i, isHalfway);
        }
    }

    public int modifyi(int i, boolean isHalfway){
        if(isHalfway){
            i--;
        }
        else i++;
        return i;
    }
}

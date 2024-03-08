import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

public class GraphicBoard extends JFrame{

    private static final double XMOD=25*Math.pow(3, 0.5);
    private static final double YMOD=75.0;
    private static Graphics grap = null;

    private  final Board board;
    public GraphicBoard(Board board){
        this.board=board;
        setSize(1000,1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }


    public void paint(Graphics g){
        double initialx=300.0, initialy=200.0;
        grap=g;
        paintHexagons(initialx, initialy, g);
        paintAtoms(initialx,initialy,g);

        //paintRays(initialx, initialy, g);

    }


    public double[] fillxArray(double top){
        double[] arr = new double[6];


        arr[0]= top;
        arr[1]=top+XMOD;
        arr[2]=top+XMOD;
        arr[3]=top;
        arr[4]=top-XMOD;
        arr[5]=top-XMOD;

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
        double val = 2*XMOD;
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
        double xmod=XMOD, ymod=YMOD;
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





    public void paintAtoms(double initialx, double initialy, Graphics g){
        Graphics2D g2d= (Graphics2D) g.create();

        double x, y;
        double w=50;

        g2d.setColor(Color.RED);

        ArrayList<Cell> cellArr=board.getAllCells();
        for(int i=0;i< cellArr.size();i++){
            if(cellArr.get(i).hasAtom()){
                //System.out.println(cellArr.get(i)+"   "+i);
                y=initialy+(cellArr.get(i).getRow()-1)*YMOD+25.0;
                x=initialx+getXdiff(i);
                drawAtom(x, y, w, g2d);
                drawCircleOfInf(x, y, w, g2d);
            }
        }




        g2d.dispose();
    }

    private void drawCircleOfInf(double x, double y, double w, Graphics2D g2d) {
        g2d.draw(new Ellipse2D.Double(x-50, y-50, w+100, w+100));
    }

    private double getXdiff(int i) {
        int first=0;
        double distance=0;
        if(i<5){
            distance=0;
            first=0;
        }
        else if(i<11){
            distance=XMOD;
            first=5;
        }
        else if(i<18){
            distance=XMOD*2;
            first=11;
        }
        else if(i<26){
            distance=XMOD*3;
            first=18;
        }
        else if(i<35){
            distance=XMOD*4;
            first=26;
        }
        else if(i<43){
            distance=XMOD*3;
            first=35;
        }
        else if(i<50){
            distance=XMOD*2;
            first=43;
        }
        else if(i<56){
            distance=XMOD;
            first=50;
        }
        else {
            distance=0;
            first=56;
        }
        return XMOD*2*(i-first)-distance-25;

    }

    public void drawAtom(double x, double y, double w, Graphics2D g2d){
        g2d.fill( new Ellipse2D.Double(x, y, w, w));
    }





    public void callPaintRays(){
        paintRays(300.0, 200.0, grap);
    }


    public void paintRays(double initialx, double initialy, Graphics g) {
        ArrayList<ArrayList<Integer>> raysArray = board.getAllRays();


        ArrayList<Integer> ray=raysArray.get(raysArray.size()-1);
        //System.out.println(ray+"SIZE="+ray.size());
        double xf, yf, xl, yl;

        xf=initialx+getXdiff(ray.getFirst());
        yf=initialy+(board.getAllCells().get(ray.getFirst()).getRow()-1)*YMOD+25.0;
        xl=initialx+getXdiff(ray.getLast());
        yl=initialy+(board.getAllCells().get(ray.getLast()).getRow()-1)*YMOD+25.0;

        Graphics2D g2d=(Graphics2D) g.create();
        drawRay(xf, yf, xl, yl, g2d);

        g2d.dispose();
    }

    public void drawRay(double xf, double yf, double xl, double yl, Graphics2D g2d){
        Path2D.Double path= new Path2D.Double();
        path.moveTo(xf, yf);
        path.lineTo(xl, yl);
        path.closePath();
        g2d.draw(path);
    }



}

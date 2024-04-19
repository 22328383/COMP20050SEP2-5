import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.geom.*;
import java.util.ArrayList;

public class GraphicBoard extends JFrame{

    private static final double initialXPos=300.0;
    private static final double initialYPos=200.0;
    private static final double XMOD=25*Math.pow(3, 0.5);
    private static final double YMOD=75.0;

    private boolean isGameOver=false;
    private int lastRayPainted=0;
    private int rayMarkerSetsPrinted=0;
    private int rayCellSide;
    private int gameLastRayPainted=0;
    private int backup;
    private ArrayList<Integer> rayMarkersBeginnings=new ArrayList<Integer>();
    private boolean isFirstTime=true;

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

        paintExampleHexagon(g);
        paintHexagons(initialx, initialy, g);
        //paintAtoms(initialx, initialy, g);
        cellNumbers(g);

        if(!isFirstTime) {
            //paintRays(initialx, initialy, g);
            paintRayMarkers(g);
        }
        if(isGameOver){
            paintAtoms(initialx, initialy, g);
        }
    }

    //Example hexagon for user to input
    public void paintExampleHexagon(Graphics g){
        Path2D.Double poly;
        double []xarr= fillxArray(100);
        double []yarr= fillyArray(100);
        poly = new Path2D.Double();
        poly.moveTo(100, 100);
        for(int j=1;j<6;j++){
            poly.lineTo(xarr[j], yarr[j]);

        }
        poly.closePath();

        Graphics2D g2d= (Graphics2D) g.create();
        g2d.draw(poly);
        g2d.dispose();

        g.drawString("0", 121, 107);
        g.drawString("1", 150, 150);
        g.drawString("2", 121, 200);
        g.drawString("3", 79, 200);
        g.drawString("4", 50, 150);
        g.drawString("5", 79, 107);
    }

    //used for painting hexagon board
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
    //used for painting hexagon board
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
    //used for painting hexagon board, one row at a time
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
    //logic to paint hexagon board
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
    //used in logic
    public int modifyi(int i, boolean isHalfway){
        if(isHalfway){
            i--;
        }
        else i++;
        return i;
    }




    //used for debug
    public void paintAtoms(double initialx, double initialy, Graphics g){
        Graphics2D g2d= (Graphics2D) g.create();

        double x, y;
        double w=50;

        g2d.setColor(Color.RED);

        ArrayList<Cell> cellArr=board.getAllCells();
        for(int i=0;i< cellArr.size();i++){
            if(cellArr.get(i).hasAtom()){
                y=initialy+(cellArr.get(i).getRow()-1)*YMOD+25.0;
                x=initialx+getXdiff(i);
                drawAtom(x, y, w, g2d);
                drawCircleOfInf(x, y, w, g2d);
            }
        }

        g2d.dispose();
    }
    //also used for debug
    private void drawCircleOfInf(double x, double y, double w, Graphics2D g2d) {
        g2d.draw(new Ellipse2D.Double(x-50, y-50, w+100, w+100));
    }
    //used for converting cell number to x coordinate position
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

    //pains atom
    public void drawAtom(double x, double y, double w, Graphics2D g2d){
        g2d.fill( new Ellipse2D.Double(x, y, w, w));
    }







    //used for debug
    //logic for printing rays
    public void paintRays(double initialx, double initialy, Graphics g) {
        ArrayList<ArrayList<Integer>> raysArray = board.getAllRays();
        Graphics2D g2d=(Graphics2D) g.create();
        g2d.setColor(Color.BLUE);
        double xf, yf, xl, yl;
        for(int i=lastRayPainted;i<raysArray.size();i++) {
            ArrayList<Integer> ray = raysArray.get(i);


            xf = initialx + getXdiff(ray.getFirst()) + 25;
            yf = initialy + (board.getAllCells().get(ray.getFirst()).getRow() - 1) * YMOD + 50.0;
            xl = initialx + getXdiff(ray.getLast()) + 25;
            yl = initialy + (board.getAllCells().get(ray.getLast()).getRow() - 1) * YMOD + 50.0;


            drawRay(xf, yf, xl, yl, g2d);
        }
        lastRayPainted= raysArray.size();
        g2d.dispose();
    }
    //paint individual ray
    public void drawRay(double xf, double yf, double xl, double yl, Graphics2D g2d){
        Path2D.Double path= new Path2D.Double();
        path.moveTo(xf, yf);
        path.lineTo(xl, yl);
        path.closePath();
        g2d.draw(path);
    }


    //paints each new set of ray markers
    public void paintRayMarkers(Graphics g){
        ArrayList<ArrayList<Integer>> raysArray = board.getAllRays();
        Graphics2D g2d=(Graphics2D) g.create();

        double xf, yf, xl, yl;

        boolean repaint=false;
        if(gameLastRayPainted==raysArray.size()){
            gameLastRayPainted=backup;
            rayMarkerSetsPrinted--;
            repaint=true;
        }
        else rayMarkersBeginnings.add(gameLastRayPainted);

        ArrayList<Integer> ray = raysArray.getLast();

        xf = initialXPos + getXdiff(ray.getFirst()) + 25;
        yf = initialYPos + (board.getAllCells().get(ray.getFirst()).getRow() - 1) * YMOD + 50.0;
        xl = initialXPos + getXdiff(ray.getLast()) + 25;
        yl = initialYPos + (board.getAllCells().get(ray.getLast()).getRow() - 1) * YMOD + 50.0;

        //ENTRY RAY LOCATION

        Color c1=markerColor(rayMarkerSetsPrinted);
        Color c2=c1;



        switch(getRayCellSide()){
            case 0:
                xf+=XMOD-10;
                yf-=50;break;
            case 1:
                xf+=XMOD+10;break;
            case 2:
                xf+=XMOD-10;
                yf+=50;break;
            case 3:
                xf-=XMOD-10;
                yf+=50;break;
            case 4:
                xf-=XMOD+10;break;
            default:
                xf-=XMOD-10;
                yf-=50;break;

        }




        //IF ITS ABSORBED OR REFLECTED
        if(raysArray.size()-gameLastRayPainted==1 && (!isOuter(ray.getLast()) || ray.getFirst()==ray.getLast())){
            warningShape((int) xf, (int) yf, g2d);
            if(!repaint)backup=gameLastRayPainted;
            gameLastRayPainted=raysArray.size();
            return;
        }

        //IF ITS SINGLE RAY IN AND OUT FINE
        if(isOuter(ray.getLast())){
            switch(oppositeSide(getRayCellSide())){
                case 0:
                    xl+=XMOD-10;
                    yl-=50;break;
                case 1:
                    xl+=XMOD+10;break;
                case 2:
                    xl+=XMOD-10;
                    yl+=50;break;
                case 3:
                    xl-=XMOD-10;
                    yl+=50;break;
                case 4:
                    xl-=XMOD+10;break;
                default:
                    xl-=XMOD-10;
                    yl-=50;break;

            }

        }
        double xEnterPos=xf, yEnterPos=yf, xExitPos=xl;


        if(raysArray.size()-gameLastRayPainted!=1){
            ray=raysArray.get(gameLastRayPainted);



            if(!isOuter(ray.getLast())){
                c2=Color.WHITE;
            }

            xf = initialXPos + getXdiff(ray.getFirst()) + 25;
            yf = initialYPos + (board.getAllCells().get(ray.getFirst()).getRow() - 1) * YMOD + 50.0;
            xl = initialXPos + getXdiff(ray.getLast()) + 25;
            yl = initialYPos + (board.getAllCells().get(ray.getLast()).getRow() - 1) * YMOD + 50.0;

            if(xf==xl)xf=xExitPos;



            switch(calcExitRayCellSide(xf, yf, xl, yl)){
                case 0:
                    xl+=XMOD-10;
                    yl-=50;break;
                case 1:
                    xl+=XMOD+10;break;
                case 2:
                    xl+=XMOD-10;
                    yl+=50;break;
                case 3:
                    xl-=XMOD-10;
                    yl+=50;break;
                case 4:
                    xl-=XMOD+10;break;
                default:
                    xl-=XMOD-10;
                    yl-=50;break;

            }

        }
        if(rayMarkerSetsPrinted%3==0){
            xShape((int)xEnterPos, (int)yEnterPos, c1, g2d);
            xShape((int)xl, (int)yl, c2, g2d);
        }
        else if(rayMarkerSetsPrinted%3==1){
            squareShape((int)xEnterPos, (int)yEnterPos, c1, g2d);
            squareShape((int)xl, (int)yl, c2, g2d);
        }
        else{
            circleShape((int)xEnterPos, (int)yEnterPos, c1, g2d);
            circleShape((int)xl, (int)yl, c2, g2d);
        }

        rayMarkerSetsPrinted++;

        if(!repaint){
            backup=gameLastRayPainted;
        }
        gameLastRayPainted=raysArray.size();

        g2d.dispose();
    }


    //makes it easier to set initial ray marker
    public void setRayCellSide(int side){
        rayCellSide=side;
    }
    public int getRayCellSide(){
        return this.rayCellSide;
    }

    //calculates the exit ray cell side based on the ray direction
    private int calcExitRayCellSide(double xf, double yf, double xl, double yl){
        if(xf<xl && yf>yl)return 0;
        if(xf<xl && yf==yl)return 1;
        if(xf<xl && yf<yl)return 2;
        if(xf>xl && yf<yl)return 3;
        if(xf>xl && yf==yl)return 4;
        if(xf>xl && yf>yl)return 5;
        else throw new IllegalArgumentException("Error"+xf+" "+yf+" "+xl+" "+yl);
    }
    //checks if the location of the ray is an outer cell
    private boolean isOuter(int ind){
        int[] validCells = {0,1,2,3,4,5,10,11,17,18,25,26,34,35,42,43,49,50,55,56,57,58,59,60};
        for (int i=0;i<validCells.length;i++) {
            if (ind == validCells[i]) return true;
        }
        return false;
    }
    //returns opposite side of hexagon
    private int oppositeSide(int side){
        if(side==0)return 3;
        if(side==1)return 4;
        if(side==2)return 5;
        if(side==3)return 0;
        if(side==4)return 1;
        else return 2;
    }

    //paints an x shaped ray marker
    private void xShape(int x, int y, Color c, Graphics2D g2d){
        if(c==Color.WHITE)return;
        g2d.setColor(c);
        Path2D path= new Path2D.Double();
        g2d.setStroke(new BasicStroke(3f));
        path.moveTo(x-5, y-5);
        path.lineTo(x+5, y+5);
        path.moveTo(x+5, y-5);
        path.lineTo(x-5, y+5);
        path.closePath();
        g2d.draw(path);
    }
    //paints a square shaped ray marker
    private void squareShape(int x, int y, Color c, Graphics2D g2d){
        if(c==Color.WHITE)return;
        g2d.setColor(c);
        Path2D path= new Path2D.Double();
        g2d.setStroke(new BasicStroke(3f));
        path.moveTo(x-5, y-5);
        path.lineTo(x+5, y-5);
        path.lineTo(x+5, y+5);
        path.lineTo(x-5, y+5);
        path.lineTo(x-5, y-5);
        path.closePath();
        g2d.draw(path);

    }
    //paints a circle shaped ray marker
    private void circleShape(int x, int y, Color c, Graphics2D g2d){
        if(c==Color.WHITE)return;
        g2d.setColor(c);
        g2d.setStroke(new BasicStroke(3f));
        g2d.fill( new Ellipse2D.Double(x-5, y-5, 15, 15));


    }
    //paints a warning shaped ray marker
    private void warningShape(int x, int y, Graphics2D g2d){
        g2d.setColor(Color.RED);
        Path2D path= new Path2D.Double();

        path.moveTo(x, y-10);
        path.lineTo(x+6, y+4);
        path.lineTo(x-6, y+4);
        path.lineTo(x, y-10);
        path.moveTo(x, y-7);
        path.lineTo(x, y+1);
        path.moveTo(x, y+2);
        path.lineTo(x, y+2);
        path.closePath();
        g2d.draw(path);

    }

    //chooses a ray marker colour based on how many ray marker sets
    private Color markerColor(int a){
        Color[] colours={Color.BLUE, Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.DARK_GRAY};
        return colours[a%5];
    }


    public void afterGamePaintAtom(){
        isGameOver=true;
        repaint();
    }







    public void setIsFirstTime(){
        isFirstTime=false;
    }




    //draws the number of each cell on the board
    public void cellNumbers(Graphics g){
        int x=(int)initialXPos;
        int y=(int)initialYPos;


        double xmod=XMOD, ymod=YMOD;
        boolean isHalfway=false;
        int i=5, k=0;
        while (i!=4) {

            int currentX=x;
            for(int j=0;j<i;j++){
                g.drawString(Integer.toString(k), currentX, y+50);
                currentX+=2*XMOD;
                k++;
            }

            if(i==9){
                isHalfway=true;
                xmod*=-1;
            }
            x-=xmod;
            y+=ymod;

            i = modifyi(i, isHalfway);
        }
    }





}

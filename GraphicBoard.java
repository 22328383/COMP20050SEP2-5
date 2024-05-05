import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.geom.*;
import java.util.ArrayList;

/**
 * The graphical representation of the Black Box+ game.

 */

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

    /**
     * The constructor for the graphic board.
     *
     *
     * @param board the board logic for the game is stored here
     */

    public GraphicBoard(Board board){
        this.board=board;
        setSize(1000,1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setBackground(Color.BLACK);
        setTitle("Black Box+");
        setIconImage(new ImageIcon("images/blackbox+icon.png").getImage());
        setAlwaysOnTop(true);
    }

    /**
     * All the separate components of the graphic board are painted from here.
     * It paints:
     *              the example cell with the labeled sides,
     *              the cells making up the board,
     *              the cell number of each cell,
     *              the ray markers,
     *              the atoms once the game is over
     * @param g the specified Graphics window
     */
    public void paint(Graphics g){
        double initialx=300.0, initialy=200.0;
        g.setColor(Color.YELLOW);

        paintExampleHexagon(g);
        paintHexagons(initialx, initialy, g);
        cellNumbers(g);

        if(!isFirstTime) {
            paintRayMarkers(g);
        }
        if(isGameOver){
            paintAtoms(initialx, initialy, g);
        }
    }

    /**
     * Paints the example cell in the top corner of the window to show the user how the sides are labelled
     * @param g the specified Graphics window
     */
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

    /**
     * Calculates the x-coordinates of all the points of the cell
     * @param top the x-coordinate of the top point of the cell
     * @return array containing the x-coordinates for all the points of the cell
     */
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

    /**
     * Calculates the y-coordinates of all the points of the cell
     * @param top the y-coordinate of the top point of the cell
     * @return array containing the y-coordinates for all the points of the cell
     */
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

    /**
     * Draws a row of cells on to the board
     * @param initialxTop The x-coordinate of the top point of the first cell in the row
     * @param initialyTop The y-coordinate of the top point of the first cell in the row
     * @param numHex The number of cells to draw on to the board
     * @param g the specified Graphics window
     */
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
            g2d.setStroke(new BasicStroke(3f));
            g2d.draw(poly);
            g2d.dispose();
        }
    }

    /**
     * Calculates the coordinates for the leftmost cells of the board and draws the board row by row
     * @param initialx The initial x-coordinate to place the board
     * @param initaly The inital y-coordinate to place the board
     * @param g the specified Graphics window
     */
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

    /**
     * Calculates how many cells to draw per row
     * @param i Value that determines how many cells are drawn per row
     * @param isHalfway Boolean value representing if half the board has been drawn already
     * @return A modified i value
     */
    //used in logic
    public int modifyi(int i, boolean isHalfway){
        if(isHalfway){
            i--;
        }
        else i++;
        return i;
    }


    /**
     * Draws atoms on to the board
     * @param initialx The initial x-coordinate of the top of the board
     * @param initialy The initial y-coordinate of the top of the board
     * @param g the specified Graphics window
     */
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

    /**
     * Draws the circle of influence of an atom
     * @param x The x-coordinate of the upper left corner of the framing square of which the circle is placed
     * @param y The y-coordinate of the upper left corner of the framing square of which the circle is placed
     * @param w The width and height of the framing square
     * @param g2d the specified Graphics2D window
     */
    private void drawCircleOfInf(double x, double y, double w, Graphics2D g2d) {
        g2d.draw(new Ellipse2D.Double(x-50, y-50, w+100, w+100));
    }

    /**
     * Calculates the x-coordinate of the centre of a given cell
     * @param i The cell number [0,1,2,...,59,60]
     * @return the x-coordinate
     */
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

    /**
     * Draws an atom
     * @param x The x-coordinate of the upper left corner of the framing square of which the atom is placed
     * @param y The y-coordinate of the upper left corner of the framing square of which the atom is placed
     * @param w The width and height of the framing square
     * @param g2d the specified Graphics2D window
     */
    //pains atom
    public void drawAtom(double x, double y, double w, Graphics2D g2d){
        g2d.fill( new Ellipse2D.Double(x, y, w, w));
    }


    /**
     * Calculates the coordinates to draw the rays
     * @param initialx The initial x-coordinate of the top of the board
     * @param initialy The initial y-coordinate of the top of the board
     * @param g the specified Graphics window
     */
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

    /**
     * Draws a single ray segment
     * @param xf The x-coordinate for the start of the ray
     * @param yf The y-coordinate for the start of the ray
     * @param xl The x-coordinate for the end of the ray
     * @param yl The y-coordinate for the end of the ray
     * @param g2d The specified Graphics window
     */
    //paint individual ray
    public void drawRay(double xf, double yf, double xl, double yl, Graphics2D g2d){
        Path2D.Double path= new Path2D.Double();
        path.moveTo(xf, yf);
        path.lineTo(xl, yl);
        path.closePath();
        g2d.draw(path);
    }

    /**
     * Draws the ray marker(s) for the most recent rays entered by the experimenter
     *
     *
     * @param g The specified Graphics window
     */
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

    /**
     * Sets the cell side at which the ray initially entered
     * @param side The cell side at which the initial ray has entered
     */
    //makes it easier to set initial ray marker
    public void setRayCellSide(int side){
        rayCellSide=side;
    }

    /**
     * Gets the cell side at which the ray initially entered
     * @return the cell side at which the initial ray has entered
     */
    public int getRayCellSide(){
        return this.rayCellSide;
    }

    /**
     * Calculates the cell side at which the ray exited
     * @param xf The x-coordinate for the start of the ray
     * @param yf The y-coordinate for the start of the ray
     * @param xl The x-coordinate for the end of the ray
     * @param yl The y-coordinate for the end of the ray
     * @return the cell side at which the ray exited
     */
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

    /**
     * Checks if the given cell number is an outer cell of the board
     * @param ind The cell number
     * @return true if the cell is an outer cell; false if not
     */
    //checks if the location of the ray is an outer cell
    private boolean isOuter(int ind){
        int[] validCells = {0,1,2,3,4,5,10,11,17,18,25,26,34,35,42,43,49,50,55,56,57,58,59,60};
        for (int i=0;i<validCells.length;i++) {
            if (ind == validCells[i]) return true;
        }
        return false;
    }

    /**
     * Gets the opposite side of a cell from a given cell side
     * @param side The side of which we want to get the opposite of
     * @return the opposite side of the given side
     */
    //returns opposite side of hexagon
    private int oppositeSide(int side){
        if(side==0)return 3;
        if(side==1)return 4;
        if(side==2)return 5;
        if(side==3)return 0;
        if(side==4)return 1;
        else return 2;
    }

    /**
     * Draws an x-shape ray marker
     * @param x The x-coordinate of the centre of the ray marker
     * @param y The y-coordinate of the centre of the ray marker
     * @param c The colour to draw the shape
     * @param g2d The specified Graphics2D window
     */
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
    /**
     * Draws a square shape ray marker
     * @param x The x-coordinate of the centre of the ray marker
     * @param y The y-coordinate of the centre of the ray marker
     * @param c The colour to draw the shape
     * @param g2d The specified Graphics2D window
     */
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
    /**
     * Draws a circle shape ray marker
     * @param x The x-coordinate of the centre of the ray marker
     * @param y The y-coordinate of the centre of the ray marker
     * @param c The colour to draw the shape
     * @param g2d The specified Graphics2D window
     */
    //paints a circle shaped ray marker
    private void circleShape(int x, int y, Color c, Graphics2D g2d){
        if(c==Color.WHITE)return;
        g2d.setColor(c);
        g2d.setStroke(new BasicStroke(3f));
        g2d.fill( new Ellipse2D.Double(x-5, y-5, 15, 15));


    }

    /**
     * Draws a warning shape ray marker when the ray marker is either absorbed or reflected back into the side where it entered
     * @param x The x-coordinate of the centre of the ray marker
     * @param y The y-coordinate of the centre of the ray marker
     * @param g2d The specified Graphics2D window
     */
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

    /**
     * Gets the colour of which to draw the ray marker
     * @param a Number which decides what colour to draw the ray marker
     * @return the colour to draw the ray marker
     */
    //chooses a ray marker colour based on how many ray marker sets
    private Color markerColor(int a){
        Color[] colours={Color.BLUE, Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.LIGHT_GRAY, Color.CYAN, Color.PINK};
        return colours[a%7];
    }

    /**
     * Makes it so that the atoms are painted after the game is over
     */
    public void afterGamePaintAtom(){
        isGameOver=true;
        repaint();
    }


    /**
     * This variable guards the ray markers being painted until one has been entered
     */
    public void setIsFirstTime(){
        isFirstTime=false;
    }


    /**
     * Draws the cell numbers in each cell
     * @param g the specified Graphic window
     */
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

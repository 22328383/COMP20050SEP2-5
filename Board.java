import java.util.ArrayList;


public class Board {
    static final int MAXCELLS = 61;
    static final int[] ROWCELLCNT = genCellCount(9);
    private ArrayList<Cell> allCells = new ArrayList<Cell>();

    public Board() {
        for(int i = 0; i < MAXCELLS; i++) {

            //generate all sides for a hexagon
            ArrayList<Side> allSides = new ArrayList<Side>();
            for(int j = 0; j < Cell.MAXSIDES; j++) {
                Side newSide = new Side(j+1, null);
                allSides.add(newSide);
            }

            Cell newCell = new Cell(allSides, false, findRow(i ,ROWCELLCNT), null);
            for(int j = 0; j < Cell.MAXSIDES; j++) {
                newCell.getSides().get(j).setOwner(newCell);
                //add current cell as owner of side
            }
            this.allCells.add(newCell);
        }

    }

    public static int[] genCellCount(int numRows) {
        int[] cellCounts = new int[numRows];
        for (int i = 0; i < (numRows + 1) / 2; i++) {
            cellCounts[i] = i + 5;
        }

        for (int i = (numRows + 1) / 2; i < numRows; i++) {
            cellCounts[i] = cellCounts[numRows - i - 1];
        }

        return cellCounts;
    }


    public static int findRow(int cellNum, int[] cellCounts) {
        int row = 0;
        int totalCells = 0;

        for (int count : cellCounts) {
            row++;
            totalCells += count;
            if (totalCells >= (cellNum+1)) {
                break;
            }
        }

        return row;
    }

    public void drawBoard() {
        int curRow = 1;
        for(int i = 0; i < allCells.size(); i++) {
            if(curRow == allCells.get(i).getRow()) {
                System.out.print(i + " ");
            } else {
                curRow++;
                System.out.println();
                System.out.print(i + " ");
            }
        }
    }

}



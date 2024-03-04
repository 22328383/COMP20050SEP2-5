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

    public static Board newBoard() {
        Board newBoard = new Board();
        return newBoard;
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

    public int[] getNeighbors(int index) {
        int[] atomPos = getCoordinates(index);
        int x = atomPos[0];
        int y = atomPos[1];

        int[] neighborsArray = new int[6];

        // Top right neighbor
        if (y == 0) {
            neighborsArray[0] = -1;
        }
        if (y == 1 || y == 8) {
            neighborsArray[0] = index-5;
        }
        if (y == 2 || y == 7) {
            neighborsArray[0] = index-6;
        }
        if (y == 3 || y == 6) {
            neighborsArray[0] = index-7;
        }
        if (y == 4 || y == 5) {
            neighborsArray[0] = index-8;
        }
        int j = 0;
        for (int i = 0; i < y; i++) {
            j += ROWCELLCNT[i];
        }
        if(neighborsArray[0] == j) {
            neighborsArray[0] = -1;
        }

        // Right neighbor
        if ((x + 1) < ROWCELLCNT[y]) {
            neighborsArray[1] = index + 1;
        } else {
            neighborsArray[1] = -1;
        }

        // Bottom right neighbor
        if (y == 0 || y == 7) {
            neighborsArray[2] = index+6;
        }
        if (y == 1 || y == 6) {
            neighborsArray[2] = index+7;
        }
        if (y == 2 || y == 5) {
            neighborsArray[2] = index+8;
        }
        if (y == 3 || y == 4) {
            neighborsArray[2] = index+9;
        }
        if (y == 8) {
            neighborsArray[2] = -1;
        } else {
            j = 0;
            for (int i = 0; i <= y + 1; i++) {
                j += ROWCELLCNT[i];
            }
            if (neighborsArray[2] == j) {
                neighborsArray[2] = -1;
            }
        }

        // Bottom left neighbor
        if (y == 0 || y == 7) {
            neighborsArray[3] = index+5;
        }
        if (y == 1 || y == 6) {
            neighborsArray[3] = index+6;
        }
        if (y == 2 || y == 5) {
            neighborsArray[3] = index+7;
        }
        if (y == 3 || y == 4) {
            neighborsArray[3] = index+8;
        }
        if (y == 8) {
            neighborsArray[3] = -1;
        } else {
            j = 0;
            for (int i = 0; i < y; i++) {
                j += ROWCELLCNT[i];
            }
            j += ROWCELLCNT[y] - 1;
            if (neighborsArray[3] == j) {
                neighborsArray[3] = -1;
            }
        }

        // Left neighbor
        if ((x - 1) >= 0) {
            neighborsArray[4] = index - 1;
        } else {
            neighborsArray[4] = -1;
        }

        // Top left neighbor
        if (y == 0) {
            neighborsArray[5] = -1;
        }
        if (y == 1 || y == 8) {
            neighborsArray[5] = index-6;
        }
        if (y == 2 || y == 7) {
            neighborsArray[5] = index-7;
        }
        if (y == 3 || y == 6) {
            neighborsArray[5] = index-8;
        }
        if (y == 4 || y == 5) {
            neighborsArray[5] = index-9;
        }
        if(y > 0) {
            j = 0;
            for (int i = 0; i < y - 2; i++) {
                j += ROWCELLCNT[i];
            }
            if(neighborsArray[5] == j) {
                neighborsArray[5] = -1;
            }
        }
        if(x == 0 && y == 1) {
            neighborsArray[5] = -1;
        }

        return neighborsArray;
    }


    public int[] getCoordinates(int index) {
        int x = 0;
        int y = allCells.get(index).getRow() - 1;

        int totalCells = 0;
        for (int i = 0; i < y; i++) {
            totalCells += ROWCELLCNT[i];
        }
        x = index - totalCells;

        int[] coordinates = {x, y};
        return coordinates;
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



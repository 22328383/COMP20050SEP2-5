import java.util.ArrayList;
import java.util.Arrays;


public class Board {
    static final int MAXCELLS = 61;
    static final int[] ROWCELLCNT = genCellCount(9);
    private ArrayList<Cell> allCells = new ArrayList<Cell>();
    ArrayList<ArrayList<Integer>> allRays = new ArrayList<ArrayList<Integer>>();

    public ArrayList<ArrayList<Integer>> getAllRays() {
        return allRays;
    }

    public Board() {
        for(int i = 0; i < MAXCELLS; i++) {

            //generate all sides for a hexagon
            ArrayList<Side> allSides = new ArrayList<Side>();
            for(int j = 0; j < Cell.MAXSIDES; j++) {
                Side newSide = new Side(j+1, null);
                allSides.add(newSide);
            }

            Cell newCell = new Cell(allSides, false, findRow(i ,ROWCELLCNT));
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
    public ArrayList<Cell> getAllCells() {
        return allCells;
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

    public void setAtom(int idx) {
        int[] neighbors = getNeighbors(idx);
        allCells.get(idx).setHasAtom(true);
        for(int i = 0; i < 6; i++) {
            if(neighbors[i] == -1) {
                continue;
            }
            allCells.get(neighbors[i]).getAtomSides().set((i), 1);
        }
    }

    private int topRight(int index, int x, int y) {
        int result = 0;
        if (y == 0) {
            result = -1;
        }
        if (y == 1 || y == 8) {
            result = index-5;
        }
        if (y == 2 || y == 7) {
            result = index-6;
        }
        if (y == 3 || y == 6) {
            result = index-7;
        }
        if (y == 4 || y == 5) {
            result = index-8;
        }
        int j = 0;
        for (int i = 0; i < y; i++) {
            j += ROWCELLCNT[i];
        }
        if(result == j) {
            result = -1;
        }
        return result;
    }

    private int right(int index, int x, int y) {
        int result;
        if ((x + 1) < ROWCELLCNT[y]) {
            result = index + 1;
        } else {
            result = -1;
        }
        return result;
    }

    private int botRight(int index, int x, int y) {
        int result = 0;
        if (y == 0 || y == 7) {
            result = index+6;
        }
        if (y == 1 || y == 6) {
            result = index+7;
        }
        if (y == 2 || y == 5) {
            result = index+8;
        }
        if (y == 3 || y == 4) {
            result = index+9;
        }
        if (y == 8) {
            result = -1;
        } else {
            int j = 0;
            for (int i = 0; i <= y + 1; i++) {
                j += ROWCELLCNT[i];
            }
            if (result == j) {
                result = -1;
            }
        }
        return result;
    }

    private int botLeft(int index, int x, int y) {
        int result = 0;
        if (y == 0 || y == 7) {
            result = index+5;
        }
        if (y == 1 || y == 6) {
            result = index+6;
        }
        if (y == 2 || y == 5) {
            result = index+7;
        }
        if (y == 3 || y == 4) {
            result = index+8;
        }
        if (y == 8) {
            result = -1;
        } else {
            int j = 0;
            for (int i = 0; i < y; i++) {
                j += ROWCELLCNT[i];
            }
            j += ROWCELLCNT[y] - 1;
            if (result == j) {
                result = -1;
            }
        }
        return result;
    }

    private int left(int index, int x, int y) {
        int result;
        if ((x - 1) >= 0) {
            result = index - 1;
        } else {
            result = -1;
        }
        return result;
    }

    private int topLeft(int index, int x, int y) {
        int result = 0;
        if (y == 0) {
            result = -1;
        }
        if (y == 1 || y == 8) {
            result = index-6;
        }
        if (y == 2 || y == 7) {
            result = index-7;
        }
        if (y == 3 || y == 6) {
            result = index-8;
        }
        if (y == 4 || y == 5) {
            result = index-9;
        }
        if(y > 0) {
            int j = 0;
            for(int i = 0; i < y - 1; i++) {
                j += ROWCELLCNT[i];
            }
            if(result == j - 1) {
                result = -1;
            }
        }
        if(x == 0 && y == 1) {
            result = -1;
        }
        return result;
    }

    public int[] getNeighbors(int index) {
        int[] atomPos = getCoordinates(index);
        int x = atomPos[0];
        int y = atomPos[1];

        int[] neighborsArray = new int[6];

        // Top right neighbor
        neighborsArray[0] = topRight(index, x, y);

        // Right neighbor
        neighborsArray[1] = right(index, x, y);

        // Bottom right neighbor
        neighborsArray[2] = botRight(index, x, y);

        // Bottom left neighbor
        neighborsArray[3] = botLeft(index, x, y);

        // Left neighbor
        neighborsArray[4] = left(index, x, y);

        // Top left neighbor
        neighborsArray[5] = topLeft(index, x, y);

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

    private int applyPhys(int idx, int enteringSide) {
        int opposite = allCells.get(idx).atomSideToClockwise().get((enteringSide+3)%6);
        int borderCnt = 0;
        for(int i = 0; i < allCells.get(idx).getAtomSides().size(); i++) {
            if(allCells.get(idx).atomSideToClockwise().get(i) > 0) {
                borderCnt += 1;
            }
        }
        if(borderCnt >= 3) {
            if((allCells.get(idx).atomSideToClockwise().get(enteringSide)) == 1) {
                return 0;
            }
            addRay(idx, (enteringSide+3)%6);
            return -1;
        } else if (borderCnt == 2) {

        } else if (borderCnt == 1) {
            if(opposite > 0) {
                return -1;
            }
        } else if (borderCnt == 0) {
        }
        return 0;
    }

    public void addRay(int idx, int side) {
        int[] pos = getCoordinates(idx);
        int x = pos[0];
        int y = pos[1];
        ArrayList<Integer> newRay = new ArrayList<Integer>();

        if(side == 0) {
            int curIdx = idx;
            while(botLeft(curIdx, x, y) != -1) {
                int terminate = applyPhys(curIdx, side);
                if(terminate < 0) {
                    break;
                }
                newRay.add(curIdx);
                curIdx = botLeft(curIdx, x, y);
                pos = getCoordinates(curIdx);
                x = pos[0];
                y = pos[1];
            }
            newRay.add(curIdx);
        }
        if(side == 1) {
            int curIdx = idx;
            while(left(curIdx, x, y) != -1) {
                int terminate = applyPhys(curIdx, side);
                if(terminate < 0) {
                    break;
                }
                newRay.add(curIdx);
                curIdx = left(curIdx, x, y);
                pos = getCoordinates(curIdx);
                x = pos[0];
                y = pos[1];
            }
            newRay.add(curIdx);
        }
        if(side == 2) {
            int curIdx = idx;
            while(topLeft(curIdx, x, y) != -1) {
                int terminate = applyPhys(curIdx, side);
                if(terminate < 0) {
                    break;
                }
                newRay.add(curIdx);
                curIdx = topLeft(curIdx, x, y);
                pos = getCoordinates(curIdx);
                x = pos[0];
                y = pos[1];
            }
            newRay.add(curIdx);
        }
        if(side == 3) {
            int curIdx = idx;
            while(topRight(curIdx, x, y) != -1) {
                int terminate = applyPhys(curIdx, side);
                if(terminate < 0) {
                    break;
                }
                newRay.add(curIdx);
                curIdx = topRight(curIdx, x, y);
                pos = getCoordinates(curIdx);
                x = pos[0];
                y = pos[1];
            }
            newRay.add(curIdx);
        }
        if(side == 4) {
            int curIdx = idx;
            while(right(curIdx, x, y) != -1) {
                int terminate = applyPhys(curIdx, side);
                if(terminate < 0) {
                    break;
                }
                newRay.add(curIdx);
                curIdx = right(curIdx, x, y);
                pos = getCoordinates(curIdx);
                x = pos[0];
                y = pos[1];
            }
            newRay.add(curIdx);
        }
        if(side == 5) {
            int curIdx = idx;
            while(botRight(curIdx, x, y) != -1) {
                int terminate = applyPhys(curIdx, side);
                if(terminate < 0) {
                    break;
                }
                newRay.add(curIdx);
                curIdx = botRight(curIdx, x, y);
                pos = getCoordinates(curIdx);
                x = pos[0];
                y = pos[1];
            }
            newRay.add(curIdx);
        }
        allRays.add(newRay);
    }

    public void drawBoard() {
        int curRow = 1;
        for(int i = 0; i < allCells.size(); i++) {
            if(curRow == allCells.get(i).getRow()) {
                boolean hasNeighbor = false;
                if(allCells.get(i).hasAtom()) {
                    System.out.print(i + "*" + " ");
                    hasNeighbor = true;
                }
                for(int j = 0; j < 6; j++) {
                    if(allCells.get(i).getAtomSides().get(j) != -1) {
                        System.out.print(i + "(" + j + ") ");
                        hasNeighbor = true;
                    }
                }
                if(!hasNeighbor) {
                    System.out.print(i + " ");
                }
            } else {
                curRow++;
                System.out.println();
                System.out.print(i + " ");
            }
        }

        System.out.println("");
        for(int i = 0; i < allRays.size(); i++) {
            System.out.println("");
            System.out.println("Ray :" + allRays.get(i));
            for(int j = 0; j < allRays.get(i).size(); j++) {
                System.out.println(allRays.get(i).get(j) + " " + allCells.get(allRays.get(i).get(j)).atomSideToClockwise());
            }
        }
    }

}



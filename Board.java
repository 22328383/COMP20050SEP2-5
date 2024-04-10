import java.io.*;
import java.util.ArrayList;

public class Board implements Serializable {
    static final int MAXCELLS = 61;
    static final int[] ROWCELLCNT = genCellCount(9); // cell count for each row
    public Board.Scoreboard Scoreboard = new Scoreboard("", "");
    private ArrayList<Cell> allCells = new ArrayList<>();
    ArrayList<ArrayList<Integer>> allRays = new ArrayList<>();

    public ArrayList<ArrayList<Integer>> getAllRays() {
        return allRays;
    }

    // constructor
    public Board() {
        for(int i = 0; i < MAXCELLS; i++) {
            Cell newCell = new Cell(false, findRow(i ,ROWCELLCNT));
            this.allCells.add(newCell); // add new cell to list of all cells on board
        }
    }

    // method to create a new board
    public static Board newBoard() {
        Board newBoard = new Board();
        return newBoard;
    }

    //save state to file
    public void saveBoard() throws IOException {
        String filename = "save.ser";
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(this);
        }
    }

    //load state from file
    public static Board loadBoard() throws IOException, ClassNotFoundException {
        String filename = "save.ser";
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            return (Board) in.readObject();
        }
    }

    // get all cells on a board
    public ArrayList<Cell> getAllCells() {
        return allCells;
    }

    // method to generate cell count for each row
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

    // method to set an atom at specified index
    public void setAtom(int idx) {
        int[] neighbors = getNeighbors(idx); // get all neighbors of the cell at a specified index
        allCells.get(idx).setHasAtom(true); // set the specified cell to have an atom
        for(int i = 0; i < 6; i++) {
            if(neighbors[i] == -1) { // if absent, skip
                continue;
            }
            allCells.get(neighbors[i]).getAtomSides().set((i), 1);
        }
    }


    private int topRight(int index, int x, int y) {
        int result = 0;
        if (y == 0) {
            result = -1; // there is no top right
        }
        if (y == 1 || y == 8) { // row 1 or 8
            result = index-5;
        }
        if (y == 2 || y == 7) { // row 2 or 7
            result = index-6;
        }
        if (y == 3 || y == 6) { // row 3 or 6
            result = index-7;
        }
        if (y == 4 || y == 5) { // row 4 or 5
            result = index-8;
        }
        int j = 0;

        // exception case for first entry
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
        int opposite = allCells.get(idx).atomSideToClockwise().get( ((enteringSide+3)%6) );
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
            if((allCells.get(idx).atomSideToClockwise().get(((enteringSide+4)%6))) == 1) {
                if((allCells.get(idx).atomSideToClockwise().get(((enteringSide+2)%6))) == 1) {
                    addRay(idx, (enteringSide+3)%6);
                    return -1;
                } else if(opposite == 1) {
                    addRay(idx, (enteringSide+4)%6);
                    return -1;
                }
            } else if(allCells.get(idx).atomSideToClockwise().get(((enteringSide+2)%6)) == 1) {
                if((allCells.get(idx).atomSideToClockwise().get(((enteringSide+4)%6))) == 1) {
                    addRay(idx, (enteringSide+3)%6);
                    return -1;
                } else if(opposite == 1) {
                    addRay(idx, (enteringSide+2)%6);
                    return -1;
                }
            }
        } else if (borderCnt == 1) {
            int[] pos = getCoordinates(idx);
            int x = pos[0];
            int y = pos[1];
            if((enteringSide == 0)) {
                int testingIdx = topRight(idx, x, y);
                if(testingIdx < 0) {
                    return -1;
                }
            }
            if((enteringSide == 1)) {
                int testingIdx = right(idx, x, y);
                if(testingIdx < 0) {
                    return -1;
                }
            }
            if((enteringSide == 2)) {
                int testingIdx = botRight(idx, x, y);
                if(testingIdx < 0) {
                    return -1;
                }
            }
            if((enteringSide == 3)) {
                int testingIdx = botLeft(idx, x, y);
                if(testingIdx < 0) {
                    return -1;
                }
            }
            if((enteringSide == 4)) {
                int testingIdx = left(idx, x, y);
                if(testingIdx < 0) {
                    return -1;
                }
            }
            if((enteringSide == 5)) {
                int testingIdx = topLeft(idx, x, y);
                if(testingIdx < 0) {
                    return -1;
                }
            }
            if(opposite > 0) {
                return -1;
            } else if ((allCells.get(idx).atomSideToClockwise().get(((enteringSide+2)%6))) == 1) {
                addRay(idx, (enteringSide+1)%6);
                return -1;
            } else if ((allCells.get(idx).atomSideToClockwise().get(((enteringSide-2+6)%6))) == 1) {
                addRay(idx, (enteringSide-1+6)%6);
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

    public String drawBoard() {
        String output = "";

        int curRow = 1;
        for(int i = 0; i < allCells.size(); i++) {
            if(curRow == allCells.get(i).getRow()) {
                boolean hasNeighbor = false;
                if(allCells.get(i).hasAtom()) {
                    System.out.print(i + "*" + " ");
                    output = output + i + "*" + " ";
                    hasNeighbor = true;
                }
                for(int j = 0; j < 6; j++) {
                    if(allCells.get(i).getAtomSides().get(j) != -1) {
                        System.out.print(i + "(" + j + ") ");
                        output = output + i + "(" + j + ") ";
                        hasNeighbor = true;
                    }
                }
                if(!hasNeighbor) {
                    System.out.print(i + " ");
                    output = output + i + " ";
                }
            } else {
                curRow++;
                System.out.println();
                output = output + "\n";
                System.out.print(i + " ");
                output = output + i + " ";
            }
        }

        System.out.println("");
        output = output + "\n";
        for(int i = 0; i < allRays.size(); i++) {
            System.out.println("");
            output = output + "\n";
            System.out.println("Ray :" + allRays.get(i));
            output = output + "Ray :" + allRays.get(i);
            for(int j = 0; j < allRays.get(i).size(); j++) {
                System.out.println(allRays.get(i).get(j) + " " + allCells.get(allRays.get(i).get(j)).atomSideToClockwise());
                output = output + allRays.get(i).get(j) + " " + allCells.get(allRays.get(i).get(j)).atomSideToClockwise();
            }
        }

        return output;
    }


    public static class Scoreboard implements Serializable {
        private final String[] playerNames;
        private int[] scores;

        public Scoreboard(String p1Name, String p2Name) {
            playerNames = new String[] {p1Name, p2Name};
            scores = new int[2];
        }

        private void increaseScore(int playerIndex, int points) {
            scores[playerIndex] += points;
        }

        public void calculateScore(int playerIndex, int numRays, int wrongCnt) {
            int penalty = wrongCnt * 5;
            increaseScore(playerIndex , numRays);
            increaseScore(playerIndex , penalty);
        }

        public int getScore(int playerIndex) {
            return scores[playerIndex];
        }

        public String getPlayerName(int playerIndex) {
            return playerNames[playerIndex];
        }

        @Override
        public String toString() {
            return "Player 1 (" + playerNames[0] + ") Score: " + scores[0] + "\n" + "Player 2 (" + playerNames[1] + ") Score: " + scores[1];
        }
    }
}



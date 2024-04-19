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
        return new Board();
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

    // getter for all cells on a board
    public ArrayList<Cell> getAllCells() {
        return allCells;
    }

    // method to generate cell count for each row
    public static int[] genCellCount(int numRows) {
        if(numRows <= 0) {
            throw new IllegalArgumentException("number of rows must be positive");
        }

        int[] cellCounts = new int[numRows];
        for(int i = 0; i < (numRows + 1) / 2; i++) {
            cellCounts[i] = i + 5;
        }

        for(int i = (numRows + 1) / 2; i < numRows; i++) {
            cellCounts[i] = cellCounts[numRows - i - 1];
        }

        return cellCounts;
    }

    // method to set an atom at specified index
    public void setAtom(int idx) {
        checkIndex(idx);

        int[] neighbors = getNeighbors(idx); // get all neighbors of the cell at a specified index
        allCells.get(idx).setHasAtom(true); // set the specified cell to have an atom
        for(int i = 0; i < 6; i++) {
            if(neighbors[i] == -1) { // if absent, skip
                continue;
            }
            allCells.get(neighbors[i]).getAtomSides().set((i), 1);
        }
    }

    // calculates index of the top-right cell based on the current index (x, y)
    private int topRight(int idx, int x, int y) {
        checkIndex(idx);

        int result = 0;
        // pattern for top rights
        if(y == 0) {
            result = -1; // there is no top right
        }
        if(y == 1 || y == 8) { // row 1 or 8
            result = idx-5;
        }
        if(y == 2 || y == 7) { // row 2 or 7
            result = idx-6;
        }
        if(y == 3 || y == 6) { // row 3 or 6
            result = idx-7;
        }
        if(y == 4 || y == 5) { // row 4 or 5
            result = idx-8;
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

    // calculates index of the right cell based on the current index (x, y)
    private int right(int idx, int x, int y) {
        checkIndex(idx);

        int result;
        if((x + 1) < ROWCELLCNT[y]) {
            // just next one
            result = idx + 1;
        } else {
            // if it is on the edge, invalid
            result = -1;
        }
        return result;
    }

    // calculates index of the bottom-right cell based on the current index (x, y)
    private int botRight(int idx, int x, int y) {
        checkIndex(idx);

        int result = 0;
        // pattern for bot rights
        if(y == 0 || y == 7) {
            result = idx+6;
        }
        if(y == 1 || y == 6) {
            result = idx+7;
        }
        if(y == 2 || y == 5) {
            result = idx+8;
        }
        if(y == 3 || y == 4) {
            result = idx+9;
        }
        if(y == 8) {
            result = -1; // invalid index, no bottom-right neighbor
        } else {
            int j = 0;
            for(int i = 0; i <= y + 1; i++) {
                j += ROWCELLCNT[i];
            }
            if(result == j) {
                result = -1; // invalid index if it aligns with the start of the next row
            }
        }
        return result;
    }

    // calculates index of the bottom-left cell based on the current index (x, y)
    private int botLeft(int idx, int x, int y) {
        checkIndex(idx);

        int result = 0;
        // pattern for bot lefts
        if(y == 0 || y == 7) {
            result = idx+5;
        }
        if(y == 1 || y == 6) {
            result = idx+6;
        }
        if(y == 2 || y == 5) {
            result = idx+7;
        }
        if(y == 3 || y == 4) {
            result = idx+8;
        }
        if(y == 8) {
            result = -1; // invalid index, no bottom-left neighbor
        } else {
            int j = 0;
            for(int i = 0; i < y; i++) {
                j += ROWCELLCNT[i];
            }
            j += ROWCELLCNT[y] - 1;
            if(result == j) {
                // invalid index if it aligns with the start of the next row
                result = -1;
            }
        }
        return result;
    }

    // calculates index of the left cell based on the current index (x, y)
    private int left(int idx, int x, int y) {
        checkIndex(idx);

        int result;
        if((x - 1) >= 0) {
            // previous one
            result = idx - 1;
        } else {
            // cant be left when at edge
            result = -1;
        }
        return result;
    }

    // calculates index of the top-left cell based on the current index (x, y)
    private int topLeft(int idx, int x, int y) {
        checkIndex(idx);

        int result = 0;
        if(y == 0) {
            // can't have top left when you're the first
            result = -1;
        }
        // pattern for bot lefts
        if(y == 1 || y == 8) {
            result = idx-6;
        }
        if(y == 2 || y == 7) {
            result = idx-7;
        }
        if(y == 3 || y == 6) {
            result = idx-8;
        }
        if(y == 4 || y == 5) {
            result = idx-9;
        }
        if(y > 0) {
            int j = 0;
            for(int i = 0; i < y - 1; i++) {
                j += ROWCELLCNT[i];
            }
            if(result == j - 1) {
                // invalid index if it aligns with the start of the next row
                result = -1;
            }
        }
        if(x == 0 && y == 1) {
            // super specific case
            result = -1;
        }
        return result;
    }

    // retrieves the indexes of the neighboring cells for a given cell index on a grid.
    public int[] getNeighbors(int idx) {
        checkIndex(idx);

        // convert index to co-ord system
        int[] atomPos = getCoordinates(idx);
        int x = atomPos[0];
        int y = atomPos[1];

        int[] neighborsArray = new int[6];

        // top right neighbor
        neighborsArray[0] = topRight(idx, x, y);

        // right neighbor
        neighborsArray[1] = right(idx, x, y);

        //bottom right neighbor
        neighborsArray[2] = botRight(idx, x, y);

        // bottom left neighbor
        neighborsArray[3] = botLeft(idx, x, y);

        // left neighbor
        neighborsArray[4] = left(idx, x, y);

        // top left neighbor
        neighborsArray[5] = topLeft(idx, x, y);

        return neighborsArray;
    }

    // converts a cells index to a co-ord based system location
    public int[] getCoordinates(int idx) {
        checkIndex(idx);
        int x = 0;
        // 0 is start, not 1
        int y = allCells.get(idx).getRow() - 1;

        int totalCells = 0;

        // count cells until we hit our one
        for(int i = 0; i < y; i++) {
            totalCells += ROWCELLCNT[i];
        }

        x = idx - totalCells;

        return new int[]{x, y};
    }

    // finds the row number from a cells index
    public static int findRow(int idx, int[] cellCounts) {
        checkIndex(idx);

        int row = 0;
        int totalCells = 0;

        // increment total cell count until we hit our desired
        for(int count : cellCounts) {
            row++;
            totalCells += count;
            if(totalCells >= (idx+1)) {
                break;
            }
        }

        return row;
    }

    // determines the behaviour of a ray entering a cell
    private int applyPhys(int idx, int enteringSide) {
        checkIndex(idx);

        // shortcut instead of writing whole line
        int opposite = allCells.get(idx).atomSideToClockwise().get( ((enteringSide+3)%6) );
        int borderCnt = 0;

        // counts how many atoms are nearby location
        for(int i = 0; i < allCells.get(idx).getAtomSides().size(); i++) {
            if(allCells.get(idx).atomSideToClockwise().get(i) > 0) {
                borderCnt += 1;
            }
        }

        // return -1 means to terminate recursive nature of ray

        // 3+ around, 180 degrees
        if(borderCnt >= 3) {
            if((allCells.get(idx).atomSideToClockwise().get(enteringSide)) == 1) {
                return 0;
            }
            addRay(idx, (enteringSide+3)%6);
            return -1;
        } else if (borderCnt == 2) {
            // 2 around, either 180 or 60
            if((allCells.get(idx).atomSideToClockwise().get(((enteringSide+4)%6))) == 1) {
                if((allCells.get(idx).atomSideToClockwise().get(((enteringSide+2)%6))) == 1) {
                    // two adjacent atoms
                    addRay(idx, (enteringSide+3)%6);
                    return -1;
                } else if(opposite == 1) {
                    addRay(idx, (enteringSide+4)%6);
                    return -1;
                }
            } else if(allCells.get(idx).atomSideToClockwise().get(((enteringSide+2)%6)) == 1) {
                if((allCells.get(idx).atomSideToClockwise().get(((enteringSide+4)%6))) == 1) {
                    // two 'bridged' atoms
                    addRay(idx, (enteringSide+3)%6);
                    return -1;
                } else if(opposite == 1) {
                    addRay(idx, (enteringSide+2)%6);
                    return -1;
                }
            }
            // 1 around, 120 degree
        } else if (borderCnt == 1) {

            int[] pos = getCoordinates(idx);
            int x = pos[0];
            int y = pos[1];

            // if trying to start ray inside circle of influence
            if((enteringSide == 0)) {
                int testingIdx = topRight(idx, x, y);
                if(testingIdx < 0) {
                    if(allCells.get(idx).atomSideToClockwise().get(5) > 0) {
                        return -1;
                    }
                    if(opposite > 0) {
                        return -1;
                    } else if ((allCells.get(idx).atomSideToClockwise().get(((enteringSide+2)%6))) == 1) {
                        addRay(left(idx, x, y), (enteringSide+1)%6);
                        return -1;
                    } else if ((allCells.get(idx).atomSideToClockwise().get(((enteringSide-2+6)%6))) == 1) {
                        addRay(botRight(idx, x, y), (enteringSide-1+6)%6);
                        return -1;
                    }
                }
            }

            if((enteringSide == 1)) {
                int testingIdx = right(idx, x, y);
                if(testingIdx < 0) {
                    if(allCells.get(idx).atomSideToClockwise().get(2) > 0) {
                        return -1;
                    }
                    if(opposite > 0) {
                        return -1;
                    } else if ((allCells.get(idx).atomSideToClockwise().get(((enteringSide+2)%6))) == 1) {
                        addRay(topLeft(idx, x, y), (enteringSide+1)%6);
                        return -1;
                    } else if ((allCells.get(idx).atomSideToClockwise().get(((enteringSide-2+6)%6))) == 1) {
                        addRay(botLeft(idx, x, y), (enteringSide-1+6)%6);
                        return -1;
                    }
                }
            }
            if((enteringSide == 2)) {
                int testingIdx = botRight(idx, x, y);
                if(testingIdx < 0) {
                    if(allCells.get(idx).atomSideToClockwise().get(3) > 0) {
                        return -1;
                    }
                    if(opposite > 0) {
                        return -1;
                    } else if ((allCells.get(idx).atomSideToClockwise().get(((enteringSide+2)%6))) == 1) {
                        addRay(topRight(idx, x, y), (enteringSide+1)%6);
                        return -1;
                    } else if ((allCells.get(idx).atomSideToClockwise().get(((enteringSide-2+6)%6))) == 1) {
                        addRay(left(idx, x, y), (enteringSide-1+6)%6);
                        return -1;
                    }
                }
            }
            if((enteringSide == 3)) {
                int testingIdx = botLeft(idx, x, y);
                if(testingIdx < 0) {
                    if(allCells.get(idx).atomSideToClockwise().get(2) > 0) {
                        return -1;
                    }
                    if(opposite > 0) {
                        return -1;
                    } else if ((allCells.get(idx).atomSideToClockwise().get(((enteringSide+2)%6))) == 1) {
                        addRay(right(idx, x, y), (enteringSide+1)%6);

                        return -1;
                    } else if ((allCells.get(idx).atomSideToClockwise().get(((enteringSide-2+6)%6))) == 1) {
                        addRay(topLeft(idx, x, y), (enteringSide-1+6)%6);
                        return -1;
                    }
                }
            }
            if((enteringSide == 4)) {
                int testingIdx = left(idx, x, y);
                if(testingIdx < 0) {
                    if(allCells.get(idx).atomSideToClockwise().get(3) > 0) {
                        return -1;
                    }
                    if(opposite > 0) {
                        return -1;
                    } else if ((allCells.get(idx).atomSideToClockwise().get(((enteringSide+2)%6))) == 1) {
                        addRay(botRight(idx, x, y), (enteringSide+1)%6);
                        return -1;
                    } else if ((allCells.get(idx).atomSideToClockwise().get(((enteringSide-2+6)%6))) == 1) {
                        addRay(topRight(idx, x, y), (enteringSide-1+6)%6);
                        return -1;
                    }
                }
            }
            if((enteringSide == 5)) {
                int testingIdx = topLeft(idx, x, y);
                if(testingIdx < 0) {
                    if(allCells.get(idx).atomSideToClockwise().get(0) > 0) {
                        return -1;
                    }
                    if(opposite > 0) {
                        return -1;
                    } else if ((allCells.get(idx).atomSideToClockwise().get(((enteringSide+2)%6))) == 1) {
                        addRay(botLeft(idx, x, y), (enteringSide+1)%6);
                        return -1;
                    } else if ((allCells.get(idx).atomSideToClockwise().get(((enteringSide-2+6)%6))) == 1) {
                        addRay(right(idx, x, y), (enteringSide-1+6)%6);
                        return -1;
                    }
                }
            }

            if(opposite > 0) {
                // nothing behind you
                return -1;
            } else if ((allCells.get(idx).atomSideToClockwise().get(((enteringSide+2)%6))) == 1) {
                addRay(idx, (enteringSide+1)%6);
                return -1;
            } else if ((allCells.get(idx).atomSideToClockwise().get(((enteringSide-2+6)%6))) == 1) {
                addRay(idx, (enteringSide-1+6)%6);
                return -1;
            }
        }
        return 0;
    }

    // simulates ray propagation by recursively applying rules until termination
    public void addRay(int idx, int side) {
        checkIndex(idx);
        if(allCells.get(idx).hasAtom()) {
            ArrayList<Integer> newRay = new ArrayList<>();
            newRay.add(idx);
            allRays.add(newRay);
            return;
        }

        int[] pos = getCoordinates(idx);
        int x = pos[0];
        int y = pos[1];
        ArrayList<Integer> newRay = new ArrayList<>();

        if(side == 0) {
            // shoots a ray toward the bottom left
            int curIdx = idx;
            while(botLeft(curIdx, x, y) != -1) {
                // till we hit a roadblock
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
            // shoots a ray toward the left
            int curIdx = idx;
            while(left(curIdx, x, y) != -1) {
                // till we hit a roadblock
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
            // shoots a ray toward the top left
            int curIdx = idx;
            while(topLeft(curIdx, x, y) != -1) {
                // till we hit a roadblock
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
            // shoots a ray toward the top right
            int curIdx = idx;
            while(topRight(curIdx, x, y) != -1) {
                // till we hit a roadblock
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
            // shoots a ray toward the right
            int curIdx = idx;
            while(right(curIdx, x, y) != -1) {
                // till we hit a roadblock
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
            // shoots a ray toward the bottom right
            int curIdx = idx;
            while(botRight(curIdx, x, y) != -1) {
                // till we hit a roadblock
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

        // rays finally done, add it to list of all
        allRays.add(newRay);
    }

    // usually for debugging/testing, helper function that draws a version of a board in ASCII
    @Override
    public String toString() {

        String output = "";

        int curRow = 1;
        for(int i = 0; i < allCells.size(); i++) {
            if(curRow == allCells.get(i).getRow()) {
                boolean hasNeighbor = false;
                if(allCells.get(i).hasAtom()) {
                    output = output + i + "*" + " ";
                    hasNeighbor = true;
                }
                for(int j = 0; j < 6; j++) {
                    if(allCells.get(i).getAtomSides().get(j) != -1) {
                        output = output + i + "(" + j + ") ";
                        hasNeighbor = true;
                    }
                }
                if(!hasNeighbor) {
                    output = output + i + " ";
                }
            } else {
                curRow++;
                output = output + "\n";
                output = output + i + " ";
            }
        }

        output = output + "\n";
        for(int i = 0; i < allRays.size(); i++) {
            output = output + "\n";
            output = output + "Ray :" + allRays.get(i);
            for(int j = 0; j < allRays.get(i).size(); j++) {
                output = output + allRays.get(i).get(j) + " " + allCells.get(allRays.get(i).get(j)).atomSideToClockwise();
            }
        }

        return output;
    }

    private static void checkIndex(int idx) {
        if(idx < 0 || idx >= MAXCELLS) {
            throw new IllegalArgumentException("cell number must be within the range of 0 and " + (MAXCELLS - 1));
        }
    }


    public static class Scoreboard implements Serializable {
        // the scoreboard subclass, part of a given board
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



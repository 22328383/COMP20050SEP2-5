import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;


class BoardTest {

    @Test
    void genCellCount() {
        int[] expected9 = {5, 6, 7, 8, 9, 8, 7, 6, 5};
        assertArrayEquals(expected9, Board.genCellCount(9));

        int[] expected13 = {5, 6, 7, 8, 9, 10, 11, 10, 9, 8, 7, 6, 5};
        assertArrayEquals(expected13, Board.genCellCount(13));

        assertThrows(IllegalArgumentException.class, () -> {
            Board.genCellCount(0);
        });
    }

    @Test
    void findRow() {
        int[] nineRows = Board.genCellCount(9);
        assertEquals(1, Board.findRow(0, nineRows));
        assertEquals(1, Board.findRow(1, nineRows));
        assertEquals(2, Board.findRow(6, nineRows));
        assertEquals(4, Board.findRow(20, nineRows));
        assertEquals(5, Board.findRow(29, nineRows));
        assertEquals(7, Board.findRow(46, nineRows));
        assertEquals(9, Board.findRow(56, nineRows));
        assertEquals(9, Board.findRow(57, nineRows));

        assertThrows(IllegalArgumentException.class, () -> {
            Board.findRow(-1, nineRows);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Board.findRow(61, nineRows);
        });
    }

    @Test
    void toStringTestDemo1() {
        Board newBoard = Board.newBoard();
        // set up of board state for demo 1 (from rulebook)
        newBoard.setAtom(12);
        newBoard.setAtom(21);
        newBoard.setAtom(22);
        newBoard.setAtom(25);
        newBoard.setAtom(28);
        newBoard.setAtom(29);

        newBoard.addRay(4, 1);
        newBoard.addRay(49, 2);
        newBoard.addRay(17, 1);
        newBoard.addRay(18, 4);
        newBoard.addRay(26, 4);
        newBoard.addRay(58, 2);
        newBoard.addRay(60, 2);

        String expectedOutput = "0 1 2 3 4 \n" +
                "5 6(0) 7 8 9 10 \n" +
                "11 12* 13(1) 13(5) 14(0) 14(5) 15(0) 16 17(5) \n" +
                "18 19(3) 19(5) 20(0) 20(2) 20(4) 20(5) 21* 21(0) 21(4) 22* 22(1) 23(1) 24(4) 25* \n" +
                "26 27(4) 28* 28(4) 29* 29(1) 29(3) 30(1) 30(2) 30(3) 31(2) 32 33(3) 34(2) \n" +
                "35 36(3) 37(2) 37(3) 38(2) 39 40 41 42 \n" +
                "43 44 45 46 47 48 49 \n" +
                "50 51 52 53 54 55 \n" +
                "56 57 58 59 60 \n" +
                "\n" +
                "Ray :[4, 3, 2, 1, 0]4 [-1, -1, -1, -1, -1, -1]3 [-1, -1, -1, -1, -1, -1]2 [-1, -1, -1, -1, -1, -1]1 [-1, -1, -1, -1, -1, -1]0 [-1, -1, -1, -1, -1, -1]\n" +
                "Ray :[23, 16, 10]23 [-1, -1, -1, -1, 1, -1]16 [-1, -1, -1, -1, -1, -1]10 [-1, -1, -1, -1, -1, -1]\n" +
                "Ray :[49, 41, 32, 23]49 [-1, -1, -1, -1, -1, -1]41 [-1, -1, -1, -1, -1, -1]32 [-1, -1, -1, -1, -1, -1]23 [-1, -1, -1, -1, 1, -1]\n" +
                "Ray :[17]17 [-1, -1, 1, -1, -1, -1]\n" +
                "Ray :[19, 18]19 [1, -1, 1, -1, -1, -1]18 [-1, -1, -1, -1, -1, -1]\n" +
                "Ray :[18, 19]18 [-1, -1, -1, -1, -1, -1]19 [1, -1, 1, -1, -1, -1]\n" +
                "Ray :[26, 27]26 [-1, -1, -1, -1, -1, -1]27 [-1, 1, -1, -1, -1, -1]\n" +
                "Ray :[37, 44, 50]37 [1, -1, -1, -1, -1, 1]44 [-1, -1, -1, -1, -1, -1]50 [-1, -1, -1, -1, -1, -1]\n" +
                "Ray :[58, 52, 45, 37]58 [-1, -1, -1, -1, -1, -1]52 [-1, -1, -1, -1, -1, -1]45 [-1, -1, -1, -1, -1, -1]37 [1, -1, -1, -1, -1, 1]\n" +
                "Ray :[30, 39, 47, 54, 60]30 [1, -1, -1, -1, 1, 1]39 [-1, -1, -1, -1, -1, -1]47 [-1, -1, -1, -1, -1, -1]54 [-1, -1, -1, -1, -1, -1]60 [-1, -1, -1, -1, -1, -1]\n" +
                "Ray :[60, 54, 47, 39, 30]60 [-1, -1, -1, -1, -1, -1]54 [-1, -1, -1, -1, -1, -1]47 [-1, -1, -1, -1, -1, -1]39 [-1, -1, -1, -1, -1, -1]30 [1, -1, -1, -1, 1, 1]";

        assertEquals(expectedOutput, newBoard.toString());
    }

    @Test
    void toStringTestDemo2() {
        Board newBoard = Board.newBoard();
        //set up the board state for demo 2 (from rulebook)
        newBoard.setAtom(13);
        newBoard.setAtom(15);
        newBoard.setAtom(31);
        newBoard.setAtom(32);
        newBoard.setAtom(51);
        newBoard.setAtom(59);

        newBoard.addRay(3, 0);
        newBoard.addRay(43, 4);
        newBoard.addRay(55, 2);

        String expectedOutput = "0 1 2 3 4 \n" +
                "5 6(5) 7(0) 8(5) 9(0) 10 \n" +
                "11 12(4) 13* 14(1) 14(4) 15* 16(1) 17 \n" +
                "18 19 20(3) 21(2) 22(3) 22(5) 23(0) 23(2) 23(5) 24(0) 25 \n" +
                "26 27 28 29 30(4) 31* 31(4) 32* 32(1) 33(1) 34 \n" +
                "35 36 37 38 39(3) 40(2) 40(3) 41(2) 42 \n" +
                "43 44(5) 45(0) 46 47 48 49 \n" +
                "50 51* 52(1) 53(5) 54(0) 55 \n" +
                "56 57(2) 58(4) 59* 60(1) \n" +
                "\n" +
                "Ray :[7, 1]7 [-1, -1, -1, 1, -1, -1]1 [-1, -1, -1, -1, -1, -1]\n" +
                "Ray :[8, 7]8 [-1, -1, 1, -1, -1, -1]7 [-1, -1, -1, 1, -1, -1]\n" +
                "Ray :[3, 8]3 [-1, -1, -1, -1, -1, -1]8 [-1, -1, 1, -1, -1, -1]\n" +
                "Ray :[44, 43]44 [-1, -1, 1, -1, -1, -1]43 [-1, -1, -1, -1, -1, -1]\n" +
                "Ray :[21, 29, 37, 44]21 [-1, -1, -1, -1, -1, 1]29 [-1, -1, -1, -1, -1, -1]37 [-1, -1, -1, -1, -1, -1]44 [-1, -1, 1, -1, -1, -1]\n" +
                "Ray :[22, 21]22 [1, -1, 1, -1, -1, -1]21 [-1, -1, -1, -1, -1, 1]\n" +
                "Ray :[21, 22]21 [-1, -1, -1, -1, -1, 1]22 [1, -1, 1, -1, -1, -1]\n" +
                "Ray :[44, 37, 29, 21]44 [-1, -1, 1, -1, -1, -1]37 [-1, -1, -1, -1, -1, -1]29 [-1, -1, -1, -1, -1, -1]21 [-1, -1, -1, -1, -1, 1]\n" +
                "Ray :[43, 44]43 [-1, -1, -1, -1, -1, -1]44 [-1, -1, 1, -1, -1, -1]\n" +
                "Ray :[53, 52]53 [-1, -1, 1, -1, -1, -1]52 [-1, -1, -1, -1, 1, -1]\n" +
                "Ray :[40, 47, 53]40 [1, -1, -1, -1, -1, 1]47 [-1, -1, -1, -1, -1, -1]53 [-1, -1, 1, -1, -1, -1]\n" +
                "Ray :[55, 48, 40]55 [-1, -1, -1, -1, -1, -1]48 [-1, -1, -1, -1, -1, -1]40 [1, -1, -1, -1, -1, 1]";

        assertEquals(expectedOutput, newBoard.toString());
    }

    @Test
    void borderTest() {
        Board newBoard = Board.newBoard();

        //cell at index 24
        int[] expectedNeighbors = {17, 25, 33, 32, 23, 16};
        assertArrayEquals(expectedNeighbors, newBoard.getNeighbors(24));

        //cell at index 0
        expectedNeighbors = new int[]{-1, 1, 6, 5, -1, -1};
        assertArrayEquals(expectedNeighbors, newBoard.getNeighbors(0));

        //cell at index 60
        expectedNeighbors = new int[]{55, -1, -1, -1, 59, 54};
        assertArrayEquals(expectedNeighbors, newBoard.getNeighbors(60));

        //cell at index 42
        expectedNeighbors = new int[]{34, -1, -1, 49, 41, 33};
        assertArrayEquals(expectedNeighbors, newBoard.getNeighbors(42));

        //cell at index 2
        expectedNeighbors = new int[]{-1, 3, 8, 7, 1, -1};
        assertArrayEquals(expectedNeighbors, newBoard.getNeighbors(2));

        //cell at index 45
        expectedNeighbors = new int[]{38, 46, 52, 51, 44, 37};
        assertArrayEquals(expectedNeighbors, newBoard.getNeighbors(45));

        //cell at index 9
        expectedNeighbors = new int[]{4, 10, 16, 15, 8, 3};
        assertArrayEquals(expectedNeighbors, newBoard.getNeighbors(9));

        //invalid cells
        assertThrows(IllegalArgumentException.class, () -> {
            newBoard.getNeighbors(100);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            newBoard.getNeighbors(-5);
        });
    }

    @Test
    void coordinateTest() {
        Board newBoard = Board.newBoard();

        int[] coords = newBoard.getCoordinates(24);
        assertArrayEquals(new int[]{6, 3}, coords);

        coords = newBoard.getCoordinates(0);
        assertArrayEquals(new int[]{0, 0}, coords);

        coords = newBoard.getCoordinates(60);
        assertArrayEquals(new int[]{4, 8}, coords);

        coords = newBoard.getCoordinates(42);
        assertArrayEquals(new int[]{7, 5}, coords);

        coords = newBoard.getCoordinates(2);
        assertArrayEquals(new int[]{2, 0}, coords);

        assertThrows(IllegalArgumentException.class, () -> {
            newBoard.getCoordinates(92);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            newBoard.getCoordinates(-3);
        });
    }

    @Test
    void initializeScoreboardTest() {
        Board newBoard = Board.newBoard();
        newBoard.Scoreboard = new Board.Scoreboard("gosling", "pitt");
        assertNotNull(newBoard.Scoreboard);
    }

    @Test
    void updateScoreboardTest() {
        Board newBoard = Board.newBoard();

        newBoard.Scoreboard = new Board.Scoreboard("gosling", "pitt");
        newBoard.Scoreboard.calculateScore(0, 8, 0);
        newBoard.Scoreboard.calculateScore(1, 4, 2);

        assertEquals("gosling", newBoard.Scoreboard.getPlayerName(0));
        assertEquals("pitt", newBoard.Scoreboard.getPlayerName(1));
        assertEquals(8, newBoard.Scoreboard.getScore(0));
        assertEquals(14, newBoard.Scoreboard.getScore(1));
    }

    @Test
    void scoreboardToStringTest() {
        Board newBoard = Board.newBoard();
        newBoard.Scoreboard = new Board.Scoreboard("gosling", "pitt");

        newBoard.Scoreboard.calculateScore(0, 8, 0);
        newBoard.Scoreboard.calculateScore(1, 4, 2);

        String expectedString = "Player 1 (gosling) Score: 8\nPlayer 2 (pitt) Score: 14";
        assertEquals(expectedString, newBoard.Scoreboard.toString());
    }

    @Test
    void saveBoardTest() {
        Board newBoard = Board.newBoard();
        newBoard.setAtom(12);
        newBoard.setAtom(21);
        newBoard.setAtom(22);
        try {
            newBoard.saveBoard();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertTrue(Files.exists(Paths.get("save.ser")));
    }

    @Test
    void saveEmptyBoardTest() {
        Board emptyBoard = Board.newBoard();
        try {
            emptyBoard.saveBoard();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertTrue(Files.exists(Paths.get("save.ser")));
    }

    @Test
    void loadBoardTest() {
        Board blankBoard = Board.newBoard();
        Board loadedBoard;
        try {
            loadedBoard = Board.loadBoard();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(loadedBoard);
        assertNotEquals(blankBoard, loadedBoard);
    }

}

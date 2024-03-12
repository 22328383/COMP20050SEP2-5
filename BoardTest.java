import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


class BoardTest {

    @Test
    void genCellCount() {
        int[] expected9 = {5, 6, 7, 8, 9, 8, 7, 6, 5};
        assertArrayEquals(expected9, Board.genCellCount(9));

        int[] expected13 = {5, 6, 7, 8, 9, 10, 11, 10, 9, 8, 7, 6, 5};
        assertArrayEquals(expected13, Board.genCellCount(13));
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
    }

    @Test
    void drawBoard() {
        Board newBoard = Board.newBoard();
        newBoard.setAtom(22);
        newBoard.setAtom(21);
        newBoard.setAtom(29);
        newBoard.addRay(60, 2);

        newBoard.drawBoard();
    }

    @Test
    void borderTest() {
        Board newBoard = Board.newBoard();
        // Test case for cell at index 24
        int[] neighbors24 = newBoard.getNeighbors(24);
        System.out.println("Neighbors of cell at index 24: " + Arrays.toString(neighbors24));
        System.out.println("Expected output: [17, 25, 33, 32, 23, 16]");
        System.out.println("");

        // Test case for cell at index 0
        int[] neighbors0 = newBoard.getNeighbors(0);
        System.out.println("Neighbors of cell at index 0: " + Arrays.toString(neighbors0));
        System.out.println("Expected output: [-1, 1, 6, 5, -1, -1]");
        System.out.println("");

        // Test case for cell at index 60
        int[] neighbors60 = newBoard.getNeighbors(60);
        System.out.println("Neighbors of cell at index 60: " + Arrays.toString(neighbors60));
        System.out.println("Expected output: [55, -1, -1, -1, 59, 54]");
        System.out.println("");

        // Test case for cell at index 42
        int[] neighbors42 = newBoard.getNeighbors(42);
        System.out.println("Neighbors of cell at index 42: " + Arrays.toString(neighbors42));
        System.out.println("Expected output: [34, -1, -1, 49, 41, 33]");
        System.out.println("");

        // Test case for cell at index 2
        int[] neighbors2 = newBoard.getNeighbors(2);
        System.out.println("Neighbors of cell at index 2: " + Arrays.toString(neighbors2));
        System.out.println("Expected output: [-1, 3, 8, 7, 1, -1]");
        System.out.println("");

        int[] neighbors45 = newBoard.getNeighbors(45);
        System.out.println("Neighbors of cell at index 45: " + Arrays.toString(neighbors45));
        System.out.println("Expected output: [38, 46, 52, 51, 44, 37]");
        System.out.println("");

        int[] neighbors9 = newBoard.getNeighbors(9);
        System.out.println("Neighbors of cell at index 9: " + Arrays.toString(neighbors9));
        System.out.println("Expected output: [4, 10, 16, 15, 8, 3]");
    }

    @Test
    void coordinateTest() {
        Board newBoard = Board.newBoard();
        // Test case for cell at index 24
        int[] coordinates24 = newBoard.getCoordinates(24);
        System.out.println("Coordinates of cell at index 24: x = " + coordinates24[0] + ", y = " + coordinates24[1]);
        System.out.println("Expected output: x = 6, y = 3");
        System.out.println("");

        // Test case for cell at index 0
        int[] coordinates0 = newBoard.getCoordinates(0);
        System.out.println("Coordinates of cell at index 0: x = " + coordinates0[0] + ", y = " + coordinates0[1]);
        System.out.println("Expected output: x = 0, y = 0");
        System.out.println("");

        // Test case for cell at index 60
        int[] coordinates60 = newBoard.getCoordinates(60);
        System.out.println("Coordinates of cell at index 60: x = " + coordinates60[0] + ", y = " + coordinates60[1]);
        System.out.println("Expected output: x = 4, y = 8");
        System.out.println("");

        // Test case for cell at index 42
        int[] coordinates42 = newBoard.getCoordinates(42);
        System.out.println("Coordinates of cell at index 42: x = " + coordinates42[0] + ", y = " + coordinates42[1]);
        System.out.println("Expected output: x = 7, y = 5");
        System.out.println("");

        // Test case for cell at index 2
        int[] coordinates2 = newBoard.getCoordinates(2);
        System.out.println("Coordinates of cell at index 2: x = " + coordinates2[0] + ", y = " + coordinates2[1]);
        System.out.println("Expected output: x = 2, y = 0");
    }

}

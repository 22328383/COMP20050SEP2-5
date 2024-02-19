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
        assertEquals(4, Board.findRow(20,nineRows));
        assertEquals(5, Board.findRow(29,nineRows));
        assertEquals(7, Board.findRow(46,nineRows));
        assertEquals(8, Board.findRow(56,nineRows));
        assertEquals(9, Board.findRow(57,nineRows));
    }

    @Test
    void drawBoard() {
        Board newBoard = new Board();
        newBoard.drawBoard();
    }
}

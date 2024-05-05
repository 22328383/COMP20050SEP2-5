import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a cell in a board with info about its row, atom presence and their sides.
 * Each cell can contain an atom and has six sides, indexed from 0 to 5 in clockwise order.
 */
public class Cell implements Serializable {
    private int row;

    private boolean hasAtom;
    private ArrayList<Integer> atomSides = new ArrayList<Integer>();

    /**
     * Constructs a Cell object with the specified attributes.
     *
     * @param hasAtom true if the cell contains an atom, false otherwise
     * @param row     the row index of the cell in the grid
     */
    public Cell(boolean hasAtom, int row) {
        this.hasAtom = hasAtom;
        this.row = row;

        // populate atomSides with default values
        for (int i = 0; i < 6; i++) {
            atomSides.add(-1);
        }
    }

    /**
     * Sets whether the cell contains an atom.
     *
     * @param hasAtom true if the cell contains an atom, false otherwise
     */
    public void setHasAtom(boolean hasAtom) {
        this.hasAtom = hasAtom;
    }

    /**
     * Returns a copy of the atom sides in clockwise order.
     *
     * @return a new ArrayList containing the atom sides rearranged in clockwise order
     */
    // method to return a copy of atomSides in CLOCKWISE order
    public ArrayList<Integer> atomSideToClockwise() {
        // copy
        ArrayList<Integer> cwAtomSides = (ArrayList<Integer>) this.atomSides.clone();
        // rearrange elements in clockwise order
        for (int i = 0; i < cwAtomSides.size(); i++) {
            cwAtomSides.set(((i + 3) % 6), atomSides.get(i));
        }
        return cwAtomSides;
    }

    /**
     * Checks if the cell contains an atom.
     *
     * @return true if the cell contains an atom, false otherwise
     */
    public boolean hasAtom() {
        return hasAtom;
    }

    /**
     * Returns the bordering atom sides of the cell.
     *
     * @return an ArrayList containing the atom sides
     */
    public ArrayList<Integer> getAtomSides() {
        return atomSides;
    }

    /**
     * Returns the row idx of the cell.
     *
     * @return the row idx of the cell
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns a string representation of the cell.
     *
     * @return a string containing the row index of the cell
     */
    @Override
    public String toString() {
        String output = "Row:" + getRow();
        return output;
    }
}

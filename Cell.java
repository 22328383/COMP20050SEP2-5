import java.io.Serializable;
import java.util.ArrayList;

public class Cell implements Serializable {
    private int row;

    private boolean hasAtom;
    private ArrayList<Integer> atomSides = new ArrayList<Integer>();

    // constructor
    public Cell(boolean hasAtom, int row) {
        this.hasAtom = hasAtom;
        this.row = row;

        // populate atomSides with default values
        for (int i = 0; i < 6; i++) {
            atomSides.add(-1);
        }
    }

    public void setHasAtom(boolean hasAtom) {
        this.hasAtom = hasAtom;
    }

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

    public boolean hasAtom() {
        return hasAtom;
    }

    public ArrayList<Integer> getAtomSides() {
        return atomSides;
    }

    public int getRow() {
        return row;
    }

    @Override
    public String toString() {
        String output = "Row:" + getRow();
        return output;
    }
}

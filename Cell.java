import java.util.ArrayList;

public class Cell {
    static final int MAXSIDES = 6;
    private int row;
    private ArrayList<Side> sides = new ArrayList<Side>();
    private boolean hasAtom;
    private ArrayList<Side> atomSides;

    public Cell(ArrayList<Side> sides, boolean hasAtom, int row, ArrayList<Side> atomSide) {
        this.sides = sides;
        this.hasAtom = hasAtom;
        this.row = row;
        this.atomSides = atomSide;
    }

    public ArrayList<Side> getSides() {
        return sides;
    }

    public boolean isHasAtom() {
        return hasAtom;
    }

    public ArrayList<Side> getAtomSide() {
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

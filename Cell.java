import java.util.ArrayList;

public class Cell {
    static final int MAXSIDES = 6;
    private int row;
    private ArrayList<Side> sides = new ArrayList<Side>();

    private boolean hasAtom;
    private ArrayList<Integer> atomSides = new ArrayList<Integer>();

    public Cell(ArrayList<Side> sides, boolean hasAtom, int row) {
        this.sides = sides;
        this.hasAtom = hasAtom;
        this.row = row;
        for(int i = 0; i < 6; i++) {
            atomSides.add(-1);
        }
    }

    public void setHasAtom(boolean hasAtom) {
        this.hasAtom = hasAtom;
    }

    public ArrayList<Integer> atomSideToClockwise() {
        ArrayList<Integer> cwAtomSides = (ArrayList<Integer>)this.atomSides.clone();
        for(int i = 0; i < cwAtomSides.size(); i++) {
            cwAtomSides.set(((i+3)%6), atomSides.get(i));
        }
        return cwAtomSides;
    }

    public ArrayList<Side> getSides() {
        return sides;
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

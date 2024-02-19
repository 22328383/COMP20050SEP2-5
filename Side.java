public class Side {
    private final int bearing;
    private Cell owner;

    public Side(int bearing, Cell owner) {
        this.bearing = bearing;
        this.owner = owner;
    }

    public int getBearing() {
        return bearing;
    }

    public Cell getOwner() {
        return owner;
    }

    public void setOwner(Cell owner) {
        this.owner = owner;
    }
}

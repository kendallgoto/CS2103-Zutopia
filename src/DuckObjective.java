public class DuckObjective extends Objective {
    /**
     * Creates a new "Duck" objective at a given x and y location
     * @param x top left corner x offset
     * @param y top left corner y offset
     */
    public DuckObjective(int x, int y) {
        super(x, y);
        setImage("duck.jpg");
        setTeleportSound("quack.wav");
    }
}

public class GoatObjective extends Objective {
    /**
     * Creates a new "Goat" objective at a given x and y location
     * @param x top left corner x offset
     * @param y top left corner y offset
     */
    public GoatObjective(int x, int y) {
        super(x, y);
        setImage("goat.jpg");
        setTeleportSound("bleat.wav");
    }
}

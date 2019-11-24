public class HorseObjective extends Objective {

    /**
     * Creates a new "Horse" objective at a given x and y location
     * @param x top left corner x offset
     * @param y top left corner y offset
     */
    public HorseObjective(int x, int y) {
        super(x, y);
        setImage("horse.jpg");
        setTeleportSound("whinny.wav");
    }
}

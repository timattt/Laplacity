package steelUnicorn.laplacity.ui.handler;

import steelUnicorn.laplacity.cat.Cat;
import steelUnicorn.laplacity.field.graphics.TrajectoryRenderer;

public class SlingshotHandler {
    private Cat cat;
    private float slingshotX;
    private float slingshotY;
    private boolean wasSet;

    public SlingshotHandler() {
        wasSet = false;
    }

    public void setSlingshot(Cat cat, float slingshotX, float slingshotY) {
        this.cat = cat;
        this.slingshotX = slingshotX;
        this.slingshotY = slingshotY;
        wasSet = true;
    }

    public void changeSlingshot(float x, float y) {
        cat.setSlingshot(x, y);
        TrajectoryRenderer.updateTrajectory();
    }

    public boolean wasSlingshotSet() {
        if (wasSet) {
            wasSet = false;
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return "SlingshotHandler{" +
                "slingshotValue=" + "(" + slingshotX + "," + slingshotY + ")" +
                ", wasSet=" + wasSet +
                "}\n";
    }
}

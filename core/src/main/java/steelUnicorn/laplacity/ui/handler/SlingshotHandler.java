package steelUnicorn.laplacity.ui.handler;

import com.badlogic.gdx.math.Vector2;

public class SlingshotHandler {
    private Vector2 slingshotValue;
    private boolean wasSet;

    public SlingshotHandler() {
        wasSet = false;
    }

    public void setSlingshot(Vector2 slingshotValue) {
        this.slingshotValue = slingshotValue;
        wasSet = true;
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
                "slingshotValue=" + slingshotValue +
                ", wasSet=" + wasSet +
                "}\n";
    }
}

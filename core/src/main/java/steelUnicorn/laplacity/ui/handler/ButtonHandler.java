package steelUnicorn.laplacity.ui.handler;

import com.badlogic.gdx.scenes.scene2d.ui.Button;

public class ButtonHandler {
    public Button btn;
    private boolean wasPressed;

    public ButtonHandler(Button btn) {
        this.btn = btn;
        wasPressed = false;
    }

    public void pressButton() {
        wasPressed = true;
    }

    public boolean wasButtonPressed() {
        if (wasPressed) {
            wasPressed = false;
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return "BtnHandler{" +
                "btn=" + btn.getName() +
                ", wasPressed=" + wasPressed +
                "}\n";
    }
}

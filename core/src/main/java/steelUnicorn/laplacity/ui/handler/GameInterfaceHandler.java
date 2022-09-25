package steelUnicorn.laplacity.ui.handler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;

import steelUnicorn.laplacity.ui.GameInterface;

public class GameInterfaceHandler {
    private OrderedMap<String, ButtonHandler> buttonHandlers;
    public ParticleHandler particleHandler;
    public SlingshotHandler slingshotHandler;

    public GameInterfaceHandler() {
        buttonHandlers = new OrderedMap<>();
        particleHandler = new ParticleHandler();
        slingshotHandler = new SlingshotHandler();
    }

    public void putButton(Button btn) {
        buttonHandlers.put(btn.getName(), new ButtonHandler(btn));
    }

    public void pressButton(String btnName) {
        if (btnName != null && buttonHandlers.containsKey(btnName)) {
            buttonHandlers.get(btnName).pressButton();
        }
    }

    public boolean wasButtonPressed(String btnName) {
        if (buttonHandlers.containsKey(btnName)) {
            return buttonHandlers.get(btnName).wasButtonPressed();
        }

        return false;
    }

    public void render() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            debugPrint();
        }
    }

    public void dispose() {
        buttonHandlers.clear();
    }

    public void debugPrint() {
        Gdx.app.log("GameInterfaceHandler", "===Start printing===");
        ObjectMap.Values<ButtonHandler> values = buttonHandlers.values();
        for (ButtonHandler value : values) {
            Gdx.app.log("GameInterfaceHandler", value.toString());
        }

        Gdx.app.log("GameInterfaceHandler", particleHandler.toString());
        Gdx.app.log("GameInterfaceHandler", slingshotHandler.toString());

        Gdx.app.log("GameInterfaceHandler", "===Finish printing===");
    }
}

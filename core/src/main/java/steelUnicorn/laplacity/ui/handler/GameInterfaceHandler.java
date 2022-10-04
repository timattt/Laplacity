package steelUnicorn.laplacity.ui.handler;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;

/**
 * Класс для управления интерфейсом. Задает флаги при нажатии на кнопки, задании траектории и
 * установке заряда.
 *
 * Кнопки хранятся в виде словаря, где ключ - имя кнопки а значение, обработчик кнопки.
 *
 * @see ButtonHandler
 * @see ParticleHandler
 * @see SlingshotHandler
 * @see FlashingBtnHandler
 */
public class GameInterfaceHandler {
    private OrderedMap<String, ButtonHandler> buttonHandlers;
    public ParticleHandler particleHandler;
    public SlingshotHandler slingshotHandler;
    public FlashingBtnHandler flashingBtnHandler;

    public GameInterfaceHandler() {
        buttonHandlers = new OrderedMap<>();
        particleHandler = new ParticleHandler();
        slingshotHandler = new SlingshotHandler();
        flashingBtnHandler = new FlashingBtnHandler();
    }

    /**
     * Кладет кнопку в словарь {@link #buttonHandlers}, где ключ - имя кнопки.
     * @param btn кнопка помещаемая в словарь.
     */
    public void putButton(Button btn) {
        buttonHandlers.put(btn.getName(), new ButtonHandler(btn));
    }

    /**
     * Устанавливает флаг, что кнопка с именем btnName нажата.
     * @param btnName имя кнопки
     */
    public void pressButton(String btnName) {
        if (btnName != null && buttonHandlers.containsKey(btnName)) {
            buttonHandlers.get(btnName).pressButton();
        }
    }

    /**
     * Проверяет была ли нажата кнопка под именем btnName. Возвращает true если была и false иначе.
     * При этом состояние флажок о нажатии кнопки становится false
     * @param btnName имя кнопки
     * @return была ли кнопка нажата
     */
    public boolean wasButtonPressed(String btnName) {
        if (btnName != null && buttonHandlers.containsKey(btnName)) {
            return buttonHandlers.get(btnName).wasButtonPressed();
        }

        return false;
    }

    /**
     * Отключает кнопку по имени.
     * @param btnName имя кнопки.
     */
    public void lockBtn(String btnName) {
        if (btnName != null && buttonHandlers.containsKey(btnName)) {
            Button btn = buttonHandlers.get(btnName).btn;
            btn.setDisabled(true);
            btn.setTouchable(Touchable.disabled);
        }
    }

    /**
     * Отключает все кнопки
     */
    public void lockAllButtons() {
        ObjectMap.Values<ButtonHandler> handlers = buttonHandlers.values();
        for (ButtonHandler handler : handlers) {
            handler.btn.setDisabled(true);
            handler.btn.setTouchable(Touchable.disabled);
        }
    }

    /**
     * Включает кнопку по имени.
     * @param btnName имя кнопки.
     */
    public void unlockBtn(String btnName) {
        if (btnName != null && buttonHandlers.containsKey(btnName)) {
            Button btn = buttonHandlers.get(btnName).btn;
            btn.setDisabled(false);
            btn.setTouchable(Touchable.enabled);
        }
    }

    /**
     * Включает мигание кнопки с именем btnName через delay секунд, каждые interval секунд
     * repeatCount раз. Мигание само остановится по истечению, но можно отключить и методом.
     * @param btnName имя кнопки
     * @param delay задержка до мигания в секундах
     * @param interval интервал миггания в секундах
     * @param repeatCount количество миганий
     *
     * @see #stopFlashing()
     */
    public void startFlashing(String btnName, float delay, float interval, int repeatCount) {
        if (btnName != null && buttonHandlers.containsKey(btnName)) {
            Button btn = buttonHandlers.get(btnName).btn;
            flashingBtnHandler.setBtn(btn);
            flashingBtnHandler.startFlashing(delay, interval, repeatCount);
        }
    }

    /**
     * Останавливает мигание
     */
    public void stopFlashing() {
        flashingBtnHandler.stopFlashing();
    }

    public void render() {
        //debugging comments
//        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
//            debugPrint();
//            slingshotHandler.wasSlingshotSet();
//        }
//
//        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
//            startFlashing("Flight", 0, 1, 5);
//        }
//
//        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
//            startFlashing("Protons", 0, 1, 10);
//        }
//
//        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
//            lockAllButtons();
//        }
//
//        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
//            slingshotHandler.setLocked(Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT));
//        }
    }

    //debugging function
//    public void debugPrint() {
//        Gdx.app.log("GameInterfaceHandler", "===Start printing===");
//        ObjectMap.Values<ButtonHandler> values = buttonHandlers.values();
//        for (ButtonHandler value : values) {
//            Gdx.app.log("GameInterfaceHandler", value.toString());
////            if (Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT)) {
////                unlockBtn(value.btn.getName());
////            } else {
////                lockBtn(value.btn.getName());
////            }
//        }
//
//        Gdx.app.log("GameInterfaceHandler", particleHandler.toString());
//        Gdx.app.log("GameInterfaceHandler", slingshotHandler.toString());
//
//        Gdx.app.log("GameInterfaceHandler", "===Finish printing===");
//    }
}

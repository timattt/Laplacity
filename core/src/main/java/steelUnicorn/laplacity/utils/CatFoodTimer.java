package steelUnicorn.laplacity.utils;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Timer;

/**
 * Класс таймер для еды кота.
 * Создает таск который каждую секунду уменьшаяет таймер
 *
 */
public class CatFoodTimer {
    public static final int MAX_VALUE = 5 * 60; //5 minutes
    private Label currentLabel;

    private int sec;
    private int min;
    //Task that will be run each second
    Timer.Task task = new Timer.Task() {
        @Override
        public void run() {
            sec--;

            if (sec < 0) {
                if (min == 0) {
                    updateTimer();
                    //TODO add launch
                } else {
                    min--;
                    sec = 59;
                }
            }

            currentLabel.setText(String.format("%02d:%02d", min, sec));
        }
    };

    public CatFoodTimer(int seconds) {
        setTime(seconds);
        //Каждую секунду таймер будет обновляться
        start();
    }

    public void start() {
        Timer.schedule(task, 0, 1);
    }

    public void stop() {
        task.cancel();
    }

    private void setTime(int seconds) {
        if (seconds > MAX_VALUE) {
            seconds = MAX_VALUE;
        }
        sec = seconds % 60;
        min = seconds / 60;
    }

    public void setCurrentLabel(Label label) {
        currentLabel = label;
    }

    public int getMin() {
        return min;
    }

    public int getSec() {
        return sec;
    }

    private void updateTimer() {
        setTime(MAX_VALUE);
    }
}

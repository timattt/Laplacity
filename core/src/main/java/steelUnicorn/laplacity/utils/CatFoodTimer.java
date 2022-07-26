package steelUnicorn.laplacity.utils;

import static steelUnicorn.laplacity.core.Globals.catFood;
import com.badlogic.gdx.utils.Timer;

import steelUnicorn.laplacity.ui.CatFoodInterface;

/**
 * Класс таймер для еды кота.
 * Создает таск который каждую секунду уменьшаяет таймер
 * и записывает в нужный label
 *
 */
public class CatFoodTimer {
    public static final int MAX_VALUE = 5; //5 minutes
    private CatFoodInterface currentInterface;

    private int sec;
    private int min;
    //Task that will be run each second
    public Timer.Task task = new Timer.Task() {
        @Override
        public void run() {
            sec--;

            if (sec < 0) {
                if (min == 0) {
                    if (currentInterface != null) {
                        currentInterface.update(catFood.reload());
                    }

                    updateTimer();
                } else {
                    min--;
                    sec = 59;
                }
            }

            setLabelText();
        }
    };

    public CatFoodTimer(int seconds) {
        setTime(seconds);
        //Каждую секунду таймер будет обновляться
        if (catFood.getTotalLaunchesAvailable()
                >= catFood.TOTAL_LAUNCHES_AVAILABLE_DEFAULT_VALUE) {
            start();
        } else {
            stop();
        }
    }

    public void start() {
        if (currentInterface != null) {
            currentInterface.getTimerLabel().setVisible(true);
        }

        Timer.schedule(task, 0, 1);
    }

    public void stop() {
        if (currentInterface != null) {
            currentInterface.getTimerLabel().setVisible(false);
        }

        if (!task.isScheduled()) {
            task.cancel();
        }
    }

    private void setTime(int seconds) {
        if (seconds > MAX_VALUE) {
            seconds = MAX_VALUE;
        }
        sec = seconds % 60;
        min = seconds / 60;
    }

    public void setCurrentInterface(CatFoodInterface foodInterface) {
        currentInterface = foodInterface;
        setLabelText();
    }

    private void setLabelText() {
        if (currentInterface != null) {
            currentInterface.getTimerLabel()
                    .setText(String.format("%02d:%02d", min, sec));
        }
    }

    private void updateTimer() {
        setTime(MAX_VALUE);
    }
}

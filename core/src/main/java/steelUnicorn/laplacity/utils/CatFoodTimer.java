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
    public static final int MAX_VALUE = 60; //seconds
    private CatFoodInterface currentInterface;

    private int sec;
    private int min;
    //Task that will be run each second
    public Timer.Task task = new Timer.Task() {
        @Override
        public void run() {
            sec--;

            if (sec < 0) {
                if (min <= 0) {
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
    }

    public void start() {
        if (!task.isScheduled()) {
            Timer.schedule(task, 0, 1);
        }
        checkLabelVisible();
    }

    public void stop() {
        if (task.isScheduled()) {
            task.cancel();
        }
        checkLabelVisible();
    }

    private void setTime(int seconds) {
        if (seconds > MAX_VALUE || seconds < 0) {
            seconds = MAX_VALUE;
        }
        sec = seconds % 60;
        min = seconds / 60;
    }

    //Функция устанавливает, каким интерфейсом управляет таймер.
    public void setCurrentInterface(CatFoodInterface foodInterface) {
        currentInterface = foodInterface;
        setLabelText(); //для обновления надписи
    }

    //Устанавливает текст формата mm:ss в поле timerLabel  класса CatFoodInterface
    private void setLabelText() {
        if (currentInterface != null) {
            currentInterface.getTimerLabel()
                    .setText(String.format("%02d:%02d", min, sec));
            checkLabelVisible();
        }
    }

    public void checkLabelVisible() {
        if (currentInterface != null) {
            if (task.isScheduled()) {
                currentInterface.getTimerLabel().setVisible(true);
            } else {
                currentInterface.getTimerLabel().setVisible(false);
            }
        }
    }
    //Функция вызывается когда таймер нужно обновить
    private void updateTimer() {
        setTime(MAX_VALUE);
        catFood.reload();
        if (currentInterface != null) {
            currentInterface.update(catFood.getTotalLaunchesAvailable());
        }
        checkLabelVisible();
    }
}

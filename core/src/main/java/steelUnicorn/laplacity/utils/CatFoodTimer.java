package steelUnicorn.laplacity.utils;

import static steelUnicorn.laplacity.core.Globals.catFood;
import com.badlogic.gdx.utils.Timer;

import steelUnicorn.laplacity.core.Laplacity;
import steelUnicorn.laplacity.ui.CatFoodInterface;

/**
 * Класс таймер для еды кота.
 * Создает таск который каждую секунду уменьшаяет таймер
 * и записывает в нужный label
 *
 * Класс содержит функции start() и stop() для запуска и остановки таймера. Таймера останавливается
 * только когда еда у кота полная. При этом с помощью функции checkVisible label скрывается.
 *
 * Класс хранит у себя ссылку на текущий интерфейс currentInterface, визуальный таймер
 * в котором он меняет
 */
public class CatFoodTimer {
    public static final int MAX_VALUE = Laplacity.isDebugEnabled() ? 5 : 60; //seconds
    private CatFoodInterface currentInterface;

    private int sec;
    private int min;
    //Таск который уменьшает таймер на 1 секунду
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

    /**
     * Возвращает время в таймере
     * @return время в секундах
     */
    public int getTime() {
        return min * 60 + sec;
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

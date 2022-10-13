package steelUnicorn.laplacity.utils;

import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;

import steelUnicorn.laplacity.core.Laplacity;
import steelUnicorn.laplacity.ui.CatFoodInterface;

/**
 * Таймер для подгрузки еды для кота.
 * Создает Timer.Task который каждую секунду уменьшаяет таймер и записывает в label.
 * <p>
 * Хранит ссылку на объект CatFood и CatFoodInterface.
 *
 * @see CatFood
 * @see CatFoodInterface
 * TODO entryUpdate and reduceTimer refactoring
 */
public class CatFoodTimer {
    private final CatFood catFoodInstance;
    private CatFoodInterface currentInterface;

    public static final int MAX_VALUE = Laplacity.isDebugEnabled() ? 20 : 60; //max значение таймера
    private static final int SLOWDOWN = Laplacity.isDebugEnabled() ?  2 : 10; //замедление таймера при выходе из игры

    private int sec;
    private int min;
    /**
     * Таск уменьшающий таймер на 1 секунду и меняющий текст на лейбле таймера.
     * @see Timer.Task
     * @see #reduceTimer(int)
     * @see #setLabelTime()
     */
    public Timer.Task task = new Timer.Task() {
        @Override
        public void run() {
            reduceTimer(1);
            setLabelTime();

            if (min == 0 && sec == Math.round(CatFoodInterface.showHideDur / 2)) {
                if (currentInterface != null) {
                    currentInterface.showHide();
                }
            }
        }
    };

    /**
     * Сохраняет ссылку на объект CatFood и записывает время таймера.
     * Вызывает entryUpdate для обновления запусков при открытии игры.
     *
     * @param instance объект класса CatFood
     * @see #setTime(int)
     * @see CatFood#getTimerValue()
     * @see #entryUpdate(long)
     */
    public CatFoodTimer(CatFood instance) {
        catFoodInstance = instance;
        setTime(catFoodInstance.getTimerValue());
    }

    /**
     * Уменьшает значение таймера на переданное количество секунд и обновляет таймер.
     *
     * @param seconds количество секунд
     * @see #updateTimer()
     */
    private void reduceTimer(int seconds) {
        sec -= seconds;

        if (sec < 0) {
            if (min <= 0) {
                updateTimer();
            } else {
                min--;
                sec = 59;
            }
        }
    }

    /**
     * Запускает таймер.
     */
    public void start() {
        if (!task.isScheduled()) {
            setTime(MAX_VALUE); //Start вызывается когда еда только потратилась. Иначе таймер sceduled
            Timer.schedule(task, 0, 1);
        }
        checkLabelVisible();
    }

    /**
     * Останавливает таймер.
     */
    public void stop() {
        if (task.isScheduled()) {
            task.cancel();
        }
        checkLabelVisible();
    }

    /**
     * Устанавливает время таймера, пересчитывая переданные секунды в минуты:секунды.
     * @param seconds количество секунд
     */
    public void setTime(int seconds) {
        if (seconds > MAX_VALUE || seconds < 0) {
            seconds = MAX_VALUE;
        }
        sec = seconds % 60;
        min = seconds / 60;
    }

    /**
     * Возвращает время в таймере в секундах.
     * @return время в секундах
     */
    public int getTime() {
        return min * 60 + sec;
    }

    /**
     * Возвращает время для восстановления еды, когда приложение свернуто или выключено.
     * @return Количество секунд для восстановления еды.
     * @see #SLOWDOWN
     */
    public int getRestoreTime() {
        int result = (CatFood.MAX_LAUNCHES - catFoodInstance.getLaunches() - 1) * MAX_VALUE +
                getTime();
        result = Math.max(result, 0);

        return result * SLOWDOWN;
    }

    /**
     * Устанавливает интерфейс с таймером для изменения отображаемого времени и количества запусков.
     *
     * @param foodInterface интерфейс CatFoodInterface.
     * @see CatFoodInterface
     */
    public void setCurrentInterface(CatFoodInterface foodInterface) {
        currentInterface = foodInterface;
        setLabelTime();
    }

    /**
     * Меняет отображаемое время в текущем отображаемом интерфейсе.
     * @see #setCurrentInterface(CatFoodInterface)
     */
    private void setLabelTime() {
        if (currentInterface != null) {
            currentInterface.getTimerLabel()
                    .setText(String.format("%02d:%02d", min, sec));
            checkLabelVisible();
        }
    }

    /**
     * Скрывает и отображет время в интерфейсе в зависимости от работы таймера.
     */
    private void checkLabelVisible() {
        if (currentInterface != null) {
            currentInterface.getTimerLabel().setVisible(task.isScheduled());
        }
    }

    /**
     * Восстанавливает запуск по истечению таймера и меняет надпись с количеством запусков.
     */
    private void updateTimer() {
        setTime(MAX_VALUE);
        catFoodInstance.reload();
        if (currentInterface != null) {
            currentInterface.update(catFoodInstance.getLaunches());
        }
        checkLabelVisible();
    }

    /**
     * Считает разницу между входом и выходом и добавляющий нужное количество запусков.
     * Когда приложение свернуто или выключено время уменьшается в {@link #SLOWDOWN} раз.
     * @param exitTime - системное время выхода в миллисекундах
     * @see #SLOWDOWN
     */
    public void entryUpdate(long exitTime) {
        long curTime = TimeUtils.millis();
        //При выходе время идет в 10 раз медленнее => делим на SLOWDOWN
        int passedSeconds = (int)((curTime - exitTime) / 1000L / SLOWDOWN);

        //Пока прошедшее время не обработано или таск не остановлен из за полной еды
        while (passedSeconds > 0 &&
                catFoodInstance.getLaunches() < CatFood.MAX_LAUNCHES) {
            if (sec == 0) {
                reduceTimer(1);
                passedSeconds--;
            } else if (sec > passedSeconds){
                reduceTimer(passedSeconds);
                passedSeconds = 0;
            } else {
                passedSeconds -= sec;
                reduceTimer(sec);
            }
        }
    }
}

package steelUnicorn.laplacity.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.TimeUtils;

import steelUnicorn.laplacity.GameProcess;

/**
 * Класс для управления запусками кота.
 * Хранит количество запусков и меняет количество через запуск, восстановление и показ рекламы.
 * Также содержит в себе таймер и управляет его запуском и остановкой.
 * <p>
 * Сохраняет количество запусков, время таймера и время выхода в Preferences при сворачивании
 * или отключении приложения.
 *
 * @see CatFoodTimer
 * @see Preferences
 */
public class CatFood {
    private final Preferences foodPrefs;
    public CatFoodTimer timer;

    public static final int MAX_LAUNCHES = 25;
    private int launches;

    private static final int REWARDED_LAUNCHES = 10;
    private static final int INTERSTITIAL_LAUNCHES = 3;

    /**
     * Подгружает из Preferences количество еды, создает таймер и вызывает {@link #checkBounds()}
     * для проверки корректности и запуска таймера.
     *
     * @see #checkBounds()
     */
    public CatFood() {
        foodPrefs = Gdx.app.getPreferences("CatFood");
        launches = foodPrefs.getInteger("totalLaunches", MAX_LAUNCHES);

        timer = new CatFoodTimer(this);
        timer.entryUpdate(getExitTime());
        checkBounds();
    }

    public int getLaunches() {
        return launches;
    }

    /**
     * Проверяет корректность количества запусков.
     * Останавливает таймер если количество запусков максимально возможное, иначе запускает.
     *
     * Вызывает {@link #saveFoodPrefs()}
     *
     * @see CatFoodTimer#start()
     * @see CatFoodTimer#stop()
     * @see #saveFoodPrefs()
     */
    private void checkBounds() {
        if (launches >= MAX_LAUNCHES) {
            launches = MAX_LAUNCHES;
            timer.stop();
        } else {
            if (launches <= 0) {
                launches = 0;
            }

            timer.start();
        }

        saveFoodPrefs();
    }

    /**
     * Добавляет запуски после просмотра interstitial рекламы.
     * @return количество запусков
     */
    public int interstitialShown() {
        launches += INTERSTITIAL_LAUNCHES;
        checkBounds();
        return launches;
    }

    /**
     * Добавляет запуски после просмотра rewarded рекламы.
     * @return количество запусков
     */
    public int rewardedShown() {
        launches += REWARDED_LAUNCHES;
        checkBounds();
        return launches;
    }

    /**
     * Уменьшает количество запусков на единицу (т.е. вызывается при запуске кота)
     * @return количество запусков
     */
    public int launch() {
        //First condition false only in 1st section. So the second condition will check in 1st section.
        if (GameProcess.sectionNumber > 1 || GameProcess.levelNumber == 4) {
            Gdx.app.log("Tutorial fixes", "launch decreased");
            launches--;
        }
        checkBounds();
        return launches;
    }
    /**
     * Восстанавливает запуски на единицу (при истечении таймера).
     * @return количество запусков
     */
    public int reload() {
        launches++;
        checkBounds();
        return launches;
    }

    public int addLaunches(int newLaunches) {
        launches += newLaunches;
        checkBounds();
        return launches;
    }

    public long getExitTime() {
        return foodPrefs.getLong("exitTime", TimeUtils.millis());
    }
    public int getTimerValue() { return foodPrefs.getInteger("timerValue", CatFoodTimer.MAX_VALUE);
    }

    /**
     * Записывает количество запусков, время выхода и время таймера.
     */
    public void saveFoodPrefs() {
        foodPrefs.putInteger("totalLaunches", launches);
        foodPrefs.putInteger("timerValue", timer.getTime());
        foodPrefs.putLong("exitTime", TimeUtils.millis());
        foodPrefs.flush();
    }
}

package steelUnicorn.laplacity.ui;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import steelUnicorn.laplacity.utils.CatFoodTimer;

/**
 * Класс для сохранения, удаления и управления количеством запусков.
 *
 * Хранит в себе максимальное количество запусков и текущее количестао
 *
 * подгружает текущее количество из preferences и кладет туда же при вызове dispose
 *
 * При создании, создает объект CatFoodTimer для управление восстановлением запусков.
 */
public class CatFood {
    // Total launches available
    public static final int TOTAL_LAUNCHES_AVAILABLE_DEFAULT_VALUE = 20;
    private int totalLaunchesAvailable;
    //prefs
    private Preferences foodPrefs;

    //advertisment
    private static final int REWARDED_LAUNCHES = 10;
    private static final int INTERSTITIAL_LAUNCHES = 2;

    public CatFoodTimer timer;
    //Конструктор подгружает из preferences
    public CatFood() {
        //подгрузка из prefs еды кота
        foodPrefs = Gdx.app.getPreferences("CatFood");

        if (foodPrefs.contains("totalLaunches")) {
            totalLaunchesAvailable = foodPrefs.getInteger("totalLaunches");
        } else {
            totalLaunchesAvailable = TOTAL_LAUNCHES_AVAILABLE_DEFAULT_VALUE;
        }

        //TODO calculate right time
        //timer initialize
        timer = new CatFoodTimer(CatFoodTimer.MAX_VALUE);
        if (totalLaunchesAvailable < TOTAL_LAUNCHES_AVAILABLE_DEFAULT_VALUE) {
            timer.start();
        } else {
            timer.stop();
        }
        checkBounds();
    }

    public int getTotalLaunchesAvailable() {
        return totalLaunchesAvailable;
    }
    
    //Функция проверки чтобы количество запусков не вышло а границы
    private void checkBounds() {
        if (totalLaunchesAvailable >= TOTAL_LAUNCHES_AVAILABLE_DEFAULT_VALUE) {
            totalLaunchesAvailable = TOTAL_LAUNCHES_AVAILABLE_DEFAULT_VALUE;
            timer.stop();
        } else {
            if (totalLaunchesAvailable <= 0) {
                totalLaunchesAvailable = 0;
            }

            timer.start();
        }
    }

    public int interstitialShown() {
        totalLaunchesAvailable += INTERSTITIAL_LAUNCHES;
        checkBounds();
        return totalLaunchesAvailable;
    }

    public int rewardedShown() {
        totalLaunchesAvailable += REWARDED_LAUNCHES;
        checkBounds();
        return totalLaunchesAvailable;
    }

    //Вызывается при запуске
    public int launch() {
        totalLaunchesAvailable--;
        checkBounds();
        return totalLaunchesAvailable;
    }

    public int reload() {
        totalLaunchesAvailable++;
        checkBounds();
        return totalLaunchesAvailable;
    }

    public void dispose() {
        foodPrefs.putInteger("totalLaunches", totalLaunchesAvailable);
        foodPrefs.flush();
    }
}

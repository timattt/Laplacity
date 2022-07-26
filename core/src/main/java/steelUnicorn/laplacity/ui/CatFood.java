package steelUnicorn.laplacity.ui;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import steelUnicorn.laplacity.core.Globals;

/**
 * Класс для сохранения, удаления и управления количеством запусков.
 *
 * Хранит в себе максимальное количество запусков и текущее количестао
 *
 * подгружает текущее количество из preferences и кладет туда же при вызове dispose
 *
 */
public class CatFood {
    // Total launches available
    private static final int TOTAL_LAUNCHES_AVAILABLE_DEFAULT_VALUE = 10;
    private int totalLaunchesAvailable;
    //prefs
    private Preferences foodPrefs;

    //advertisment
    private static final int REWARDED_LAUNCHES = 10;
    private static final int INTERSTITIAL_LAUNCHES = 5;

    //Конструктор подгружает из preferences
    public CatFood() {
        //подгрузка из prefs еды кота
        foodPrefs = Gdx.app.getPreferences("CatFood");

        if (foodPrefs.contains("totalLaunches")) {
            totalLaunchesAvailable = foodPrefs.getInteger("totalLaunches");
        } else {
            totalLaunchesAvailable = TOTAL_LAUNCHES_AVAILABLE_DEFAULT_VALUE;
        }

        checkPositive();
    }

    public int getTotalLaunchesAvailable() {
        return totalLaunchesAvailable;
    }
    //Функция проверки чтобы количество запусков не вышло а границы
    private void checkPositive() {
        if (totalLaunchesAvailable < 0) {
            totalLaunchesAvailable = 0;
        }
    }

    //Функция показа рекламы и добавления запусков в награду
    public int callInterstitialAd() {
        Globals.game.showInterstitial();
        totalLaunchesAvailable += INTERSTITIAL_LAUNCHES;
        return totalLaunchesAvailable;
    }

    public int callRewardedAd() {
        Globals.game.showRewarded();
        totalLaunchesAvailable += REWARDED_LAUNCHES;
        return totalLaunchesAvailable;
    }

    //Вызывается при запуске
    public int launch() {
        totalLaunchesAvailable--;
        checkPositive();
        return totalLaunchesAvailable;
    }

    public void dispose() {
        foodPrefs.putInteger("totalLaunches", totalLaunchesAvailable);
        foodPrefs.flush();
    }
}

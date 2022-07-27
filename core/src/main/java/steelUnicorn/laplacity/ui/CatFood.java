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
    private static final int INTERSTETIAL_LAUNCHES = 10;
    private static final int BANNER_LAUNCHES = 5;

    //Конструктор подгружает из preferences
    public CatFood() {
        //подгрузка из prefs еды кота
        foodPrefs = Gdx.app.getPreferences("CatFood");

        if (foodPrefs.contains("totalLaunches")) {
            totalLaunchesAvailable = foodPrefs.getInteger("totalLaunches");
        } else {
            totalLaunchesAvailable = TOTAL_LAUNCHES_AVAILABLE_DEFAULT_VALUE;
        }

        checkBounds();
    }

    public int getTotalLaunchesAvailable() {
        return totalLaunchesAvailable;
    }
    
    //Функция проверки чтобы количество запусков не вышло а границы
    private void checkBounds() {
        if (totalLaunchesAvailable > TOTAL_LAUNCHES_AVAILABLE_DEFAULT_VALUE) {
            totalLaunchesAvailable = TOTAL_LAUNCHES_AVAILABLE_DEFAULT_VALUE;
        } else if (totalLaunchesAvailable < 0) {
            totalLaunchesAvailable = 0;
        }
    }

    //Функция показа рекламы и добавления запусков в награду
    public int callBannerAd() {
        Globals.game.showInterstitial();
        totalLaunchesAvailable += BANNER_LAUNCHES;
        checkBounds();
        return totalLaunchesAvailable;
    }

    public int callInterstitialAd() {
        Globals.game.showRewarded();
        totalLaunchesAvailable += INTERSTETIAL_LAUNCHES;
        checkBounds();
        return totalLaunchesAvailable;
    }

    //Вызывается при запуске
    public int launch() {
        totalLaunchesAvailable--;
        checkBounds();
        return totalLaunchesAvailable;
    }

    public void dispose() {
        foodPrefs.putInteger("totalLaunches", totalLaunchesAvailable);
        foodPrefs.flush();
    }
}

package steelUnicorn.laplacity.ui;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import steelUnicorn.laplacity.core.Globals;

public class CatFood {
    // Total launches available
    public static final int TOTAL_LAUNCHES_AVAILABLE_DEFAULT_VALUE = 10;
    public int totalLaunchesAvailable;
    //prefs
    private Preferences foodPrefs;

    //CatFood UI
    public CatFoodUI foodInterface;

    public CatFood() {
        //подгрузка из prefs еды кота
        foodPrefs = Gdx.app.getPreferences("CatFood");

        if (foodPrefs.contains("totalLaunches")) {
            totalLaunchesAvailable = foodPrefs.getInteger("totalLaunches");
        } else {
            totalLaunchesAvailable = TOTAL_LAUNCHES_AVAILABLE_DEFAULT_VALUE;
        }

        Skin skin = Globals.assetManager.get("ui/uiskin.json", Skin.class);
        foodInterface = new CatFoodUI(totalLaunchesAvailable, skin);
    }


    public void update() {
        if (totalLaunchesAvailable > TOTAL_LAUNCHES_AVAILABLE_DEFAULT_VALUE) {
            totalLaunchesAvailable = TOTAL_LAUNCHES_AVAILABLE_DEFAULT_VALUE;
        } else if (totalLaunchesAvailable < 0) {
            totalLaunchesAvailable = 0;
        }

        foodInterface.update(totalLaunchesAvailable);
    }

    public void dispose() {
        //уничтожения всего класса
        foodPrefs.putInteger("totalLaunches", totalLaunchesAvailable);
        foodPrefs.flush();
    }
}

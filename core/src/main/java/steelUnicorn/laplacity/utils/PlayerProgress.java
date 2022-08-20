package steelUnicorn.laplacity.utils;

import static steelUnicorn.laplacity.core.LaplacityAssets.sectionLevels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;

/**
 * Класс для оперирования прогрессом игрока. Сохраняет прогресс в preferences в виде json объекта
 * Подгружает оттуда же. Если прогресса в Preferences нету, то создается массив нулей.
 *
 * Класс содержит функцию levelFinished которая принимает секцию уровень и количество звезд, и
 * сохраняет прогресс.
 *
 * Прогресс хранится в виде массива массивов чисел: (секции - уровни - количество звезд)
 * -1 - уровень заблокирован
 * 0 1 2 3 - количество собранных на уровне звезд
 *
 *
 */
public class PlayerProgress {
    private final Preferences prefs;
    private Array<Array<Integer>> progress;
    private final Json json;

    public PlayerProgress() {
        json = new Json();

        prefs = Gdx.app.getPreferences("player_progress");

        initProgress();

        Gdx.app.log("Progress", progress.toString());
    }

    private void initProgress() {
        //Создаем массив исходя из текущих уровней (на случай если добавлены новые)
        progress = new Array<>();

        for (Array<Texture> lvls : sectionLevels) {
            //Каждое вхождение в sectionLevels это секция. entry.value - уровни
            Array<Integer> sectionProgress = new Array<>();
            for (int i = 0; i < lvls.size; i++) {
                sectionProgress.add(0);
            }

            progress.add(sectionProgress);
        }

        //Переносим старый прогресс в новый
        if (prefs.contains("progress")) {
            @SuppressWarnings("unchecked")
			Array<Array<Integer>> previousProgress =
                    json.fromJson(Array.class, prefs.getString("progress"));
            //copy previous progress to current
            for (int secNumber = 0; secNumber < previousProgress.size; secNumber++) {
                for (int lvlNumber = 0; lvlNumber < previousProgress.get(secNumber).size; lvlNumber++) {
                    if (progress.size > secNumber) {
                        if (progress.get(secNumber).size > lvlNumber) {
                            progress.get(secNumber)
                                    .set(lvlNumber, previousProgress.get(secNumber).get(lvlNumber));
                        }
                    }
                }
            }
        }
    }

    public void levelFinished(int section, int level, int stars) {
        if (progress.size < section || progress.get(section - 1).size < level) {
            Gdx.app.error("Progress", "Wrong section or level number");
            return;
        }

        //set new progress to current level
        if (progress.get(section - 1).get(level - 1) < stars) { //Если звезд собрано больше чем было
            progress.get(section - 1).set(level - 1, stars);
        }
        //open next level
        openNextProgress(section, level);

        Gdx.app.log("Progress", progress.toString());
    }

    private void openNextProgress(int section, int level) {
        if (level < progress.get(section - 1).size) {   //Если в секции есть следующий уровень
            if (progress.get(section - 1).get(level) == -1) {   //Если закрыт
                progress.get(section - 1).set(level, 0);
            }
        } else if (section < progress.size){    //Если есть следующая секция
            if (progress.get(section).size > 0 && progress.get(section).get(0) == -1) {
                progress.get(section).set(0, 0);
            }
        }
    }

    public int getProgress(int section, int level) {
        if (progress.size < section || progress.get(section - 1).size < level) {
            Gdx.app.error("Progress", "Wrong section or level number");
            return -1;
        }

        return progress.get(section - 1).get(level - 1);
    }

    public void dispose() {
        prefs.putString("progress", json.toJson(progress));
        prefs.flush();
    }
}

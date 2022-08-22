package steelUnicorn.laplacity.utils;

import static steelUnicorn.laplacity.core.LaplacityAssets.sectionLevels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SerializationException;

import steelUnicorn.laplacity.core.Globals;


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
    public int starsCollected = 0;

    private final Preferences prefs;
    private Array<SectionProgress> progress;

    private static final int[] starsToOpenSection = new int[]{0, 5, 25, 55, 85, 115, 145};

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

        for (int i = 0; i < sectionLevels.size; i++) {
            //Каждое вхождение в sectionLevels это секция. entry.value - уровни
            Array<Integer> levelStars = new Array<>();
            for (int j = 0; j < sectionLevels.get(i).size; j++) {
                levelStars.add((i == 0 && j == 0) ? 0 : -1);
            }
            //первая секция всегда открыта
            progress.add(new SectionProgress(levelStars, i == 0, starsToOpenSection[i]));
        }

        //Переносим старый прогресс в новый
        if (prefs.contains("progress")) {
            try {
                @SuppressWarnings("unchecked")
                Array<SectionProgress> previousProgress =
                        json.fromJson(Array.class, prefs.getString("progress"));
                for (int section = 0; section < previousProgress.size && section < progress.size; section++) {
                    progress.get(section).copy(previousProgress.get(section));
                    starsCollected += progress.get(section).getStars();
                }
            } catch (SerializationException ex) {
                Gdx.app.log("Progress", "Old progress not valid: " + ex.toString());
            }
        }
    }

    public void levelFinished(int section, int level, int stars) {
        if (progress.size < section || progress.get(section - 1).levelStars.size < level) {
            Gdx.app.error("Progress", "Wrong section or level number");
            return;
        }

        //set new progress to current level
        if (progress.get(section - 1).getLevelStars(level) < stars) { //Если звезд собрано больше чем было
            starsCollected += (stars - progress.get(section - 1).getLevelStars(level)); //добавляем доп звезды
            progress.get(section - 1).setLevelStars(level, stars);
        }
        //open next level
        openNextProgress(section, level);

        Gdx.app.log("Progress", progress.toString());
    }

    private void openNextProgress(int section, int level) {
        if (level < progress.get(section - 1).levelStars.size) {   //Если в секции есть следующий уровень
            if (!progress.get(section - 1).isLevelOpened(level + 1)) {   //Если закрыт
                progress.get(section - 1).openLevel(level + 1);
            }
        } else if (section < progress.size){    //Если есть следующая секция
            if (progress.get(section).levelStars.size > 0 && !progress.get(section).isLevelOpened(1)) {
                progress.get(section).openLevel(1);
            }
        }
    }

    public int getProgress(int section, int level) {
        if (progress.size < section || progress.get(section - 1).levelStars.size < level) {
            Gdx.app.error("Progress", "Wrong section or level number");
            return -1;
        }

        return progress.get(section - 1).getLevelStars(level);
    }

    public SectionProgress getSectionProgress(int section) {
        return progress.get(section - 1);
    }

    public void dispose() {
        if (!Globals.LEVEL_DEBUG) { //прогресс не сохраняется в случае отладки уровней
            prefs.putString("progress", json.toJson(progress));
            prefs.flush();
        }
    }


    public static class SectionProgress {
        private Array<Integer> levelStars;
        private boolean isOpened;
        private int starsToOpen;

        public SectionProgress() {}

        public SectionProgress(Array<Integer> levelStars, boolean isOpened, int starsToOpen) {
            this.levelStars = levelStars;
            this.isOpened = isOpened;
            this.starsToOpen = starsToOpen;
        }

        public boolean isOpened() {
            return isOpened;
        }

        public void setOpened(boolean opened) {
            isOpened = opened;
        }

        public int getStarsToOpen() {
            return starsToOpen;
        }

        public int getLevelStars(int level) {
            return levelStars.get(level - 1);
        }

        public void setLevelStars(int level, int stars) {
            levelStars.set(level - 1, stars);
        }

        public int getStars() {
            int result = 0;
            for (int stars : levelStars) {
                if (stars > 0) {
                    result += stars;
                }
            }
            return result;
        }

        public boolean isLevelOpened(int level) {
            return !(levelStars.get(level - 1) == -1);
        }

        public void openLevel(int level) {
            if (levelStars.get(level - 1) < 0) {
                levelStars.set(level - 1, 0);
            }
        }

        public void copy(SectionProgress newProgress) {
            isOpened = newProgress.isOpened;
            for (int i = 0; i < levelStars.size && i < newProgress.levelStars.size; i++) {
                levelStars.set(i, newProgress.levelStars.get(i));
            }
        }

        @Override
        public String toString() {
            return "SectionProgress{" +
                    "levelStars=" + levelStars +
                    ", isOpened=" + isOpened +
                    ", starsToOpen=" + starsToOpen +
                    "}\n";
        }
    }
}

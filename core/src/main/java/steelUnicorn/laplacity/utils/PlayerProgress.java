package steelUnicorn.laplacity.utils;

import static steelUnicorn.laplacity.core.LaplacityAssets.sectionLevels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import steelUnicorn.laplacity.core.Globals;


/**
 * Класс для обработки прогресса игрока.
 * Сохраняет и загружает прогресс в виде json строки, используя Preferences.
 * <ul>
 *  <li>Считает количество собранных звезд игроком и количество звезд для открытия секций.</li>
 *  <li>Хранит прогресс в массиве объектов SectionProgress.</li>
 *  <li>Хранит флаг isNewUser для показа комикса при нажатии на play.</li>
 * </ul>
 * @see Preferences
 */
public class PlayerProgress {
    private final Json json;
    private final Preferences prefs;

    private static final int[] starsToOpenSection = new int[]{0, 4, 24, 44, 66, 88, 106};
    public int starsCollected = 0;

    private Array<SectionProgress> progress;
    private boolean isNewUser;

    /**
     * Создание полей, подгрузка и сохранение прогресса
     */
    public PlayerProgress() {
        json = new Json();
        prefs = Gdx.app.getPreferences("player_progress");

        initProgress();
        isNewUser = prefs.getBoolean("isNewUser", true);

        saveProgress();
    }

    public boolean isNewUser() {
        return isNewUser;
    }
    public void setNewUser(boolean newUser) {
        isNewUser = newUser;
        prefs.putBoolean("isNewUser", isNewUser);
        prefs.flush();
    }

    /**
     * Создает и заполняет прогресс.
     * Изначально создается массив закрытых уровней на основе размеров sectionLevels
     * (т.е. по текстурам уровней). Далее старый прогресс из Preferences переносится в созданный.
     */
    private void initProgress() {
        progress = new Array<>();

        for (int sec = 1; sec <= sectionLevels.size; sec++) {
            //Создаем количество звезд собранных в каждом уровне
            Array<Integer> levelStars = new Array<>();
            for (int lvl = 1; lvl <= sectionLevels.get(sec - 1).size; lvl++) {
                levelStars.add((sec == 1 && lvl == 1) ? 0 : -1);
            }
            //первая секция всегда открыта
            progress.add(new SectionProgress(
                    levelStars, sec == 1, starsToOpenSection[sec - 1]));
        }

        //Переносим старый прогресс в новый
        if (prefs.contains("progress")) {
            try {
                @SuppressWarnings("unchecked")
                Array<SectionProgress> previousProgress =
                        json.fromJson(Array.class, prefs.getString("progress"));
                for (int sec = 1; sec <= previousProgress.size && sec <= progress.size; sec++) {
                    progress.get(sec - 1).copy(previousProgress.get(sec - 1));
                    starsCollected += progress.get(sec - 1).getStars();
                }
            } catch (Exception ex) {
                Gdx.app.error("Progress", "Old progress not valid: " + ex);
            }
        }
    }

    /**
     * Обрабатывает прохождение уровня, сохраняя новое количество звезд и открывая следующий
     * уровень в прогрессе. Звезды сохраняются если их собрано больше чем в предыдущем уровне.
     *
     * @param section номер секции пройденного уровня
     * @param level номер пройденного уровня
     * @param stars количество звезд собранных на уровне
     */
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

        saveProgress();
    }

    /**
     * Открывает следующий уровень в секции.
     *
     * @param section номер секции
     * @param level номер уровня. Открывается следующий!
     *
     * @see SectionProgress#openLevel(int)
     */
    private void openNextProgress(int section, int level) {
        if (level < progress.get(section - 1).levelStars.size) {
            progress.get(section - 1).openLevel(level + 1);
        }
    }

    /**
     * Возвращает количество звезд в уровне.
     * @param section номер секция
     * @param level номер уровня
     * @return количество собранных звезд на уровне
     */
    public int getProgress(int section, int level) {
        if (progress.size < section || progress.get(section - 1).levelStars.size < level) {
            Gdx.app.error("Progress", "Wrong section or level number");
            return -1;
        }

        return progress.get(section - 1).getLevelStars(level);
    }

    /**
     * Возвращает прогресс в секции объектом SectionProgress.
     * @param section номер секции
     * @return объект прогресса секции
     * @see SectionProgress
     */
    public SectionProgress getSectionProgress(int section) {
        return progress.get(section - 1);
    }

    /**
     * Записывает json строку с объектом прогресса в Preferences.
     */
    public void saveProgress() {
        prefs.putString("progress", json.toJson(progress));
        prefs.flush();
    }

    /**
     * Оперирование прогрессом секции. Объект прогресса хранит в себе:
     * <ul>
     *     <li>Массив чисел со звездами в каждом уровне</li>
     *     <li>Открыта ли секция</li>
     *     <li>Количество звезд для открытия секции</li>
     * </ul>
     */
    public static class SectionProgress {
        private Array<Integer> levelStars;
        private boolean isOpened;
        private int starsToOpen;

        /**
         * По умолчанию нужен для чтения json формата.
         */
        public SectionProgress() {}

        /**
         * Конструктор принимающий поля класса.
         * @param levelStars количество собранных звезд в каджом уровне
         * @param isOpened открыта ли секция
         * @param starsToOpen количество звезд для открытия секции
         */
        public SectionProgress(Array<Integer> levelStars, boolean isOpened, int starsToOpen) {
            this.levelStars = levelStars;
            this.isOpened = isOpened;
            this.starsToOpen = starsToOpen;
        }

        public boolean isOpened() {
            return isOpened;
        }

        /**
         * Открывает секцию и первый уровень.
         * @param opened открыть ли секцию?
         */
        public void setOpened(boolean opened) {
            isOpened = opened;
            if (isOpened) {
                openLevel(1);
            }
            Globals.progress.saveProgress();
        }

        public int getStarsToOpen() {
            return starsToOpen;
        }

        private int getLevelStars(int level) {
            return levelStars.get(level - 1);
        }

        private void setLevelStars(int level, int stars) {
            levelStars.set(level - 1, stars);
            Globals.progress.saveProgress();
        }

        /**
         * Считает количество звезд собранных в секции.
         * @return количество звезд в секции.
         */
        private int getStars() {
            int result = 0;
            for (int stars : levelStars) {
                if (stars > 0) {
                    result += stars;
                }
            }
            return result;
        }

        /**
         * Открывает уровень если он закрыт.
         * @param level номер уровня
         */
        private void openLevel(int level) {
            if (levelStars.get(level - 1) < 0) {
                levelStars.set(level - 1, 0);
                Globals.progress.saveProgress();
            }
        }

        /**
         * Переносит старый прогресс в новый
         * @param oldProgress старый прогресс
         */
        private void copy(SectionProgress oldProgress) {
            isOpened = oldProgress.isOpened;
            for (int i = 0; i < levelStars.size && i < oldProgress.levelStars.size; i++) {
                levelStars.set(i, oldProgress.levelStars.get(i));
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

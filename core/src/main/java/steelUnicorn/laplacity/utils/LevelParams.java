package steelUnicorn.laplacity.utils;

import com.badlogic.gdx.utils.Array;

/**
 * Класс хранения параметров уровня. Массив параметров по секциям хранится в классе LevelParser
 * где он подгружается из папки levelparams из json файлов.
 *
 * Параметры:
 * backIds - массив id бекграундов
 * levelHints - массив подсказок
 */
public class LevelParams {
    private Array<Integer> backIds; //массив фонов
    private Array<Hint> levelHints;

    public Array<Integer> getBackIds() {
        return backIds;
    }

    public void setBackIds(Array<Integer> backId) {
        this.backIds = backId;
    }

    public Array<Hint> getLevelHints() {
        return levelHints;
    }

    public void setLevelHints(Array<Hint> levelHints) {
        this.levelHints = levelHints;
    }

    @Override
    public String toString() {
        return "{" +
                "backIds=" + backIds +
                ", levelHints=" + levelHints +
                "}\n";
    }

    /**
     * Класс подсказки, хранит в себе координаты и id подсказки в массиве
     * HINTS в LaplacityAssets.
     */
    public static class Hint {
        private float x;
        private float y;
        private int hintId;

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public int getHintId() {
            return hintId;
        }

        @Override
        public String toString() {
            return "Hint{" +
                    "x=" + x +
                    ", y=" + y +
                    ", hintId=" + hintId +
                    '}';
        }
    }
}

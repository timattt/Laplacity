package steelUnicorn.laplacity.utils;

import com.badlogic.gdx.utils.Array;

/**
 * Хранение параметров уровня.
 * Массив параметров по секциям и уровням хранится в классе LevelsParamsParser.
 * <ul>Параметры:
 *  <li>backIds - массив id бекграундов</li>
 *  <li>levelHints - массив подсказок на уровне</li>
 * </ul>
 * @see LevelsParamsParser
 */
public class LevelParams {
    private Array<Integer> backIds;
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
     * Хранение подсказок в виде координат и id подсказки в HINTS в LaplacityAssets
     *
     * @see steelUnicorn.laplacity.core.LaplacityAssets
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

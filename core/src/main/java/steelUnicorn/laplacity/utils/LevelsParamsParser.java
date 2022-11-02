package steelUnicorn.laplacity.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

/**
 * Хранение и подгрузка параметров уровней.
 * Параметры хранятся в виде двумерного массива, первый индекс - секция, второй - уровень.
 *
 * @see LevelParams
 */
public class LevelsParamsParser {
    public static Array<Array<LevelParams>> levelsParams; //first index is section second is level

    /**
     * Заполняет массив параметров.
     * Параметры хранятся в виде файлов assets/levelparams/sectionN/levelNN.json.
     *
     * @see LevelParams
     */
    public static void parseParams() {
        levelsParams = new Array<>();
        Json json = new Json();

        FileHandle[] sections = Gdx.files.internal("levelparams/").list();
        for (FileHandle section : sections) {
            if (section.isDirectory()) {
                FileHandle[] lvlFiles = section.list();
                if (lvlFiles.length > 0) {
                    Array<LevelParams> lvlParams = new Array<>();
                    for (FileHandle lvlJson : lvlFiles) {
                        lvlParams.add(json.fromJson(LevelParams.class, lvlJson));
                    }

                    levelsParams.add(lvlParams);
                }
            }
        }
    }

    /**
     * Возвращает параметры уровня.
     * @param section номер уровня
     * @param level номер секции
     * @return объект LevelParams - параметры уровня.
     *
     * @see LevelParams
     */
    public static LevelParams getParams(int section, int level) {
        if (section > levelsParams.size) {
            section = 1;
        }

        if (level > levelsParams.get(section - 1).size) {
            level = 1;
        }

        return levelsParams.get(section - 1).get(level - 1);
    }
}

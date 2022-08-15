package steelUnicorn.laplacity.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;


/**
 * Класс содержит OrderedMap где ключ - номер секции, а значение - массив путей до png файлов
 *
 * Также есть функция, которая парсит папку levels/
 * Каждая подпапка в levels должна именть название sectionN где N - номер секции
 * Каждый файл в секции иметь название levelN.png где N - номер уровня формата %02d
 *
 * LevelsParser подгружает ассеты
 *
 * Так же класс содержит массив массивов параметров уровня. Первый индекс это порядок секции,
 * второй - номер уровня, а значение - его параметры, которые подгружаются из json файлов в папке
 * levelparams/sectionN для каждой секции
 *
 */
public class LevelsParser {
    //Основная функция, возвращает
    public static Array<Array<LevelParams>> levelParams; //first index is section second is level

    public static void parseParams() {
        levelParams = new Array<>();
        Json json = new Json();

        FileHandle[] sections = Gdx.files.internal("levelparams/").list();
        for (FileHandle section : sections) {
            if (section.isDirectory()) {
                FileHandle[] backJsons = section.list();
                if (backJsons.length > 0) {
                    Array<LevelParams> secBackIds = new Array<>();
                    for (FileHandle backJson : backJsons) {
                        secBackIds.add(json.fromJson(LevelParams.class, backJson));
                    }

                    levelParams.add(secBackIds);
                }
            }
        }

        Gdx.app.log("Parse Params", levelParams.toString());
    }

    public static LevelParams getParams(int section, int level) {
        if (section > levelParams.size) {
            section = 1;
        }

        if (level > levelParams.get(section - 1).size) {
            level = 1;
        }

        return levelParams.get(section - 1).get(level - 1);
    }
}

package steelUnicorn.laplacity.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedMap;

import steelUnicorn.laplacity.core.Globals;

/**
 * Класс содержит OrderedMap где ключ - номер секции, а значение - массив путей до png файлов
 *
 * Также есть функция, которая парсит папку levels/
 * Каждая подпапка в levels должна именть название sectionN где N - номер секции
 * Каждый файл в секции иметь название levelN.png где N - номер уровня формата %02d
 */
public class LevelsParser {
    //Основная функция, возвращает
    public static OrderedMap<Integer, Array<String>> sectionLevelsPaths;

    public static void parseSections() {
        if (sectionLevelsPaths == null) {
            FileHandle[] sectionFolders = Gdx.files.internal("levels/").list();


            sectionLevelsPaths = new OrderedMap<>();
            //Проходимся по папкам
            int sectionNumber = 1;
            for (FileHandle section : sectionFolders) {
                if (section.isDirectory()) { //Если это действительно папка секции
                    //инициализация массива путей
                    Array<String> paths = new Array<>(Globals.LEVELS_PER_SECTION);

                    //записываем пути до файлов с tilemap уровня
                    for (FileHandle lvl : section.list()) {
                        int levelNumber = 1;
                        if (lvl.extension().equals("png")
                                && levelNumber <= Globals.LEVELS_PER_SECTION) {
                            paths.add(lvl.path());
                            levelNumber++;
                        }
                    }
                    sectionLevelsPaths.put(sectionNumber++, paths);
                }
            }
        }
    }
}

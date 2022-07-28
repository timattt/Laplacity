package steelUnicorn.laplacity.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.Globals;

/**
 * Класс содержит OrderedMap где ключ - номер секции, а значение - массив путей до png файлов
 *
 * Также есть функция, которая парсит папку levels/
 * Каждая подпапка в levels должна именть название sectionN где N - номер секции
 * Каждый файл в секции иметь название levelN.png где N - номер уровня формата %02d
 *
 * LevelsParser подгружает ассеты
 */
public class LevelsParser {
    //Основная функция, возвращает
    public static OrderedMap<Integer, Array<String>> sectionLevelsPaths;

    public static void parseSections() {
        if (sectionLevelsPaths == null) {
            FileHandle[] sectionFolders = Gdx.files.internal("levels/").list();

            sectionLevelsPaths = new OrderedMap<>();
            Globals.TOTAL_LEVELS_AVAILABLE = 0;
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
                            Globals.TOTAL_LEVELS_AVAILABLE++;
                        }
                    }
                    sectionLevelsPaths.put(sectionNumber++, paths);
                }
            }
        }
    }

    public static void loadAssets(AssetManager assetManager) {
        if (sectionLevelsPaths == null) {
            parseSections();
        }

        ObjectMap.Entries<Integer, Array<String>> entries = sectionLevelsPaths.iterator();

        while (entries.hasNext()) {
            //пара ключ значение из словаря
            ObjectMap.Entry<Integer, Array<String>> entry = entries.next();
            //Значение - массив путей
            for (String path : entry.value) {
                assetManager.load(path, Texture.class);
            }

        }
    }
}

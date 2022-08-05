package steelUnicorn.laplacity.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;

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
    public static Array<Array<LevelParams>> levelParams; //first index is section second is level

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
                    int levelNumber = 1;
                    for (FileHandle lvl : section.list()) {
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

        if (levelParams == null) {
            parseParams();
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

    public static class LevelParams {
        private int backId;

        public LevelParams() {
        }

        public LevelParams(int backId) {
            this.backId = backId;
        }

        public int getBackId() {
            return backId;
        }

        public void setBackId(int backId) {
            this.backId = backId;
        }

        @Override
        public String toString() {
            return "LevelParams{" +
                    "backId=" + backId +
                    '}';
        }
    }
}

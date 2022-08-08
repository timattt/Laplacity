package steelUnicorn.laplacity.utils;

import static steelUnicorn.laplacity.utils.LevelsParser.sectionLevelsPaths;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class PlayerProgress {
    private final Preferences prefs;
    private Array<Array<Integer>> progress;
    private final Json json;

    public PlayerProgress() {
        json = new Json();

        prefs = Gdx.app.getPreferences("player_progress");
        if (prefs.contains("progress")) {
            progress = json.fromJson(Array.class, prefs.getString("progress"));
        } else {
            initProgress();
        }

        Gdx.app.log("Progress", progress.toString());
    }

    private void initProgress() {
        progress = new Array<>();

        for (ObjectMap.Entry<Integer, Array<String>> entry : sectionLevelsPaths) {
            //Каждое вхождение в sectionLevels это секция. entry.value - уровни
            Array<Integer> sectionProgress = new Array<>();
            for (int i = 0; i < entry.value.size; i++) {
                sectionProgress.add(0);
            }

            progress.add(sectionProgress);
        }
    }

    public void levelFinished(int section, int level, int stars) {
        if (progress.size < section) {
            Gdx.app.error("Progress", "Wrong section");
            return;
        }

        if (progress.get(section - 1).size < level) {
            Gdx.app.error("Progress", "Wrong level");
            return;
        }

        if (progress.get(section - 1).get(level - 1) < stars) {
            progress.get(section - 1).set(level - 1, stars);
        }

        Gdx.app.log("Progress", progress.toString());
    }

    public void dispose() {
        prefs.putString("progress", json.toJson(progress));
        prefs.flush();
    }
}

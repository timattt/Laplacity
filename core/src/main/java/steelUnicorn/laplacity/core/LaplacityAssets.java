package steelUnicorn.laplacity.core;

import java.util.Random;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

import steelUnicorn.laplacity.utils.Settings;

public class LaplacityAssets {
    // instance трека, который играет в данный момент.
    // Music - очень тяжёлый класс, поэтому при переключении треков его надо диспозить и грузить заново
    public static Music music;
    private static String currentMusic;

    public static FileHandle[] levelTracks;
    private static Random trackRandomizer = new Random();
    private static int currentTrack = 0;

    public static Sound clickSound; // звук нажатия на кнопки
    public static Sound lightClickSound;
    public static Sound bumpSound; // звук удара о стену
    public static Sound placeSound; // звук размещения частицы
	public static Sound hurtSound; // звук касания смертельной стены

    public static void getAssets() {
        clickSound = Globals.assetManager.get("sounds/click1.ogg");
        lightClickSound = Globals.assetManager.get("sounds/simple_signal.ogg");
        bumpSound = Globals.assetManager.get("sounds/sword_hit_b.ogg");
        placeSound = Globals.assetManager.get("sounds/bow_draw.ogg");
        hurtSound = Globals.assetManager.get("sounds/fire_burst.ogg");
    }

    public static void changeTrack(String name) {
        if (currentMusic != null)
            Globals.assetManager.unload(currentMusic);
        currentMusic = name;
        Globals.assetManager.load(currentMusic, Music.class);
        Globals.assetManager.finishLoadingAsset(currentMusic);
        music = Globals.assetManager.get(currentMusic);
        music.setLooping(true);
        music.setVolume(Settings.getMusicVolume());
        music.play();
    }

    public static void setLevelTrack() {
        if (levelTracks == null) {
            return;
        } else if (levelTracks.length == 0) {
            return;
        }
        int index = trackRandomizer.nextInt(levelTracks.length);
        while (index == currentTrack)
            index = trackRandomizer.nextInt(levelTracks.length);
        currentTrack = index;
        changeTrack("music/levels/" + levelTracks[currentTrack].name());
    }

    public static void playSound(Sound sound) {
        if (Settings.getSoundVolume() != 0) {
            long id = sound.play(); // трюк с id может не работать на ведроиде
            sound.setVolume(id, Settings.getSoundVolume());
        }
    }
}

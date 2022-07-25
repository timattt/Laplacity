package steelUnicorn.laplacity.core;

import java.util.Random;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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
    public static Sound bumpStructureSound; // звук удара о структуру

	public static Texture BARRIER_TEXTURE;
	public static Texture DEADLY_TEXTURE;
	public static TextureRegion[] BARRIER_REGIONS;
	public static TextureRegion[][] DEADLY_REGIONS;
	
    public static void getAssets() {
        clickSound = Globals.assetManager.get("sounds/click.ogg");
        lightClickSound = Globals.assetManager.get("sounds/light_click.ogg");
        bumpSound = Globals.assetManager.get("sounds/bump_barrier.ogg");
        placeSound = Globals.assetManager.get("sounds/place.ogg");
        hurtSound = Globals.assetManager.get("sounds/bump_deadly.ogg");
        bumpStructureSound = Globals.assetManager.get("sounds/bump_structure.ogg");
        
        BARRIER_TEXTURE = Globals.assetManager.get("textures/barrier.png");
        DEADLY_TEXTURE = Globals.assetManager.get("textures/deadly.png");
        
        loadTextureRegions();
    }
    
    private static void loadTextureRegions() {
    	cut(BARRIER_TEXTURE, BARRIER_REGIONS = new TextureRegion[4]);
    	cut(DEADLY_TEXTURE, DEADLY_REGIONS = new TextureRegion[3][2]);
    }
    
    private static void cut(Texture from, TextureRegion[] result) {
    	for (int i = 0; i < result.length; i++) {
    		result[i] = new TextureRegion(from, i * from.getWidth() / result.length, 0, from.getWidth() / result.length, from.getHeight());
    	}
    }
    
    private static void cut(Texture from, TextureRegion[][] result) {
    	for (int i = 0; i < result.length; i++) {
    		for (int j = 0; j < result[i].length; j++) {
    			result[i][j] = new TextureRegion(from, i * from.getWidth() / result.length, j * from.getHeight() / result[i].length, from.getWidth() / result.length, from.getHeight() / result[i].length);
    		}
    	}
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
            sound.play();
        }
    }
}

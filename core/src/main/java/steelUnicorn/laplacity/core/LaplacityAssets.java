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

    // SOUNDS
    public static Sound clickSound; // звук нажатия на кнопки
    public static Sound lightClickSound;
    public static Sound bumpSound; // звук удара о стену
    public static Sound placeSound; // звук размещения частицы
	public static Sound hurtSound; // звук касания смертельной стены
    public static Sound bumpStructureSound; // звук удара о структуру
    public static Sound genStartSound;
    public static Sound sprayStartSound;
    public static Sound popupSound;
    public static Sound annihilationSound;
    public static Sound spraySound;

    // TILES
	public static Texture BARRIER_TEXTURE;
	public static Texture DEADLY_TEXTURE;
	public static Texture TRAMPOLINE_TEXTURE;
	public static Texture WALL_TEXTURE;
	public static TextureRegion[] WALL_REGIONS;
	public static TextureRegion[] BARRIER_REGIONS;
	public static TextureRegion[][] DEADLY_REGIONS;
	public static TextureRegion TRAMPOLINE_REGION;
	
	// BACKGROUNDS
	public static Texture[][] BACKGROUNDS; 
	public static Texture SPACE_BACKGROUND;
	
	// PARTICLES
	public static Texture PARTICLES_TEXTURE;
	public static TextureRegion[][] PARTICLES_REGIONS;
	
	// DENSITY
	public static Texture DENSITY;
	public static TextureRegion[][] DENSITY_REGIONS;
	
	// OBJECTS
	public static Texture GIFT_TEXTURE;
	public static Texture CAT_TEXTURE;
	public static TextureRegion CAT_REGION;
	
	// STRUCTURES
	public static Texture BLADES_TEXTURE;
	public static TextureRegion[] BLADES_REGIONS;
	public static Texture CELERATORS_TEXTURE;
	public static TextureRegion[] CELERATORS_REGIONS;
	public static Texture HATCH_TEXTURE;
	public static TextureRegion[][] HATCH_REGIONS;
	public static Texture MOVING_WALL_TEXTURE;
	public static TextureRegion[][] MOVING_WALL_STRUCTURE_REGIONS;
	public static Texture GLASS_TEXTURE;
	public static TextureRegion[] GLASS_REGIONS;
	public static Texture RED_LED_TEXTURE;
	public static TextureRegion[] RED_RED_REGIONS;
	public static Texture HINGE_TEXTURE;
	public static TextureRegion[][] HINGE_REGIONS;
	
    public static void getAssets() {
    	// sounds
        clickSound = Globals.assetManager.get("sounds/click.wav");
        lightClickSound = Globals.assetManager.get("sounds/light_click.wav");
        bumpSound = Globals.assetManager.get("sounds/bump_barrier.wav");
        placeSound = Globals.assetManager.get("sounds/place.wav");
        hurtSound = Globals.assetManager.get("sounds/bump_deadly.wav");
        bumpStructureSound = Globals.assetManager.get("sounds/bump_structure.wav");
        genStartSound = Globals.assetManager.get("sounds/generator_start.wav");
        sprayStartSound = Globals.assetManager.get("sounds/spray_start.wav");
        popupSound = Globals.assetManager.get("sounds/popup.wav");
        annihilationSound = Globals.assetManager.get("sounds/annihilation.wav");
        spraySound = Globals.assetManager.get("sounds/spray.wav");
        
        // TILES
        TRAMPOLINE_TEXTURE = Globals.assetManager.get("textures/tiles/trampoline.png");
        BARRIER_TEXTURE = Globals.assetManager.get("textures/tiles/barrier.png");
        DEADLY_TEXTURE = Globals.assetManager.get("textures/tiles/deadly.png");
        WALL_TEXTURE = Globals.assetManager.get("textures/tiles/wall.png"); 
        
        // STRUCTURES
        BLADES_TEXTURE = Globals.assetManager.get("textures/structures/fan.png");
        CELERATORS_TEXTURE = Globals.assetManager.get("textures/structures/celerators.png");
        HATCH_TEXTURE = Globals.assetManager.get("textures/structures/hatch.png");
        MOVING_WALL_TEXTURE = Globals.assetManager.get("textures/structures/structure_thin.png");
        GLASS_TEXTURE = Globals.assetManager.get("textures/structures/glass.png");
        RED_LED_TEXTURE = Globals.assetManager.get("textures/structures/redLed.png");
        HINGE_TEXTURE = Globals.assetManager.get("textures/structures/hinge.png");
        
        // OBJECTS
        GIFT_TEXTURE = Globals.assetManager.get("rigidObjects/gift.png");
        CAT_TEXTURE = Globals.assetManager.get("textures/objects/cat.png");
        PARTICLES_TEXTURE = Globals.assetManager.get("textures/objects/particles.png");
        
        // UTILS
        DENSITY = Globals.assetManager.get("textures/utils/density.png");
        
        loadTextureRegions();
        loadBackgrounds();
    }
    
    private static void loadTextureRegions() {
    	cut(BARRIER_TEXTURE, BARRIER_REGIONS = new TextureRegion[7]);
    	cut(DEADLY_TEXTURE, DEADLY_REGIONS = new TextureRegion[3][2]);
    	cut(PARTICLES_TEXTURE, PARTICLES_REGIONS = new TextureRegion[7][2]);
        cut(DENSITY, DENSITY_REGIONS = new TextureRegion[3][3]);
        cut(WALL_TEXTURE, WALL_REGIONS = new TextureRegion[7]);
        cut(BLADES_TEXTURE, BLADES_REGIONS = new TextureRegion[3]);
        cut(CELERATORS_TEXTURE, CELERATORS_REGIONS = new TextureRegion[3]);
        cut(HATCH_TEXTURE, HATCH_REGIONS = new TextureRegion[5][3]);
        cut(MOVING_WALL_TEXTURE, MOVING_WALL_STRUCTURE_REGIONS = new TextureRegion[10][4]);
        cut(GLASS_TEXTURE, GLASS_REGIONS = new TextureRegion[3]);
        cut(RED_LED_TEXTURE, RED_RED_REGIONS = new TextureRegion[1]);
        cut(HINGE_TEXTURE, HINGE_REGIONS = new TextureRegion[3][2]);
        
        TRAMPOLINE_REGION = new TextureRegion(TRAMPOLINE_TEXTURE);
        CAT_REGION = new TextureRegion(CAT_TEXTURE);
    }
    
    private static void loadBackgrounds() {
    	BACKGROUNDS = new Texture[Globals.TOTAL_SECTIONS][Globals.LEVELS_PER_SECTION];
    	
    	for (int section = 0; section < Globals.TOTAL_SECTIONS; section++) {
    		for (int i = 0; i < Globals.LEVELS_PER_SECTION; i++) {
    			if (i < 9) {
    				BACKGROUNDS[section][i] = Globals.assetManager.get("backgrounds/section" + (section+1) + "/level0" + (i+1) +".png");
    			} else {
    				BACKGROUNDS[section][i] = Globals.assetManager.get("backgrounds/section" + (section+1) + "/level" + (i+1) +".png");
    			}
    		}
    	}
    	
    	SPACE_BACKGROUND = Globals.assetManager.get("backgrounds/SPACE_BACKGROUND.png", Texture.class);
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

    public static void loopSound(Sound sound) {
        if (Settings.getSoundVolume() != 0) {
            sound.loop();
        }
    }
    
    public static void stopSound(Sound sound) {
        sound.stop();
    }
}

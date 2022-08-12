package steelUnicorn.laplacity.core;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

import steelUnicorn.laplacity.utils.Settings;

public class LaplacityAssets {
    // instance трека, который играет в данный момент.
    // Music - очень тяжёлый класс, поэтому при переключении треков его надо диспозить и грузить заново
    public static Music intro;
    public static Music music;
    private static String currentIntro;
    private static String currentMusic;
    private static OnCompletionListener fromIntroToDrop;

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
	public static Array<Texture> BACKGROUNDS;
	public static Texture SPACE_BACKGROUND;
	public static Texture MAIN_MENU_BACKGROUND;
	public static Texture EARTH_BACKGROUND;

	//HINTS
	public static Array<Texture> HINTS;

	// PARTICLES
	public static Texture ELECTRON_TEXTURE;
	public static Texture PROTON_TEXTURE;
	public static TextureRegion[] ELECTRON_REGIONS;
	public static TextureRegion[] PROTON_REGIONS;
	
	// OBJECTS
	public static Texture GIFT_TEXTURE;
	public static Texture CAT_TEXTURE;
	public static TextureRegion[][] CAT_REGIONS;
	
	// STRUCTURES
	public static Texture BLADES_TEXTURE;
	public static TextureRegion[] BLADES_REGIONS;
	public static Texture CELERATORS_TEXTURE;
	public static TextureRegion[] CELERATORS_REGIONS;
	public static Texture HATCH_TEXTURE;
	public static TextureRegion[][] HATCH_REGIONS;
	public static Texture MOVING_WALL_TEXTURE;
	public static TextureRegion[] MOVING_WALL_STRUCTURE_REGIONS;
	public static Texture GLASS_TEXTURE;
	public static TextureRegion[] GLASS_REGIONS;
	public static Texture RED_LED_TEXTURE;
	public static TextureRegion[] RED_RED_REGIONS;
	public static Texture HINGE_TEXTURE;
	public static TextureRegion[] HINGE_REGIONS;
	public static Texture STAR_TEXTURE;
	public static TextureRegion[] STAR_REGIONS;
	public static Texture ELEVATOR_TEXTURE;
	public static TextureRegion[][] ELEVATOR_REGIONS;

    //UI
    public static Skin SKIN;
    public static TextureAtlas ICONS;

    public static void repackAssets() {
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
        MOVING_WALL_TEXTURE = Globals.assetManager.get("textures/structures/moving.png");
        GLASS_TEXTURE = Globals.assetManager.get("textures/structures/glass.png");
        RED_LED_TEXTURE = Globals.assetManager.get("textures/structures/redLed.png");
        HINGE_TEXTURE = Globals.assetManager.get("textures/structures/hinge.png");
        STAR_TEXTURE = Globals.assetManager.get("textures/structures/stars.png");
        ELEVATOR_TEXTURE = Globals.assetManager.get("textures/structures/elevator.png");
        
        // OBJECTS
        GIFT_TEXTURE = Globals.assetManager.get("rigidObjects/gift.png");
        CAT_TEXTURE = Globals.assetManager.get("textures/objects/cat.png");
        ELECTRON_TEXTURE = Globals.assetManager.get("textures/objects/electron.png");
        PROTON_TEXTURE = Globals.assetManager.get("textures/objects/proton.png");
        
        loadTextureRegions();
        loadBackgrounds();
        loadHints();

        SKIN = Globals.assetManager.get("ui/uiskin.json", Skin.class);
        ICONS = Globals.assetManager.get("ui/gameicons/icons.atlas", TextureAtlas.class);
    }
    
    private static void loadTextureRegions() {
    	cut(BARRIER_TEXTURE, BARRIER_REGIONS = new TextureRegion[7]);
    	cut(DEADLY_TEXTURE, DEADLY_REGIONS = new TextureRegion[9][4]);
    	cut(ELECTRON_TEXTURE, ELECTRON_REGIONS = new TextureRegion[7]);
    	cut(PROTON_TEXTURE, PROTON_REGIONS = new TextureRegion[7]);
        cut(WALL_TEXTURE, WALL_REGIONS = new TextureRegion[7]);
        cut_blades(BLADES_TEXTURE, BLADES_REGIONS = new TextureRegion[2]);
        cut(CELERATORS_TEXTURE, CELERATORS_REGIONS = new TextureRegion[1]);
        cut(HATCH_TEXTURE, HATCH_REGIONS = new TextureRegion[5][3]);
        cut(MOVING_WALL_TEXTURE, MOVING_WALL_STRUCTURE_REGIONS = new TextureRegion[4]);
        cut(GLASS_TEXTURE, GLASS_REGIONS = new TextureRegion[5]);
        cut(RED_LED_TEXTURE, RED_RED_REGIONS = new TextureRegion[1]);
        cut(HINGE_TEXTURE, HINGE_REGIONS = new TextureRegion[4]);
        cut(CAT_TEXTURE, CAT_REGIONS = new TextureRegion[5][2]);
        cut(STAR_TEXTURE, STAR_REGIONS = new TextureRegion[8]);
        cut(ELEVATOR_TEXTURE, ELEVATOR_REGIONS = new TextureRegion[3][3]);
        
        TRAMPOLINE_REGION = new TextureRegion(TRAMPOLINE_TEXTURE);
    }
    
    private static void loadBackgrounds() {
    	BACKGROUNDS = new Array<>();

        FileHandle[] backs = Gdx.files.internal("textures/backgrounds/levelbacks/").list();

    	for (FileHandle back : backs) {
            BACKGROUNDS.add(Globals.assetManager.get(back.path(), Texture.class));
        }

    	SPACE_BACKGROUND = Globals.assetManager.get("textures/backgrounds/SPACE_BACKGROUND.png", Texture.class);

        MAIN_MENU_BACKGROUND = Globals.assetManager.get("textures/backgrounds/MAIN_MENU_BACKGROUND.png", Texture.class);
        EARTH_BACKGROUND = Globals.assetManager.get("textures/backgrounds/EARTH_BACKGROUND.png", Texture.class);
    }

    private static void loadHints() {
        HINTS = new Array<>();

        FileHandle[] hints = Gdx.files.internal("textures/hints/").list();

        for (FileHandle hint : hints) {
            HINTS.add(Globals.assetManager.get(hint.path(), Texture.class));
        }

        Gdx.app.log("Hints loaded", HINTS.toString());
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
    
    private static void cut_blades(Texture from, TextureRegion[] result) {
    	result[0] = new TextureRegion(
    			from,
    			0,
    			0,
    			from.getWidth() / 4,
    			from.getHeight());
    	result[1] = new TextureRegion(
    			from,
    			1 * from.getWidth() / 4,
    			0,
    			3 * from.getWidth() / 4,
    			from.getHeight());
    }

    public static void changeTrack(String name) {
        if (currentMusic != null && Globals.assetManager.contains(currentMusic))
            Globals.assetManager.unload(currentMusic);
        if (currentIntro != null && Globals.assetManager.contains(currentIntro))
            Globals.assetManager.unload(currentIntro);
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
        loopTrackWithIntro(levelTracks[currentTrack].name());
    }

    // передавать просто name без префикса
    public static void loopTrackWithIntro(String name) {
        // ставим интро
        if (currentIntro != null && Globals.assetManager.contains(currentIntro))
            Globals.assetManager.unload(currentIntro);
        currentIntro = "music/intros/" + name; 
        Globals.assetManager.load(currentIntro, Music.class);
        Globals.assetManager.finishLoadingAsset(currentIntro);
        intro = Globals.assetManager.get(currentIntro);
        intro.setVolume(Settings.getMusicVolume());
        intro.play();

        // асинхронно грузим и ставим в очередь дроп
        if (currentMusic != null && Globals.assetManager.contains(currentMusic))
            Globals.assetManager.unload(currentMusic);
        currentMusic = "music/levels/" + name;
        Globals.assetManager.load(currentMusic, Music.class);
        fromIntroToDrop = new OnCompletionListener() {
            @Override
            public void onCompletion(Music a) {
                music.play();
            }
        };
        Globals.assetManager.finishLoadingAsset(currentMusic);
        music = Globals.assetManager.get(currentMusic);
        music.setVolume(Settings.getMusicVolume());
        music.setLooping(true);
        intro.setOnCompletionListener(fromIntroToDrop);
        

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

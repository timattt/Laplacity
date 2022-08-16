package steelUnicorn.laplacity.core;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

import steelUnicorn.laplacity.utils.LevelsParser;
import steelUnicorn.laplacity.utils.Settings;

public class LaplacityAssets {
	//levels
	public static Array<Array<Texture>> sectionLevels;

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
	public static Sound trampolineSound;
	public static Sound glassBreakingSound;
	public static Sound glassBumpSound;
	public static Sound starSound;

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
    public static Skin TEXSKIN;
    public static TextureAtlas ICONS;

    public static void repackAssets(AssetManager assetManager) {
		repackLevels(assetManager);

    	// sounds
        clickSound = assetManager.get("sounds/click.wav");
        lightClickSound = assetManager.get("sounds/light_click.wav");
        bumpSound = assetManager.get("sounds/bump_barrier.wav");
        placeSound = assetManager.get("sounds/place.wav");
        hurtSound = assetManager.get("sounds/bump_deadly.wav");
        bumpStructureSound = assetManager.get("sounds/bump_structure.wav");
        genStartSound = assetManager.get("sounds/generator_start.wav");
        sprayStartSound = assetManager.get("sounds/spray_start.wav");
        popupSound = assetManager.get("sounds/popup.wav");
        annihilationSound = assetManager.get("sounds/annihilation.wav");
        spraySound = assetManager.get("sounds/spray.wav");
		trampolineSound = assetManager.get("sounds/trampoline.wav");
		glassBreakingSound = assetManager.get("sounds/glass_break.wav");
		glassBumpSound = assetManager.get("sounds/bump_glass.wav");
		starSound = assetManager.get("sounds/star.wav");
        
        // TILES
        TRAMPOLINE_TEXTURE = assetManager.get("textures/tiles/trampoline.png");
        BARRIER_TEXTURE = assetManager.get("textures/tiles/barrier.png");
        DEADLY_TEXTURE = assetManager.get("textures/tiles/deadly.png");
        WALL_TEXTURE = assetManager.get("textures/tiles/wall.png"); 
        
        // STRUCTURES
        BLADES_TEXTURE = assetManager.get("textures/structures/fan.png");
        CELERATORS_TEXTURE = assetManager.get("textures/structures/celerators.png");
        HATCH_TEXTURE = assetManager.get("textures/structures/hatch.png");
        MOVING_WALL_TEXTURE = assetManager.get("textures/structures/moving.png");
        GLASS_TEXTURE = assetManager.get("textures/structures/glass.png");
        RED_LED_TEXTURE = assetManager.get("textures/structures/redLed.png");
        HINGE_TEXTURE = assetManager.get("textures/structures/hinge.png");
        STAR_TEXTURE = assetManager.get("textures/structures/stars.png");
        ELEVATOR_TEXTURE = assetManager.get("textures/structures/elevator.png");
        
        // OBJECTS
        GIFT_TEXTURE = assetManager.get("rigidObjects/gift.png");
        CAT_TEXTURE = assetManager.get("textures/objects/cat.png");
        ELECTRON_TEXTURE = assetManager.get("textures/objects/electron.png");
        PROTON_TEXTURE = assetManager.get("textures/objects/proton.png");
        
        loadTextureRegions(assetManager);
        loadBackgrounds(assetManager);
        loadHints(assetManager);

        SKIN = assetManager.get("ui/uiskin.json", Skin.class);
        TEXSKIN = assetManager.get("ui/texskin/texskin.json", Skin.class);
        ICONS = assetManager.get("ui/gameicons/icons.atlas", TextureAtlas.class);
    }
    
    private static void loadTextureRegions(AssetManager assetManager) {
    	cut(BARRIER_TEXTURE, BARRIER_REGIONS = new TextureRegion[7]);
    	cut(DEADLY_TEXTURE, DEADLY_REGIONS = new TextureRegion[11][5], true);
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

	private static void repackLevels(AssetManager assetManager) {
		if (LevelsParser.levelParams == null) {
			LevelsParser.parseParams();
		}

		sectionLevels = new Array<>();

		FileHandle[] sectionFolders = Gdx.files.internal("levels/").list();

		Globals.TOTAL_LEVELS_AVAILABLE = 0;
		for (FileHandle folder : sectionFolders) {
			if (folder.isDirectory()) {
				Array<Texture> lvls = new Array<>();

				FileHandle[] files = folder.list();
				for (FileHandle file : files) {
					if (file.extension().equals("png")) {
						lvls.add(assetManager.get(file.path(), Texture.class));
						Globals.TOTAL_LEVELS_AVAILABLE++;
					}
				}

				sectionLevels.add(lvls);
			}
		}
	}
    
    private static void loadBackgrounds(AssetManager assetManager) {
    	BACKGROUNDS = new Array<>();

		FileHandle[] backs = Gdx.files.internal("textures/backgrounds/levelbacks/").list();

		for (FileHandle back : backs) {
			BACKGROUNDS.add(assetManager.get(back.path(), Texture.class));
		}

		SPACE_BACKGROUND = assetManager.get("textures/backgrounds/SPACE_BACKGROUND.png", Texture.class);

		MAIN_MENU_BACKGROUND = assetManager.get("textures/backgrounds/MAIN_MENU_BACKGROUND.png", Texture.class);
		EARTH_BACKGROUND = assetManager.get("textures/backgrounds/EARTH_BACKGROUND.png", Texture.class);
	}

	private static void loadHints(AssetManager assetManager) {
		HINTS = new Array<>();

		FileHandle[] hints = Gdx.files.internal("textures/hints/").list();

		for (FileHandle hint : hints) {
			HINTS.add(assetManager.get(hint.path(), Texture.class));
		}

		Gdx.app.log("Hints loaded", HINTS.toString());
	}
	
	private static void cut(Texture from, TextureRegion[] result) {
		cut(from, result, false);
	}
	
	private static void cut(Texture from, TextureRegion[][] result) {
		cut(from, result, false);
	}
	
	private static void cut(Texture from, TextureRegion[] result, boolean useD) {
		int d = useD ? 1 : 0;
		for (int i = 0; i < result.length; i++) {
			result[i] = new TextureRegion(from, i * from.getWidth() / result.length + d, d, from.getWidth() / result.length - 2 * d, from.getHeight() - 2 * d);
		}
	}
	
	private static void cut(Texture from, TextureRegion[][] result, boolean useD) {
		int d = useD ? 1 : 0;
		
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[i].length; j++) {
				result[i][j] = new TextureRegion(from, i * from.getWidth() / result.length + d, j * from.getHeight() / result[i].length + d, from.getWidth() / result.length - 2 * d, from.getHeight() / result[i].length - 2 * d);
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
		AssetManager assetManager = Globals.game.assetManager;
		
		if (currentMusic != null && assetManager.contains(currentMusic))
			assetManager.unload(currentMusic);
		if (currentIntro != null && assetManager.contains(currentIntro))
			assetManager.unload(currentIntro);
		currentMusic = name;
		assetManager.load(currentMusic, Music.class);
		assetManager.finishLoadingAsset(currentMusic);
		music = assetManager.get(currentMusic);
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
		if (levelTracks[currentTrack].name().charAt(0) == 'a') {
			changeTrack("music/levels/" + levelTracks[currentTrack].name());
		} else {
			loopTrackWithIntro(levelTracks[currentTrack].name());
		}
	}

	// передавать просто name без префикса
	public static void loopTrackWithIntro(String name) {
		AssetManager assetManager = Globals.game.assetManager;
		
		// ставим интро
		if (currentIntro != null && assetManager.contains(currentIntro))
			assetManager.unload(currentIntro);
		currentIntro = "music/intros/" + name; 
		assetManager.load(currentIntro, Music.class);
		assetManager.finishLoadingAsset(currentIntro);
		intro = assetManager.get(currentIntro);
		intro.setVolume(Settings.getMusicVolume());
		intro.play();

		// асинхронно грузим и ставим в очередь дроп
		if (currentMusic != null && assetManager.contains(currentMusic))
			assetManager.unload(currentMusic);
		currentMusic = "music/levels/" + name;
		assetManager.load(currentMusic, Music.class);
		fromIntroToDrop = new OnCompletionListener() {
			@Override
			public void onCompletion(Music a) {
				music.play();
			}
		};
		assetManager.finishLoadingAsset(currentMusic);
		music = assetManager.get(currentMusic);
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

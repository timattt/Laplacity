package steelUnicorn.laplacity.core;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class LaplacityAssets {
    // instance трека, который играет в данный момент.
    // Music - очень тяжёлый класс, поэтому при переключении треков его надо диспозить и грузить заново
    public static Music music;
    AssetDescriptor<Music> currentMusic;

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
}

package steelUnicorn.laplacity.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Класс с функциями для подгрузки и сохранения настроек.
 * Сохранения и загрузка происходит на основе Preferences
 *
 * Функция loadSettings подгружает prefs и выставляет нужные значения статическим переменным
 * отвечающим за значения настроек.
 *
 * Для каждой переменной настройки есть геттеры и сеттеры.
 * В сеттерах настройки сразу сохраняются в preferences
 */
public class Settings {
	private static Preferences prefs;
	private static final String prefsName = "game_settings";

	private static float soundVolume;
	private static float musicVolume;
	public enum VOLUME {
		OFF,
		ON
	}
	private static boolean lightingEnabled;
	private static boolean showFps;

	/**
	 * Функция загружающая настройки используя prefs
	 */
	public static void loadSettings() {
		prefs = Gdx.app.getPreferences(prefsName);

		soundVolume = prefs.getFloat("soundVolume", VOLUME.ON.ordinal());
		musicVolume = prefs.getFloat("musicVolume", VOLUME.ON.ordinal());
		lightingEnabled = prefs.getBoolean("lighting", true);
		showFps = prefs.getBoolean("showFps", false);
	}

	public static float getSoundVolume() {
		return soundVolume;
	}
	public static void setSoundVolume(float soundVolume) {
		Settings.soundVolume = soundVolume;
		prefs.putFloat("soundVolume", soundVolume);
		prefs.flush();
	}

	public static float getMusicVolume() {
		return musicVolume;
	}
	public static void setMusicVolume(float musicVolume) {
		Settings.musicVolume = musicVolume;
		prefs.putFloat("musicVolume", musicVolume);
		prefs.flush();
	}

	public static boolean isLightingEnabled() {
		return lightingEnabled;
	}
	public static void setLighting(boolean lighting) {
		Settings.lightingEnabled = lighting;
		prefs.putBoolean("lighting", lightingEnabled);
		prefs.flush();
	}

	public static boolean isShowFps() {
		return showFps;
	}
	public static void setShowFps(boolean showFps) {
		Settings.showFps = showFps;
		prefs.putBoolean("showFps", showFps);
		prefs.flush();
	}
}

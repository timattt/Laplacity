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
 * Функция saveSettings должна вызываться по завершению приложения и сохранять настройки.
 *
 * Для каждой переменной настройки есть геттеры и сеттеры.
 */
public class Settings {
	private static Preferences prefs;
	private static final String prefsName = "game_settings";

	private static float soundVolume;
	private static float musicVolume;
	public static enum VOLUME {
		OFF,
		ON
	}

	/**
	 * Функция загружающая настройки используя prefs
	 */
	public static void loadSettings() {
		prefs = Gdx.app.getPreferences(prefsName);
		if (prefs.contains("soundVolume")) {
			soundVolume = prefs.getFloat("soundVolume");
		} else {
			soundVolume = 1;
		}

		if (prefs.contains("musicVolume")) {
			musicVolume = prefs.getFloat("musicVolume");
		} else {
			musicVolume = 1;
		}
	}

	/**
	 * Функция сохраняющая настройки
	 */
	public static void saveSettings() {
		prefs.putFloat("soundVolume", soundVolume);
		prefs.putFloat("musicVolume", musicVolume);
	}

	public static float getSoundVolume() {
		return soundVolume;
	}
	public static void setSoundVolume(float soundVolume) {
		Settings.soundVolume = soundVolume;
	}

	public static float getMusicVolume() {
		return musicVolume;
	}
	public static void setMusicVolume(float musicVolume) {
		Settings.musicVolume = musicVolume;
	}
}

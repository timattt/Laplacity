package steelUnicorn.laplacity.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * @brief Class with static settings variables.
 *
 * Setting can be changed in main menu "Setting" tab.
 * soundVolume and musicVolume should be used when sound.play called or as music.setVolume
 *
 */
public class Settings {
	private static Preferences prefs;

	private static float soundVolume;
	private static float musicVolume;

	/**
	 * Uploading settings using preferences
	 */
	public static void loadSettings() {
		prefs = Gdx.app.getPreferences("game_settings");
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
	 * Function that saves settings
	 * Should be called before exiting from app.
	 */
	public static void saveSettings() {
		prefs.putFloat("soundVolume", soundVolume);
		prefs.putFloat("musicVolume", musicVolume);
		prefs.flush();
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

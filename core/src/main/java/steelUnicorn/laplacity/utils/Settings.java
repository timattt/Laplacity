package steelUnicorn.laplacity.utils;

/**
 * @brief Class with static settings variables.
 *
 * Setting can be changed in main menu "Setting" tab.
 * soundVolume and musicVolume should be used when sound.play called or as music.setVolume
 *
 */
public class Settings {
	private static float soundVolume;
	private static float musicVolume;

	//TODO upload settings from file (json?)
	public static void loadSettings() {
		soundVolume = 1;
		musicVolume = 1;
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

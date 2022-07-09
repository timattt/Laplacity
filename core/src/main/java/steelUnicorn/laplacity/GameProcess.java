package steelUnicorn.laplacity;

import steelUnicorn.laplacity.field.LaplacityField;

public class GameProcess {

	// Tilemap
	public static LaplacityField field;
	
	// Methods
	public static boolean isPlaying() {
		return field != null;
	}
	
	
}

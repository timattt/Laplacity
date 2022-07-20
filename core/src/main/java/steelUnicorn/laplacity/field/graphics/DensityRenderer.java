package steelUnicorn.laplacity.field.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.field.LaplacityField;

public class DensityRenderer {

	// Rendering visible density tiles
	private static Pixmap densityPixmap;
	private static Texture densityTexture;
	
	// Utils
	private static final Color tmpColor = new Color();
	
	public static void init() {
		int fieldWidth = LaplacityField.fieldWidth;
		int fieldHeight = LaplacityField.fieldHeight;
		
		densityPixmap = new Pixmap(fieldWidth, fieldHeight, Format.RGBA8888);
		densityPixmap.setBlending(Blending.None);
		densityPixmap.setColor(Color.CLEAR);
		densityPixmap.fill();
		densityTexture = new Texture(densityPixmap);
	}
	
	public static void updateDensity() {
		densityTexture.draw(densityPixmap, 0, 0);
	}
	
	public static void setTileDensity(int x, int y, float val) {
		tmpColor.set(Color.VIOLET);
		tmpColor.a = val / GameProcess.MAX_DENSITY;
		densityPixmap.drawPixel(x, LaplacityField.fieldHeight - y - 1, Color.rgba8888(tmpColor));
	}
	
	public static void render(Batch batch) {
		int fieldWidth = LaplacityField.fieldWidth;
		int fieldHeight = LaplacityField.fieldHeight;
		float tileSize = LaplacityField.tileSize;
		
		batch.begin();
		batch.draw(densityTexture, 0, 0, fieldWidth * tileSize, fieldHeight * tileSize);
		batch.end();
	}
	
	public static void cleanup() {
		if (densityPixmap != null) {
			densityPixmap.dispose();
			densityPixmap = null;
		}
		if (densityTexture != null) {
			densityTexture.dispose();
			densityTexture = null;
		}
	}

}

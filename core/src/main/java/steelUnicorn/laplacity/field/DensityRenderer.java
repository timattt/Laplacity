package steelUnicorn.laplacity.field;

import static steelUnicorn.laplacity.GameProcess.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import steelUnicorn.laplacity.GameProcess;

public class DensityRenderer {

	// Rendering visible density tiles
	private static Pixmap densityPixmap;
	private static Texture densityTexture;
	
	// Utils
	private static final Color tmpColor = new Color();
	
	public static void init() {
		int fieldWidth = field.getFieldWidth();
		int fieldHeight = field.getFieldHeight();
		
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
		densityPixmap.drawPixel(x, field.getFieldHeight() - y - 1, Color.rgba8888(tmpColor));
	}
	
	public static void render(Batch batch) {
		int fieldWidth = field.getFieldWidth();
		int fieldHeight = field.getFieldHeight();
		float tileSize = field.getTileSize();
		
		batch.begin();
		batch.draw(densityTexture, 0, 0, fieldWidth * tileSize, fieldHeight * tileSize);
		batch.end();
	}
	
	public static void cleanup() {
		densityPixmap.dispose();
		densityTexture.dispose();
	}

}

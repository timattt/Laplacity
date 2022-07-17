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
		int fieldWidth = field.getFieldWidth();
		int fieldHeight = field.getFieldHeight();
		
		Color chargeColor = new Color(Color.VIOLET);
		float chargeDensity = 0.0f;
		for (int i = 0; i < fieldWidth; i++) {
			for (int j = 0; j < fieldHeight; j++) {
				if ((chargeDensity = field.getTiles()[i][j].getChargeDensity()) != 0) {
					chargeColor.a = chargeDensity / GameProcess.MAX_DENSITY;
					densityPixmap.drawPixel(i, fieldHeight - j - 1, Color.rgba8888(chargeColor));
				}
			}
		}
		densityTexture.draw(densityPixmap, 0, 0);
	}
	
	public static void render(Batch batch) {
		int fieldWidth = field.getFieldWidth();
		int fieldHeight = field.getFieldHeight();
		float tileSize = field.getTileSize();
		
		batch.begin();
		batch.draw(densityTexture, -fieldWidth * tileSize / 2, -fieldHeight * tileSize / 2, fieldWidth * tileSize, fieldHeight * tileSize);
		batch.end();
	}
	
	public static void cleanup() {
		densityPixmap.dispose();
		densityTexture.dispose();
	}

}

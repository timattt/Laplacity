package steelUnicorn.laplacity.field.graphics;

import static steelUnicorn.laplacity.GameProcess.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import steelUnicorn.laplacity.CameraManager;
import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.utils.LevelParams.Hint;

/**
 * Класс отвечает за прорисовку фонов в игре. Он слева направо рисует последовательность фонов,
 * которая задана в levelParams. Высота фона всегда равна высоте поля.
 * @author timat
 *
 */
public class BackgroundRenderer {

	// sprite cache
	private static int id;
	private static int idSpace;
	
	public static void init() {
		Texture tex = null;
		float aspect = 0;
		float w = 0;
		float h = 0;
		
		gameCache.beginCache();
		
		int fieldHeight = LaplacityField.fieldHeight;
		float sz = LaplacityField.tileSize;

		idSpace = gameCache.endCache();

		float x = 0;

		gameCache.beginCache();
		Gdx.app.log("background rnederer", levelParams.getBackIds().toString());
		for (int i = 0; i < levelParams.getBackIds().size; i++) {
			tex = LaplacityAssets.BACKGROUNDS.get(levelParams.getBackIds().get(i));
			aspect = (float) tex.getWidth() / (float) tex.getHeight();
			h = fieldHeight * sz;
			w = aspect * h;
			
			gameCache.add(new TextureRegion(tex), x, 0, w, h);
			x += w;
			i++;
		}		
		
		for (Hint hint : levelParams.getLevelHints()) {
			tex = (LaplacityAssets.HINTS.get(hint.getHintId()));
			aspect = (float) tex.getWidth() / (float) tex.getHeight();
			h = fieldHeight * sz;
			w = aspect * h;
			gameCache.add(new TextureRegion(tex), 0, 0, w, h);
		}
		
		tex = LaplacityAssets.LEFT_BORDER;
		aspect = (float) tex.getWidth() / (float) tex.getHeight();
		h = fieldHeight * sz;
		w = aspect * h;
		gameCache.add(new TextureRegion(tex), -w, 0, w, h);
		
		tex = LaplacityAssets.RIGHT_BORDER;
		aspect = (float) tex.getWidth() / (float) tex.getHeight();
		h = fieldHeight * sz;
		w = aspect * h;
		gameCache.add(new TextureRegion(tex), sz * LaplacityField.fieldWidth, 0, w, h);
		
		for (int y = 0; y < fieldHeight; y++) {
			int k = 0;
			for (k = 0; k < 1; k++) {
				gameCache.add(LaplacityAssets.BARRIER_REGIONS[3], - sz * (k + 1), y * sz, sz, sz);
				gameCache.add(LaplacityAssets.BARRIER_REGIONS[3], sz * LaplacityField.fieldWidth + sz * (k), y * sz, sz, sz);
			}
			gameCache.add(LaplacityAssets.BARRIER_REGIONS[4], - sz * (k + 1), y * sz, sz/2, sz/2, sz, sz, 1, 1, 180);
			gameCache.add(LaplacityAssets.BARRIER_REGIONS[4], sz * LaplacityField.fieldWidth + sz * (k), y * sz, sz, sz);
	
		}
		
		id = gameCache.endCache();
	}
	
	public static void render() {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		gameCache.setProjectionMatrix(CameraManager.camMat());
		gameCache.begin();

		gameCache.draw(idSpace);
		gameCache.draw(id);
		gameCache.end();
		
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}
	
	public static void cleanup() {
	}

}

package steelUnicorn.laplacity.field.graphics;

import static steelUnicorn.laplacity.GameProcess.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import steelUnicorn.laplacity.CameraManager;
import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.field.LaplacityField;

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
		gameCache.beginCache();
		
		int fieldWidth = LaplacityField.fieldWidth;
		int fieldHeight = LaplacityField.fieldHeight;
		float sz = LaplacityField.tileSize;
		

		float x = 2 * fieldWidth * sz;

		while (x > 0) {
			Texture tex = LaplacityAssets.SPACE_BACKGROUND;
			float aspect = (float) tex.getWidth() / (float) tex.getHeight();
			float h = fieldHeight * sz;
			float w = aspect * h;

			gameCache.add(new TextureRegion(tex), x - w, 0, w, h);
			x -= w;
		}
		
		idSpace = gameCache.endCache();

		x = 0;

		gameCache.beginCache();
		
		for (int i = 0; i < levelParams.getBackIds().size; i++) {
			Texture tex = LaplacityAssets.BACKGROUNDS.get(levelParams.getBackIds().get(i));
			float aspect = (float) tex.getWidth() / (float) tex.getHeight();
			float h = fieldHeight * sz;
			float w = aspect * h;
			
			gameCache.add(new TextureRegion(tex), x, 0, w, h);
			x += w;
			i++;
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

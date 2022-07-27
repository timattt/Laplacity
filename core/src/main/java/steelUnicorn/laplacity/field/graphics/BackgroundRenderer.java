package steelUnicorn.laplacity.field.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.field.LaplacityField;

public class BackgroundRenderer {

	// sprite cache
	private static SpriteCache cache;
	private static int id;
	private static int idSpace;
	
	public static void init() {
		cache = new SpriteCache();
		
		cache.beginCache();
		
		int fieldWidth = LaplacityField.fieldWidth;
		int fieldHeight = LaplacityField.fieldHeight;
		float sz = LaplacityField.tileSize;
		

		float x = fieldWidth * sz;

		while (x > 0) {
			Texture tex = LaplacityAssets.SPACE_BACKGROUND;
			float aspect = (float) tex.getWidth() / (float) tex.getHeight();
			float h = fieldHeight * sz;
			float w = aspect * h;

			cache.add(new TextureRegion(tex), x - w, 0, w, h);
			x -= w;
		}
		
		idSpace = cache.endCache();

		x = fieldWidth * sz;

		cache.beginCache();
		
		while (x > 0) {
			Texture tex = LaplacityAssets.BACKGROUNDS[Globals.currentSection][(GameProcess.levelNumber - 1) % Globals.LEVELS_PER_SECTION];
			float aspect = (float) tex.getWidth() / (float) tex.getHeight();
			float h = fieldHeight * sz;
			float w = aspect * h;
			
			cache.add(new TextureRegion(tex), x - w, 0, w, h);
			x -= w;
		}
		
		id = cache.endCache();
	}
	
	public static void render(Batch batch) {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		cache.setProjectionMatrix(Globals.camera.combined);
		cache.begin();

		cache.draw(idSpace);
		cache.draw(id);
		cache.end();
		
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}
	
	public static void cleanup() {
		if (cache != null) {
			cache.dispose();
			cache = null;
		}
	}

}

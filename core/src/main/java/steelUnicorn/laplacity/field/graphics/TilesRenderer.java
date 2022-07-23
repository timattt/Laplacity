package steelUnicorn.laplacity.field.graphics;

import static steelUnicorn.laplacity.field.LaplacityField.*;

import com.badlogic.gdx.graphics.g2d.SpriteCache;

import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.field.tiles.EmptyTile;
import steelUnicorn.laplacity.field.tiles.SolidTile;

public class TilesRenderer {

	private static SpriteCache cache;
	private static int id;
	
	public static void init() {
		cache = new SpriteCache();
		
		cache.beginCache();
		for (int i = 0; i < fieldWidth; i++) {
			for (int j = 0; j < fieldHeight; j++) {
				EmptyTile tl = tiles[i][j];
				if (tl instanceof SolidTile) {
					SolidTile stl = (SolidTile) tl;
					stl.constantDraw(cache);
				}
			}
		}
		id = cache.endCache();
	}
	
	public static void render() {
		cache.setProjectionMatrix(Globals.camera.combined);
		cache.begin();
		cache.draw(id);
		cache.end();
	}
	
	public static void cleanup() {
		if (cache != null) {
			cache.dispose();
			cache = null;
		}
		id = -1;
	}
	
}

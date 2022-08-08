package steelUnicorn.laplacity.field.graphics;

import static steelUnicorn.laplacity.field.LaplacityField.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import steelUnicorn.laplacity.CameraManager;
import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.tiles.EmptyTile;
import steelUnicorn.laplacity.field.tiles.SolidTile;

/**
 * Класс, который кеширует все тайлы, а потом на каждом тике этот кеш быстро прорисовывает.
 * @author timat
 *
 */
public class TilesRenderer {

	private static int id;
	private static final float[] tmp = new float[1];
	
	public static void init() {
		float sz = LaplacityField.tileSize;
		
		float d = 0.01f;
		
		GameProcess.gameCache.beginCache();
		for (int i = 0; i < fieldWidth; i++) {
			for (int j = 0; j < fieldHeight; j++) {
				EmptyTile tl = tiles[i][j];
				if (tl instanceof SolidTile) {
					SolidTile stl = (SolidTile) tl;
					TextureRegion reg = stl.getRegion(tmp);
					if (reg != null) {
						GameProcess.gameCache.add(reg, stl.getGridX() * sz - d, stl.getGridY() * sz - d,  sz/2, sz/2, sz + 2*d, sz+2*d, 1, 1, tmp[0]);
					}
				}
			}
		}
		id = GameProcess.gameCache.endCache();
	}
	
	public static void render() {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		GameProcess.gameCache.setProjectionMatrix(CameraManager.camMat());
		GameProcess.gameCache.begin();
		GameProcess.gameCache.draw(id);
		GameProcess.gameCache.end();
		
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}
	
	public static void cleanup() {
	}
	
}

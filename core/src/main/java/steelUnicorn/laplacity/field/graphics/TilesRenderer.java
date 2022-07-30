package steelUnicorn.laplacity.field.graphics;

import static steelUnicorn.laplacity.field.LaplacityField.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import steelUnicorn.laplacity.CameraManager;
import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.field.tiles.EmptyTile;
import steelUnicorn.laplacity.field.tiles.SolidTile;



public class TilesRenderer {

	private static int id;
	
	public static void init() {
		GameProcess.gameCache.beginCache();
		for (int i = 0; i < fieldWidth; i++) {
			for (int j = 0; j < fieldHeight; j++) {
				EmptyTile tl = tiles[i][j];
				if (tl instanceof SolidTile) {
					SolidTile stl = (SolidTile) tl;
					stl.constantDraw(GameProcess.gameCache);
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

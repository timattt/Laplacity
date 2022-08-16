package steelUnicorn.laplacity.field.graphics;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.field.LaplacityField.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.TimeUtils;

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
	private static int animationId;
	private static final float[] tmp = new float[1];
	
	private static long lastAnimationRepaintTime;
	
	private static boolean repaintRequested = false;
	
	private static ShaderProgram shader;
	
	private static long startTime;
	
	public static void init() {
		shader = new ShaderProgram(Gdx.files.internal("shaders/Tiles.vert"), Gdx.files.internal("shaders/Tiles.frag"));
		startTime = TimeUtils.millis();
		
		if (!shader.isCompiled()) {
			Gdx.app.log("shader compile", shader.getLog());
		}
		
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
		
		GameProcess.gameCache.beginCache();
		for (int i = 0; i < fieldWidth; i++) {
			for (int j = 0; j < fieldHeight; j++) {
				EmptyTile tl = tiles[i][j];
				if (tl instanceof SolidTile) {
					SolidTile stl = (SolidTile) tl;
					TextureRegion reg = stl.getAnimatedRegion(tmp);
					if (reg != null) {
						GameProcess.gameCache.add(reg, stl.getGridX() * sz - d, stl.getGridY() * sz - d,  sz/2, sz/2, sz + 2*d, sz+2*d, 1, 1, tmp[0]);
					}
				}
			}
		}
		animationId = GameProcess.gameCache.endCache();
	}
	
	private static void repaint() {
		float sz = LaplacityField.tileSize;
		
		float d = 0.01f;
		
		GameProcess.gameCache.beginCache(id);
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
		GameProcess.gameCache.endCache();
	}
	
	private static void repaintAnims() {
		float sz = LaplacityField.tileSize;
		
		float d = 0.01f;
		
		GameProcess.gameCache.beginCache(animationId);
		for (int i = 0; i < fieldWidth; i++) {
			for (int j = 0; j < fieldHeight; j++) {
				EmptyTile tl = tiles[i][j];
				if (tl instanceof SolidTile) {
					SolidTile stl = (SolidTile) tl;
					TextureRegion reg = stl.getAnimatedRegion(tmp);
					if (reg != null) {
						GameProcess.gameCache.add(reg, stl.getGridX() * sz - d, stl.getGridY() * sz - d,  sz/2, sz/2, sz + 2*d, sz+2*d, 1, 1, tmp[0]);
					}
				}
			}
		}
		GameProcess.gameCache.endCache();
	}
	
	public static void render() {
		if (repaintRequested) {
			repaint();
			repaintRequested = false;
		}
		if (TimeUtils.millis() - lastAnimationRepaintTime > TILE_ANIMATION_DELAY) {
			lastAnimationRepaintTime = TimeUtils.millis();
			repaintAnims();
		}
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		shader.bind();
		GameProcess.gameCache.setShader(shader);
		shader.setUniformf("time", (float)(TimeUtils.millis() - startTime) / 1000f);
		GameProcess.gameCache.setProjectionMatrix(CameraManager.camMat());
		GameProcess.gameCache.begin();
		GameProcess.gameCache.draw(id);
		GameProcess.gameCache.end();
		GameProcess.gameCache.begin();
		GameProcess.gameCache.draw(animationId);
		GameProcess.gameCache.end();
		GameProcess.gameCache.setShader(null);

		Gdx.gl.glDisable(GL20.GL_BLEND);
	}
	
	public static void cleanup() {
		if (shader != null) {
			shader.dispose();
			shader = null;
		}
	}
	
	public static void requestRepaint() {
		repaintRequested = true;
	}
}

package steelUnicorn.laplacity;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.Globals.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

import steelUnicorn.laplacity.field.GameMode;
import steelUnicorn.laplacity.particles.Electron;
import steelUnicorn.laplacity.particles.Proton;

public class LaplacityIngameController implements GestureListener {

	public LaplacityIngameController() {
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		TMP3.set(x, y, 0);
		camera.unproject(TMP3);
		if (currentGameMode == GameMode.electrons) {
			addStaticParticle(new Electron(TMP3.x, TMP3.y));
		}
		if (currentGameMode == GameMode.protons) {
			addStaticParticle(new Proton(TMP3.x, TMP3.y));
		}
		return true;
	}

	@Override
	public boolean longPress(float x, float y) {
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		return false;
	}
	
	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		TMP3.set(x, y, 0);
		camera.unproject(TMP3);
		if (currentGameMode == GameMode.dirichlet) {
			field.fillCircleWithRandomDensity(TMP3.x, TMP3.y, BRUSH_RADIUS, MAX_DENSITY);
		} else if (currentGameMode == GameMode.eraser) {
			field.clearCircleDensity(TMP3.x, TMP3.y, BRUSH_RADIUS);
		} else {
			camera.position.x -= deltaX * Globals.SCREEN_WORLD_WIDTH / Gdx.graphics.getWidth();
			float mx = field.getFieldWidth() / 2 * field.getTileSize() - Globals.SCREEN_WORLD_WIDTH / 2;
			camera.position.x = Math.max(-mx, Math.min(mx, camera.position.x));
			camera.update();
		}
		
		return true;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		return false;
	}

	@Override
	public void pinchStop() {
	}

}

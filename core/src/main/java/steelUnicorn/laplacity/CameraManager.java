package steelUnicorn.laplacity;

import static steelUnicorn.laplacity.core.Globals.*;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import steelUnicorn.laplacity.field.LaplacityField;

public class CameraManager {
	
	// VELOCITY
	public static final float CAMERA_VELOCITY = 15f;
	
	// CAMERA
	private static OrthographicCamera camera;
	
	// MOVING
	private static float targetX;
	private static boolean isMoving = false;
	
	public static void init() {
		camera = new OrthographicCamera(SCREEN_WORLD_WIDTH, SCREEN_WORLD_HEIGHT);
	}
	
	public static void setToMainParticle() {
		camera.position.y = SCREEN_WORLD_HEIGHT / 2;
		setXPosition(GameProcess.mainParticle.getX());
	}
	
	public static void moveToX(float x) {
		targetX = clamp(x);
		isMoving = true;
	}
	
	public static void moveX(float dx) {
		setXPosition(camera.position.x + dx);
		isMoving = false;
	}
	
	private static float sgn(float x) {
		if (x < 1f && -1f < x) {
			return x;
		}
		if (x < 0) {
			return -1f;
		} else {
			return 1f;
		}
	}
	
	public static void update(float dt) {
		if (isMoving) {
			float dx = CAMERA_VELOCITY * sgn(targetX - camera.position.x) * dt;
			float newX = camera.position.x + dx;
			if (Math.abs(targetX - newX) < Math.abs(dx)) {
				isMoving = false;
			} else {
				setXPosition(newX);
			}
		}
	}
	
	public static void stopMove() {
		isMoving = false;
	}
	
	private static float clamp(float x) {
		float min = SCREEN_WORLD_WIDTH /2;
		float max = LaplacityField.fieldWidth * LaplacityField.tileSize - min;;
		if (min > max) {
			return LaplacityField.fieldWidth * LaplacityField.tileSize / 2;
		}
		
		return Math.max(min, Math.min(max, x));
	}
	
	public static void setXPosition(float x) {
		camera.position.x = clamp(x);
		camera.update();
	}
	
	public static Matrix4 camMat() {
		return camera.combined;
	}

	public static ExtendViewport createViewport() {
		return new ExtendViewport(SCREEN_WORLD_WIDTH, SCREEN_WORLD_HEIGHT, camera);
	}
	
	public static void getCameraWorldPos(float x, float y, Vector2 res) {
		TMP3.x = x;
		TMP3.y = y;
		TMP3.z = 0;
		camera.unproject(TMP3);
		res.x = TMP3.x;
		res.y = TMP3.y;
	}
	
	public static OrthographicCamera getCamera() {
		return camera;
	}
	
}

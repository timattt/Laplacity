package steelUnicorn.laplacity;

import static steelUnicorn.laplacity.core.Globals.*;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import steelUnicorn.laplacity.field.LaplacityField;

public class CameraManager {
	
	// VELOCITY
	public static final float CAMERA_VELOCITY = 15f;

	// ZOOM
	public static final float MIN_ZOOM = 0.5f;
	public static final float MAX_ZOOM = 1f;
	public static final float ZOOM_COEFF = 0.0001f;
	
	// CAMERA
	private static OrthographicCamera camera;
	
	// MOVING
	private static float targetX;
	private static float targetY;
	private static boolean isMoving = false;
	
	public static void init() {
		camera = new OrthographicCamera(SCREEN_WORLD_WIDTH, SCREEN_WORLD_HEIGHT);
	}
	
	public static void setToMainParticle() {
		camera.position.y = SCREEN_WORLD_HEIGHT / 2;
		setXPosition(GameProcess.mainParticle.getX());
	}
	
	public static void moveToX(float x) {
		targetX = clampX(x);
		isMoving = true;
	}

	public static void moveTo(float x, float y) {
		targetX = clampX(x);
		targetY = clampY(y);
		isMoving = true;
	}
	
	public static void moveX(float dx) {
		setXPosition(camera.position.x + dx);
		isMoving = false;
	}

	public static void move(float dx, float dy) {
		setPosition(camera.position.x + dx, camera.position.y + dy);
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
	
	private static float clampX(float x) {
		float min = 0.5f * camera.zoom * SCREEN_WORLD_WIDTH;
		float max = LaplacityField.fieldWidth * LaplacityField.tileSize - min;
		if (min > max) {
			return LaplacityField.fieldWidth * LaplacityField.tileSize / 2;
		}
		
		return Math.max(min, Math.min(max, x));
	}

	private static float clampY(float y) {
		float min = 0.5f * camera.zoom * SCREEN_WORLD_HEIGHT;
		float max = LaplacityField.fieldHeight * LaplacityField.tileSize - min;
		if (min > max) {
			return LaplacityField.fieldHeight * LaplacityField.tileSize / 2;
		}
		return Math.max(min, Math.min(max, y));
	}
	
	public static void setXPosition(float x) {
		camera.position.x = clampX(x);
		camera.update();
	}

	public static void setPosition(float x, float y) {
		camera.position.x = clampX(x);
		camera.position.y = clampY(y);
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


	private static Vector2 oldCameraPosition = new Vector2();
	private static Vector2 zoomTarget = new Vector2();
	private static boolean isPinching = false;

	public static void processPinch(Vector2 initialFirstPointer, Vector2 initialSecondPointer, Vector2 firstPointer, Vector2 secondPointer) {
		if (!isPinching) {
			oldCameraPosition.set(camera.position.x, camera.position.y);
			getCameraWorldPos(0.5f * (initialFirstPointer.x + initialSecondPointer.x), 0.5f * (initialFirstPointer.y + initialSecondPointer.y), zoomTarget);
			zoomTarget.sub(oldCameraPosition);
			float interpCoeff = (MAX_ZOOM - camera.zoom) / (MAX_ZOOM - MIN_ZOOM);
			oldCameraPosition.x -= interpCoeff * zoomTarget.x;
			oldCameraPosition.y -= interpCoeff * zoomTarget.y;
			isPinching = true;
		}
		//float centerDx = (initialFirstPointer.x + initialSecondPointer.x - firstPointer.x - secondPointer.x) / (2f * Gdx.graphics.getWidth() * SCREEN_WORLD_WIDTH);
		//float centerDy = (initialFirstPointer.y + initialSecondPointer.y - firstPointer.y - secondPointer.y) / (2f * Gdx.graphics.getHeight() * SCREEN_WORLD_HEIGHT);
		float dr = Math.abs(initialFirstPointer.x - initialSecondPointer.x) + Math.abs(initialFirstPointer.y - initialSecondPointer.y) -
			Math.abs(firstPointer.x - secondPointer.x) - Math.abs(firstPointer.y - secondPointer.y);
		camera.zoom = MathUtils.clamp(camera.zoom + ZOOM_COEFF*dr*camera.zoom, MIN_ZOOM, MAX_ZOOM);
		float interpCoeff = (MAX_ZOOM - camera.zoom) / (MAX_ZOOM - MIN_ZOOM);
		setPosition(oldCameraPosition.x + interpCoeff * zoomTarget.x, oldCameraPosition.y + interpCoeff * zoomTarget.y);
	}

	public static void stopPinching() {
		isPinching = false;
	}
	
}

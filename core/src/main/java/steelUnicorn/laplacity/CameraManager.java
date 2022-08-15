package steelUnicorn.laplacity;

import static steelUnicorn.laplacity.core.Globals.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.field.LaplacityField;

public class CameraManager {
	
	// VELOCITY
	public static final float BASE_VELOCITY = 10f;
	private static final float VELOCITY_DISTANCE_MULTIPLIER = 6f;

	// ZOOM
	public static final float MIN_ZOOM = 0.5f;
	public static final float MAX_ZOOM = 1f;
	
	// CAMERA
	private static OrthographicCamera camera;
	
	// MOVING
	private static float targetX;
	private static float targetY;
	private static boolean isMoving = false;

	// Bounding box
	private static final float CAMERA_BB_X = 5f;
	private static final float CAMERA_BB_Y = 3f;

	// Переменные, используемые при обработке двухпальцевых жестов
	private static Vector2 cameraPositionWithoutZoom = new Vector2();
	private static Vector2 zoomDirection = new Vector2();
	private static boolean isPinching = false;
	private static float initialDistance = 0f;
	private static float currentDistance = 0f;
	private static float initialZoom = 1f;
	private static float zoomMultiplier = 1f;

	public static void init() {
		camera = new OrthographicCamera(SCREEN_WORLD_WIDTH, SCREEN_WORLD_HEIGHT);
	}
	
	public static void setToMainParticle() {
		camera.zoom = MAX_ZOOM;
		targetX = GameProcess.cat.getX();
		targetY = GameProcess.cat.getY();
		setPosition(targetX, targetY);
	}
	
	public static void setMoving(float x, float y) {
		targetX = x;
		targetY = y;
		isMoving = true;
	}

	public static void move(float dx, float dy) {
		setPosition(camera.position.x + dx, camera.position.y + dy);
		isMoving = false;
	}

	public static void update(float dt) {
		if (isMoving) {
			/*
			 * Сначала находим модуль скорости
			 * Если мы за пределами коробки (BB), то скорость складывается из базового значения
			 * и слагаемого, пропорционального расстоянию до коробки.
			 * Базовое значение не может превышать скорость тела (кота)
			*/
			float xVelocity = (Math.abs(targetX - camera.position.x) > CAMERA_BB_X) ?
				(Math.abs(targetX - camera.position.x) - CAMERA_BB_X) * VELOCITY_DISTANCE_MULTIPLIER +
				MathUtils.clamp(BASE_VELOCITY, 0f, Math.abs(GameProcess.cat.getVelocity().x)) :
				0f;

			float yVelocity = (Math.abs(targetY - camera.position.y) > CAMERA_BB_Y) ?
				(Math.abs(targetY - camera.position.y) - CAMERA_BB_Y) * VELOCITY_DISTANCE_MULTIPLIER +
				MathUtils.clamp(BASE_VELOCITY, 0f, Math.abs(GameProcess.cat.getVelocity().y)) :
				0f;
			Gdx.app.log(String.valueOf(xVelocity), String.valueOf(yVelocity));
			// Теперь находим dx
			float dx = xVelocity * Math.signum(targetX - camera.position.x) * dt;
			float dy = yVelocity * Math.signum(targetY - camera.position.y) * dt;
			float newX = camera.position.x + dx;
			float newY = camera.position.y + dy;
			if ((Math.abs(targetX - newX) < Math.abs(dx)) && (Math.abs(targetY - newY) < Math.abs(dy))) {
				isMoving = false;
			} else {
				setPosition(newX, newY);
			}
		}
	}
	
	public static void stopMoving() {
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
	
	private static void setPosition(float x, float y) {
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

	/**
	 * Функция, обрабатывающая двухпальцевые жесты.
	 * Поддерживается зум и перемещение камеры в двух направлениях.
	 * @param initialFirstPointer
	 * @param initialSecondPointer
	 * @param firstPointer
	 * @param secondPointer
	 */
	public static void processPinch(Vector2 initialFirstPointer, Vector2 initialSecondPointer, Vector2 firstPointer, Vector2 secondPointer) {
		if (!isPinching) {
			// Initialize pinching event since LibGDX lacks startPinching() method
			/*
			 * Addidional plotting is required to make zooming smooth.
			 * We take two points: current camera position and the point in between two initial pointer positions.
			 * If we fully zoom in, the camera should approach the point in between, let call it zoomTarget.
			 * So we plot a straight line through these point, the camera must stick to this line when moving.
			 * Vector zoomDirection is zoomTarger - camera.position, it is the direction of this straight line
			 * Finally, we must set the point where camera should be without zoom
			 * If we start pinching with zoom coefficient equal to 1, this is simply camera.position
			 * But if not, we can extrapolate camera movement past current camera.position.
			 * The zoom factor is simply current distance between fingers / initial distance
			 * Note that we don't calculate precise distance to avoid square root calculation
			 * 
			 * To move camera, we simply traack the movement of the point in between the fingers
			 */

			// 1. Find zoomDirection vector
			cameraPositionWithoutZoom.set(camera.position.x, camera.position.y);
			getCameraWorldPos(
				0.5f * (initialFirstPointer.x + initialSecondPointer.x),
				0.5f * (initialFirstPointer.y + initialSecondPointer.y),
				zoomDirection
			);
			zoomDirection.sub(cameraPositionWithoutZoom);
			// 2. find interpolation coefficient and extrapolate camera position without zoom using it
			float interpCoeff = (MAX_ZOOM - camera.zoom) / (MAX_ZOOM - MIN_ZOOM);
			cameraPositionWithoutZoom.x -= interpCoeff * zoomDirection.x;
			cameraPositionWithoutZoom.y -= interpCoeff * zoomDirection.y;
			// 3. Save initial distance between fingers and initial zoom
			initialDistance = 
				Math.abs(initialFirstPointer.x - initialSecondPointer.x) +
				Math.abs(initialFirstPointer.y - initialSecondPointer.y);
			initialZoom = camera.zoom;
			isPinching = true;
		}
		float centerDx = 
			(initialFirstPointer.x + initialSecondPointer.x - firstPointer.x - secondPointer.x) *
			camera.zoom * SCREEN_WORLD_WIDTH /(2f * Gdx.graphics.getWidth());
		float centerDy =
			-(initialFirstPointer.y + initialSecondPointer.y - firstPointer.y - secondPointer.y) *
			camera.zoom * SCREEN_WORLD_HEIGHT / (2f * Gdx.graphics.getHeight());
		currentDistance =
			Math.abs(firstPointer.x - secondPointer.x) +
			Math.abs(firstPointer.y - secondPointer.y);
		if (currentDistance < Globals.EPSILON_PRECISION) {
			zoomMultiplier = MAX_ZOOM;
		} else {
			zoomMultiplier = initialDistance / currentDistance;
		}
		camera.zoom = MathUtils.clamp(zoomMultiplier * initialZoom, MIN_ZOOM, MAX_ZOOM);
		float interpCoeff = (MAX_ZOOM - camera.zoom) / (MAX_ZOOM - MIN_ZOOM);
		setPosition(
			centerDx + cameraPositionWithoutZoom.x + interpCoeff * zoomDirection.x,
			centerDy + cameraPositionWithoutZoom.y + interpCoeff * zoomDirection.y
		);
	}

	public static void stopPinching() {
		isPinching = false;
	}
	
}

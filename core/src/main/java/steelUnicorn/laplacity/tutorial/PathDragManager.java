package steelUnicorn.laplacity.tutorial;

import static steelUnicorn.laplacity.core.Globals.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import steelUnicorn.laplacity.CameraManager;
import steelUnicorn.laplacity.field.LaplacityField;

public class PathDragManager {

	private static RequestedPoint[] requested;
	private static int completedNumber;
	
	private static final Vector2 tmp = new Vector2();
	private static final float REACTION_RAD = 5f;

	private static RequestedPoint currentTarget = null;
	
	public static void update() {
		if (requested == null) {
			return;
		}
		
		CameraManager.getCameraWorldPos(Gdx.input.getX(), Gdx.input.getY(), TMP1);
		float x = TMP1.x;
		float y = TMP1.y;
		
		RequestedPoint pointerTarget = null;
		
		for (RequestedPoint p : requested) {
			if (pointerTarget == null && !p.completed) {
				pointerTarget = p;
			}
			tmp.set(x - p.x, y - p.y);
			if (tmp.len2() < REACTION_RAD * REACTION_RAD && Gdx.input.isTouched()) {
				if (!p.completed) {
					completedNumber++;
				}
				p.completed = true;
			}
		}

		if (pointerTarget != null) {
			if (currentTarget != pointerTarget) {
				currentTarget = pointerTarget;
				TutorialManager.pointer.linearAnimation(pointerTarget.x, pointerTarget.y, pointerTarget.x + 2f, pointerTarget.y + 2f, 1000);

			}
		}
	}

	public static void newTask(float[][] targets) {
		requested = new RequestedPoint[targets.length];
		
		for (int i = 0; i < requested.length; i++) {
			requested[i] = new RequestedPoint(targets[i][0] * LaplacityField.tileSize, targets[i][1] * LaplacityField.tileSize);
		}
		
		completedNumber = 0;
	}
	
	public static boolean isCompleted() {
		return requested == null || completedNumber == requested.length;
	}
	
	public static void cleanup() {
		requested = null;
		TutorialManager.pointer.hide();
	}
	
	private static class RequestedPoint {
		private float x;
		private float y;
		private boolean completed;
		
		public RequestedPoint(float x, float y) {
			super();
			this.x = x;
			this.y = y;
			this.completed = false;
		}
	}

}

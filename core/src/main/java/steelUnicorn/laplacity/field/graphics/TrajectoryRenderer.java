package steelUnicorn.laplacity.field.graphics;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.core.Globals.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import steelUnicorn.laplacity.GameMode;

public class TrajectoryRenderer {

	// Buffer for trajectory calculation
	private static final Vector2[] trajectory = new Vector2[TRAJECTORY_POINTS];
	
	// changing direction currently
	public static boolean changingDir = false;
	
	public static void init() {
		for (int i = 0; i < TRAJECTORY_POINTS; i++) {
			trajectory[i] = new Vector2();
		}
	}
	
	public static void render() {	
		if (currentGameMode == GameMode.flight) {
			return;
		}
		
		for (int i = 1; i < TRAJECTORY_POINTS; i++) {
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(Color.CYAN);
			shapeRenderer.point(trajectory[i].x, trajectory[i].y, 0);
			shapeRenderer.end();
		}
		
		if (changingDir) {
			mainParticle.drawStartVelocityArrow();
		}
	}
	
	public static void updateTrajectory() {
		mainParticle.calculateTrajectory(trajectory);
	}
	
	public static void cleanup() {
		
	}

}

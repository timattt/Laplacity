package steelUnicorn.laplacity.field;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.Globals.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import steelUnicorn.laplacity.particles.ControllableElectron;

public class TrajectoryRenderer {

	public static void render() {
		Vector2[] tr = ControllableElectron.getTrajectory();
		
		if (tr == null || currentGameMode == GameMode.flight) {
			return;
		}
		
		for (int i = 1; i < tr.length; i++) {
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(Color.CYAN);
			shapeRenderer.point(tr[i].x, tr[i].y, 0);
			shapeRenderer.end();
		}
	}
	
	public static void updateTrajectory() {
		TMP1.set(mainParticle.getX(), mainParticle.getY());
		mainParticle.getDir(TMP2);
		mainParticle.calculateTrajectory(TMP1, TMP2, mainParticle.getMass(), mainParticle.getCharge(), TRAJECTORY_STEP, TRAJECTORY_POINTS, STEPS_PER_POINT);
	}

}

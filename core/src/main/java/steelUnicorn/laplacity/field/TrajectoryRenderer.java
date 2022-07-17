package steelUnicorn.laplacity.field;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.Globals.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class TrajectoryRenderer {

	public static void render() {
		Vector2[] tr = FieldPotentialCalculator.getTrajectory();
		
		if (tr == null) {
			return;
		}
		
		float dx = field.getFieldWidth() * field.getTileSize() / 2;
		float dy = field.getFieldHeight() * field.getTileSize() / 2;
		
		for (int i = 1; i < tr.length; i++) {
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(Color.CYAN);
			shapeRenderer.point(tr[i].x - dx, tr[i].y - dy, 0);
			shapeRenderer.end();
		}
	}
	
	public static void updateTrajectory() {
		TMP1.set(mainParticle.getX() + field.getFieldWidth() * field.getTileSize() / 2, mainParticle.getY() + field.getFieldHeight() * field.getTileSize() / 2);
		mainParticle.getDir(TMP2);
		FieldPotentialCalculator.calculateTrajectory(TMP1, TMP2, mainParticle.getMass(), ELECTRON_CHARGE, TRAJECTORY_STEP, TRAJECTORY_POINTS);
	}

}

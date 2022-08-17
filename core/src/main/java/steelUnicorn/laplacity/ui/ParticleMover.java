package steelUnicorn.laplacity.ui;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.core.Globals.*;

import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.graphics.TrajectoryRenderer;
import steelUnicorn.laplacity.field.physics.FieldCalculator;
import steelUnicorn.laplacity.particles.ChargedParticle;

public class ParticleMover {

	public static ChargedParticle current;

	private static ChargedParticle findNear(float x, float y) {
		for (ChargedParticle p : particles) {
			TMP1.set(x, y);
			TMP1.sub(p.getX(), p.getY());
			if (TMP1.len2() < 4 * PARTICLE_SIZE * PARTICLE_SIZE) {
				return p;
			}
		}
		return null;
	}
	
	public static void tryToStartMoving(float x, float y) {
		ChargedParticle p = findNear(x, y);
		
		if (p != null && !TrajectoryRenderer.changingDir) {
			current = p;
		}
	}
	
	public static void tryToMove(float x, float y) {
		if (current != null) {
			TMP1.set(cat.getX(), cat.getY());
			TMP1.sub(x, y);
			if (TMP1.len2() < 4 * PARTICLE_SIZE * PARTICLE_SIZE) {
				stopMoving();
			}
			tryToMoveStaticParticle(current, x, y);
		}
	}
	
	public static void stopMoving() {
		if (current != null) {
			FieldCalculator.initPotentialCalculation(LaplacityField.tiles);
			TrajectoryRenderer.updateTrajectory();
			current = null;
		}
	}
	
	public static boolean isMoving() {
		return current != null;
	}
}

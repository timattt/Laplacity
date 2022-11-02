package steelUnicorn.laplacity.ui;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.core.Globals.*;

import steelUnicorn.laplacity.chargedParticles.ChargedParticle;
import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.graphics.TrajectoryRenderer;
import steelUnicorn.laplacity.field.physics.FieldCalculator;

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
	
	private static void tryToStartMoving(float x, float y) {
		if (current != null) {
			return;
		}
		
		ChargedParticle p = findNear(x, y);
		
		if (p != null && !TrajectoryRenderer.changingDir) {
			current = p;
			current.useVirtual(x, y);
		}
	}
	
	public static void tryToMove(float x, float y) {
		tryToStartMoving(x, y);
		
		if (current != null) {
			TMP1.set(cat.getX(), cat.getY());
			TMP1.sub(x, y);
			if (TMP1.len2() > 4 * CAT_SIZE * CAT_SIZE) {
				tryToMoveStaticParticle(current, x, y);
			}
			current.useVirtual(x, y);
		}
	}
	
	public static void stopMoving(float x, float y) {
		if (current != null) {
			current.disableVirtual();
			FieldCalculator.initPotentialCalculation(LaplacityField.tiles);
			TrajectoryRenderer.updateTrajectory();
			current = null;
		}
	}
	
	public static boolean isMoving() {
		return current != null;
	}
}

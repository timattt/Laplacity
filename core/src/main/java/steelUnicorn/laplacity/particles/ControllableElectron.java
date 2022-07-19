package steelUnicorn.laplacity.particles;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.Globals.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import steelUnicorn.laplacity.field.FieldPotentialCalculator;
import steelUnicorn.laplacity.field.GameMode;

public class ControllableElectron extends Electron {

	// Start velocity
	private float dirX;
	private float dirY;
	
	// Start pos
	private float startX;
	private float startY;
	
	public ControllableElectron(float x, float y) {
		super(x, y, false);
		startX = x;
		startY = y;
	}

	@Override
	public void act(float delta) {
		if (currentGameMode == GameMode.flight) {
			FieldPotentialCalculator.calculateForce(getX(), getY(), field.getTiles(), TMP1);
			body.applyForceToCenter(TMP1.scl(charge), false);
		}
		super.act(delta);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (currentGameMode != GameMode.flight) {
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(Color.YELLOW);
			shapeRenderer.line(getX(), getY(), getX() + dirX, getY() + dirY);
			shapeRenderer.end();
		}
		super.draw(batch, parentAlpha);
	}

	public void setDir(float x, float y) {
		dirX = x;
		dirY = y;
	}
	
	public void getDir(Vector2 dest) {
		dest.x = dirX;
		dest.y = dirY;
	}
	
	public void startFlight() {
		body.setLinearVelocity(dirX, dirY);
	}
	
	public void reset() {
		setPosition(startX, startY);
		body.setLinearVelocity(0, 0);
		body.setAngularVelocity(0);
	}

	public float getMass() {
		return body.getMass();
	}
	
	// Buffer for trajectory calculation
	private static Vector2[] trajectory;
	private static float trajLength;

	/**
	 * Calculate trajectory of an electron using simplectic Euler method.
	 * Calculates n trajectory points following departure point and stores them in array inside the class
	 * The array is accessible via getTrajectory() method
	 * @param departure Initial trajectory point
	 * @param initVelocity Initial velocity
	 * @param step Integration step
	 * @param n Number of iterations
	 */
	public void calculateTrajectory(Vector2 departure, Vector2 initVelocity, float mass, float charge, float step, int n, int stepsPerPoint) {
		// Resize buffer if needed
		if (trajLength != n) {
			trajectory = new Vector2[n];
			for (int i = 0; i < n; i++) {
				trajectory[i] = new Vector2();
			}
			trajLength = n;
		}

		startFlight();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < stepsPerPoint; j++) {
				FieldPotentialCalculator.calculateForce(body.getTransform().getPosition().x, body.getTransform().getPosition().y, field.getTiles(), TMP1);
				body.applyForceToCenter(TMP1.scl(charge), false);
				levelWorld.step(step, 1, 1);
			}
			
			trajectory[i].set(body.getTransform().getPosition());
		}
		reset();
	}
	
	public static Vector2[] getTrajectory() {
		return trajectory;
	}
}

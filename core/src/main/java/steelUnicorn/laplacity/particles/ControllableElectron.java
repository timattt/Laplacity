package steelUnicorn.laplacity.particles;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.core.Globals.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.physics.FieldCalculator;
import steelUnicorn.laplacity.gameModes.GameMode;


/**
 * Класс управляемой частицы.
 * Тут есть поле начальной скорости. При обновлении к телу добавляется сила от поля.
 * @author timat
 *
 */
public class ControllableElectron extends ChargedParticle {

	// Start velocity
	private float slingshotX;
	private float slingshotY;
	
	// Start pos
	private float startX;
	private float startY;
	
	public ControllableElectron(float x, float y) {
		super(x, y, CAT_SIZE, - PARTICLE_CHARGE, false);
		startX = x;
		startY = y;
	}

	@Override
	public void draw() {
		GameProcess.gameBatch.draw(LaplacityAssets.CAT_REGION,
				getX() - CAT_SIZE,
				getY() - CAT_SIZE,
				CAT_SIZE,
				CAT_SIZE,
				2 * CAT_SIZE,
				2 * CAT_SIZE,
				1f,
				1f,
				body.getAngle() * MathUtils.radDeg
				);
	}
	
	@Override
	public boolean isMainParticle() {
		return true;
	}

	@Override
	public void update(float delta) {
		if (currentGameMode == GameMode.FLIGHT) {
			FieldCalculator.calculateFieldIntensity(getX(), getY(), LaplacityField.tiles, TMP1);
			body.applyForceToCenter(TMP1.scl(charge / getMass()), false);
		}
	}

	public void setSlingshot(float x, float y) {
		slingshotX = x;
		slingshotY = y;
	}
	
	public void makeParticleMoveWithStartVelocity() {
		body.setLinearVelocity(-slingshotX * TRAJECTORY_VELOCITY_MULTIPLIER, -slingshotY * TRAJECTORY_VELOCITY_MULTIPLIER);
	}
	
	public void resetToStartPosAndStartVelocity() {
		setPosition(startX, startY);
		body.setLinearVelocity(0, 0);
		body.setAngularVelocity(0);
	}
	
	public void drawStartVelocityArrow() {
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.YELLOW);
		shapeRenderer.line(getX(), getY(), getX() + slingshotX, getY() + slingshotY);
		shapeRenderer.end();
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.circle(getX() + slingshotX, getY() + slingshotY, ELECTRON_SIZE / 3);
		shapeRenderer.end();
	}

	public void calculateTrajectory(Vector2[] dest) {
		makeParticleMoveWithStartVelocity();
		for (int i = 0; i < TRAJECTORY_POINTS; i++) {
			dest[i].set(body.getTransform().getPosition());
			for (int j = 0; j < STEPS_PER_POINT; j++) {
				FieldCalculator.calculateFieldIntensity(body.getTransform().getPosition().x, body.getTransform().getPosition().y, LaplacityField.tiles, TMP1);
				body.applyForceToCenter(TMP1.scl(charge / getMass()), false);
				body.getWorld().step(PHYSICS_TIME_STEP, VELOCITY_STEPS, POSITION_STEPS);
			}
		}
		LaplacityField.resetStructures();
		resetToStartPosAndStartVelocity();
	}

	@Override
	public void collidedWithDeadly() {
		LaplacityAssets.playSound(LaplacityAssets.hurtSound);
		GameProcess.justHitted = true;
	}

	@Override
	public void collidedWithFinish() {
	}

	@Override
	public void collidedWithTile() {
		LaplacityAssets.playSound(LaplacityAssets.bumpSound);
	}

	@Override
	public void collidedWithStructure() {
		LaplacityAssets.playSound(LaplacityAssets.bumpStructureSound);
	}

}

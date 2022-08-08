package steelUnicorn.laplacity.particles;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.core.Globals.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
//import com.badlogic.gdx.math.MathUtils;
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
public class Cat extends ChargedParticle {

	// Start velocity
	private float slingshotX;
	private float slingshotY;

	// Prev pos
	private float prevX = 0f;
	private float prevY = 0f;
	
	// emoji
	private int currentEmoji = 0;
	
	public Cat() {
		super(LaplacityField.electronStartPos.x, LaplacityField.electronStartPos.y, CAT_SIZE, - PARTICLE_CHARGE, false, Color.WHITE);
		pointLight.setStaticLight(false);
		pointLight.setDistance(CAT_LIGHT_DISTANCE);
	}

	@Override
	public void draw() {
		GameProcess.gameBatch.enableBlending();
		GameProcess.gameBatch.draw(LaplacityAssets.CAT_REGIONS[currentEmoji % LaplacityAssets.CAT_REGIONS.length][currentEmoji / LaplacityAssets.CAT_REGIONS.length],
				interpX() - CAT_SIZE,
				interpY() - CAT_SIZE,
				CAT_SIZE,
				CAT_SIZE,
				2 * CAT_SIZE,
				2 * CAT_SIZE,
				1f,
				1f,
				0f //body.getAngle() * MathUtils.radDeg
				);
		GameProcess.gameBatch.disableBlending();
	}
	
	@Override
	public boolean isMainParticle() {
		return true;
	}

	public void updatePhysics(float delta) {
		if (currentGameMode == GameMode.FLIGHT) {
			FieldCalculator.calculateFieldIntensity(getX(), getY(), LaplacityField.tiles, TMP1);
			body.applyForceToCenter(TMP1.scl(charge / getMass()), false);
		}
		pointLight.setPosition(interpX(), interpY());
	}

	public void setSlingshot(float x, float y) {
		slingshotX = x;
		slingshotY = y;
	}
	
	public void makeParticleMoveWithStartVelocity() {
		body.setLinearVelocity(-slingshotX * TRAJECTORY_VELOCITY_MULTIPLIER, -slingshotY * TRAJECTORY_VELOCITY_MULTIPLIER);
	}
	
	public void resetToStartPosAndStartVelocity() {
		setPosition(LaplacityField.electronStartPos.x, LaplacityField.electronStartPos.y, 0);
		savePosition();
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
		shapeRenderer.circle(getX() + slingshotX, getY() + slingshotY, PARTICLE_SIZE / 3);
		shapeRenderer.end();
	}

	public void calculateTrajectory(Vector2[] dest) {
		makeParticleMoveWithStartVelocity();
		for (int i = 0; i < TRAJECTORY_POINTS; i++) {
			dest[i].set(body.getTransform().getPosition());
			for (int j = 0; j < STEPS_PER_POINT; j++) {
				FieldCalculator.calculateFieldIntensity(body.getTransform().getPosition().x, body.getTransform().getPosition().y, LaplacityField.tiles, TMP1);
				body.applyForceToCenter(TMP1.scl(charge / getMass()), false);
				body.getWorld().step(TRAJECTORY_TIME_STEP, VELOCITY_STEPS, POSITION_STEPS);
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
	public void collidedWithTile() {
		LaplacityAssets.playSound(LaplacityAssets.bumpSound);
		currentEmoji = (int) (Math.random() * LaplacityAssets.CAT_REGIONS.length * LaplacityAssets.CAT_REGIONS[0].length);
	}

	@Override
	public void collidedWithStructure() {
		LaplacityAssets.playSound(LaplacityAssets.bumpStructureSound);
	}

	public float interpX() {
		return (1 - interpCoeff) * prevX + interpCoeff * getX();
	}

	public float interpY() {
		return (1 - interpCoeff) * prevY + interpCoeff * getY();
	}

	/**
	 * Сохраняет текущее положение частицы во внутреннюю память частицы
	 * При прорисовке будет использована интерполяция между записанным
	 * и текущим физическим состоянием (текущее состояние опережает экранное время)
	 * с использованием глобального коэффициента {@linkplain GameProcess#interpCoeff}.
	 */
	public void savePosition() {
		prevX = getX();
		prevY = getY();
	}

}

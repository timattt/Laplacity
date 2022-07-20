package steelUnicorn.laplacity.particles;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.core.Globals.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import steelUnicorn.laplacity.GameMode;
import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.physics.FieldPotentialCalculator;

/**
 * Класс управляемой частицы.
 * Тут есть поле начальной скорости. При обновлении к телу добавляется сила от поля.
 * @author timat
 *
 */
public class ControllableElectron extends Electron {

	// Start velocity
	private float startVelocityX;
	private float startVelocityY;
	
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
			FieldPotentialCalculator.calculateFieldIntensity(getX(), getY(), LaplacityField.tiles, TMP1);
			body.applyForceToCenter(TMP1.scl(charge / getMass()), false);
		}
		super.act(delta);
	}

	public void setStartVelocity(float x, float y) {
		startVelocityX = x;
		startVelocityY = y;
	}
	
	public void getStartVelocity(Vector2 dest) {
		dest.x = startVelocityX;
		dest.y = startVelocityY;
	}
	
	public void makeParticleMoveWithStartVelocity() {
		body.setLinearVelocity(-startVelocityX, -startVelocityY);
	}
	
	public void resetToStartPosAndStartVelocity() {
		setPosition(startX, startY);
		body.setLinearVelocity(0, 0);
		body.setAngularVelocity(0);
	}
	
	public void drawStartVelocityArrow() {
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.YELLOW);
		shapeRenderer.line(getX(), getY(), getX() + startVelocityX, getY() + startVelocityY);
		shapeRenderer.end();
	}

	public void calculateTrajectory(Vector2[] dest) {
		makeParticleMoveWithStartVelocity();
		for (int i = 0; i < TRAJECTORY_POINTS; i++) {
			for (int j = 0; j < STEPS_PER_POINT; j++) {
				FieldPotentialCalculator.calculateFieldIntensity(body.getTransform().getPosition().x, body.getTransform().getPosition().y, LaplacityField.tiles, TMP1);
				body.applyForceToCenter(TMP1.scl(charge / getMass()), false);
				body.getWorld().step(PHYSICS_TIME_STEP, 1, 1);
			}
			
			dest[i].set(body.getTransform().getPosition());
		}
		resetToStartPosAndStartVelocity();
	}

}

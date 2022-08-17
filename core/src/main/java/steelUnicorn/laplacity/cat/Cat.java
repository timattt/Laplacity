package steelUnicorn.laplacity.cat;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.core.Globals.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.physics.CollisionListener;
import steelUnicorn.laplacity.field.physics.FieldCalculator;
import steelUnicorn.laplacity.gameModes.GameMode;
import steelUnicorn.laplacity.particles.CatAnimationManager;
import steelUnicorn.laplacity.particles.CatAnimationManager.CatAnimation;


/**
 * Класс управляемой частицы.
 * Тут есть поле начальной скорости. При обновлении к телу добавляется сила от поля.
 * @author timat
 *
 */
public class Cat implements CollisionListener {

	// Start velocity
	private float slingshotX;
	private float slingshotY;

	// Prev pos
	private float prevX = 0f;
	private float prevY = 0f;
	
	// body
	private Body body;
	
	public Cat() {
		prevX = LaplacityField.electronStartPos.x;
		prevY = LaplacityField.electronStartPos.y;
		
		BodyDef bodydef = new BodyDef();
		bodydef.type = BodyType.DynamicBody;
		bodydef.position.set(LaplacityField.electronStartPos.x, LaplacityField.electronStartPos.y);
		
		body = registerPhysicalObject(bodydef);
		
		CircleShape cir = new CircleShape();
		cir.setRadius(CAT_SIZE);
		
		FixtureDef fxt = new FixtureDef();
		fxt.shape = cir;
		fxt.density = 0.25f;
		fxt.restitution = 1f;
		fxt.friction = 0f;
		fxt.restitution = 0;
		
		body.createFixture(fxt);
		body.setUserData(this);
		
		cir.dispose();
		
		body.setBullet(true);
	}

	public float getX() {
		return body.getTransform().getPosition().x;
	}
	
	public float getY() {
		return body.getTransform().getPosition().y;
	}
	
	public void setPosition(float x, float y) {
		body.setTransform(x, y, 0);
		prevX = x;
		prevY = y;
	}
	
	public CatAnimation startAnimation(CatAnimation type) {
		return CatAnimationManager.startAnimation(type);
	}

	public void draw() {
		Globals.TMP3.set(interpX(), interpY(), 1f);
		CatAnimationManager.update(Globals.TMP3);
		
		GameProcess.gameBatch.enableBlending();
		GameProcess.gameBatch.draw(CatAnimationManager.getTextureRegion(),
				TMP3.x - CAT_SIZE,
				TMP3.y - CAT_SIZE,
				CAT_SIZE,
				CAT_SIZE,
				2 * CAT_SIZE,
				2 * CAT_SIZE,
				TMP3.z,
				TMP3.z,
				body.getAngle() * MathUtils.radDeg
				);
		GameProcess.gameBatch.disableBlending();
	}

	@Override
	public boolean isMainParticle() {
		return true;
	}

	public void updatePhysics(float delta) {
		if (currentGameMode == GameMode.FLIGHT && !CatAnimationManager.isPlayingSomeAnimation()) {
			FieldCalculator.calculateFieldIntensity(getX(), getY(), LaplacityField.tiles, TMP1);
			body.applyForceToCenter(TMP1.scl(-PARTICLE_CHARGE / body.getMass()), false);
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
		CatAnimationManager.stopCurrentAnimation();
		body.setTransform(LaplacityField.electronStartPos.x, LaplacityField.electronStartPos.y, 0);
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
		LaplacityField.resetStructures();
		for (int i = 0; i < TRAJECTORY_POINTS; i++) {
			dest[i].set(body.getTransform().getPosition());
			for (int j = 0; j < STEPS_PER_POINT; j++) {
				FieldCalculator.calculateFieldIntensity(body.getTransform().getPosition().x, body.getTransform().getPosition().y, LaplacityField.tiles, TMP1);
				body.applyForceToCenter(TMP1.scl(-PARTICLE_CHARGE / body.getMass()), false);
				body.getWorld().step(TRAJECTORY_TIME_STEP, VELOCITY_STEPS, POSITION_STEPS);
			}
		}
		LaplacityField.resetStructures();
		resetToStartPosAndStartVelocity();
	}

	@Override
	public void collidedWithDeadly() {
		if (CatAnimationManager.isPlayingSomeAnimation()) {
			return;
		}
		LaplacityAssets.playSound(LaplacityAssets.hurtSound);
		CatAnimationManager.startAnimation(CatAnimationManager.DEATH);
		body.setLinearVelocity(0, 0);
	}

	@Override
	public void collidedWithTile() {
		LaplacityAssets.playSound(LaplacityAssets.bumpSound);
		CatAnimationManager.setRandomEmoji();
	}

	public float interpX() {
		return (1 - interpCoeff) * prevX + interpCoeff * getX();
	}

	public float interpY() {
		return (1 - interpCoeff) * prevY + interpCoeff * getY();
	}

	@Override
	public void collidedWithTrampoline() {
		LaplacityAssets.playSound(LaplacityAssets.trampolineSound);
		CatAnimationManager.setRandomEmoji();
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

	public Vector2 getVelocity() {
		return body.getLinearVelocity();
	}

	@Override
	public boolean isDeadly() {
		return false;
	}

	@Override
	public void collidedWithMainParticle() {
	}

	@Override
	public boolean isStructure() {
		return false;
	}

	@Override
	public void collidedWithStructure() {
	}

	@Override
	public boolean isTile() {
		return false;
	}

	@Override
	public boolean isTrampoline() {
		return false;
	}

	public Body getBody() {
		return body;
	}
	
	
}
